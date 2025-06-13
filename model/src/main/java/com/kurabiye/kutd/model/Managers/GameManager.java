package com.kurabiye.kutd.model.Managers;

import java.util.ArrayList;
import java.util.List;

import com.kurabiye.kutd.model.Collectable.ICollectable;
import com.kurabiye.kutd.model.Coordinates.Point2D;
import com.kurabiye.kutd.model.Enemy.IEnemy;
import com.kurabiye.kutd.model.Listeners.IGameUpdateListener;
import com.kurabiye.kutd.model.Map.GameMap;
import com.kurabiye.kutd.model.Player.Player;
import com.kurabiye.kutd.model.Projectile.IProjectile;
import com.kurabiye.kutd.model.Timer.GameTimer;
import com.kurabiye.kutd.model.Tower.ITower;
import com.kurabiye.kutd.model.Tower.TowerType;
import com.kurabiye.kutd.util.DynamicList.DynamicArrayList;


/** GameManager.java
 * This class is responsible for managing the game state, including
 * the game loop, player input, and game events. It will also handle
 * the interactions between different game components, such as enemies,
 * towers, and the game map.
 * 
 * 
 * 
 * @author: Atlas Berk Polat
 * @version: 1.0
 * @since: 2025-04-28
 */

public class GameManager implements Runnable{

    

    // The target frames per second for the game loop
    private static final int TARGET_FPS = 60; // Target frames per second

    
    // Thread management
    private Thread gameThread; // Reference to the game thread
    private volatile boolean running = true; // Flag to control thread execution


    private volatile GameState gameState; // Current state of the game

    private GameTimer gameTimer; // Game time

    private GameMap gameMap; // Game map

    private Player player; // Player object


    // These should be retrieved from the respective managers
    // Use the dynamic array list to store enemies and projectiles

    private List<ITower> towers; // List of towers in the game
    
    private List<IEnemy> enemies; // List of enemies in the game

    private List<IProjectile> projectiles; // List of projectiles in the game


    // The callback for the view update method
    private IGameUpdateListener gameUpdateListener; // Listener for game updates


    



    // Helper managers

    private TowerManager towerManager; // Manager for handling tower-related operations

    private EnemyManager enemyManager; // Manager for handling enemy-related operations

    private ProjectileManager projectileManager; // Manager for handling projectile-related operations

    private CollisionManager collisionManager; // Manager for handling collisions between projectiles and enemies

    private MainEffectManager effectManager; // Manager for handling effects like synergetic movement

    private CollactableManager collectableManager; // Manager for handling collectable items


    public GameManager(GameMap gameMap) {
        this.gameState = GameState.INITIALIZING; // Initialize game state to RUNNING
        this.gameTimer = GameTimer.getInstance(); // Get the singleton instance of GameTimer
        this.gameTimer.setTimeCoefficient(1); // Set the time coefficient to 1 (normal speed)
        this.gameMap = gameMap; // Initialize the game map
        this.player = new Player(); // Initialize the player object
        

        
        this.enemyManager = new EnemyManager((ArrayList<Point2D>) gameMap.getPointPath()); // Initialize the enemy manager
        this.enemies = enemyManager.getEnemies();

        
        this.projectileManager = new ProjectileManager(); // Initialize the projectile manager
        this.projectiles = projectileManager.getProjectiles(); // Get the list of projectiles from the projectile manager



        this.towerManager = new TowerManager(gameMap, player, projectileManager, enemies);
        this.towers = towerManager.getTowers();

        this.collisionManager = new CollisionManager(enemyManager.getDynamicEnemies(), projectileManager.getDynamicProjectiles()); // Initialize the collision manager with enemies and projectiles


        this.effectManager = new MainEffectManager(enemyManager.getDynamicEnemies()); // Initialize the effect manager with enemies

        this.collisionManager.setSlowDownManager(effectManager.getSlowDownManager()); // Set the slow down manager in the collision manager

        this.collectableManager = new CollactableManager(player); // Initialize the collectable manager with the player

        this.collisionManager.setCollectableManager(collectableManager); // Set the collectable manager in the collision manager
    }

    public void setGameUpdateListener(IGameUpdateListener gameUpdateListener) {
        this.gameUpdateListener = gameUpdateListener; // Set the game update listener
    }

    @Override
    public void run() {
 
        // Debugging: Print the initial game state
        // System.out.println("Game started with state: " + gameState);

        while (running && gameState != GameState.GAME_LOST && gameState != GameState.GAME_WON) {
            
        gameTimer.update();


            while (running && gameState != GameState.PAUSED ) {
                
            // Check if thread has been requested to stop
            if (!running) {
                break;
            }
            
            // Update game state
            if (gameState == GameState.PAUSED) {
                // Pause game logic
                try {
                    Thread.sleep(100); // Sleep for a short duration to avoid busy waiting
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue; // Skip the rest of the loop if the game is paused
            }

            // Calculate the delta time
            gameTimer.update(); // Update the game timer
            double deltaTime = gameTimer.getDeltaTime(); // Get the delta time from the game timer



            // Spawn enemies based on the current wave and game state
            if(!enemyManager.spawnEnemies(deltaTime)) {
                // If there are no more enemies to spawn, check if the game is won
                if (enemies.size() == 0) {
                    gameState = GameState.GAME_WON; // Set game state to GAME_WON
                }
            }

            

            // Move enemies based on the elapsed time
            int arrivedEnemiesCount = enemyManager.moveEnemies(deltaTime); // Move enemies and get the count of arrived enemies

            if (arrivedEnemiesCount > 0) {
                // If any enemies have arrived, reduce the player's health
                player.loseHealth(arrivedEnemiesCount); // Reduce player's health based on the number of arrived enemies
                
            }
            // Create projectiles for the towers

            towerManager.createProjectiles(deltaTime); // Create projectiles for the towers

            // Move projectiles based on the elapsed time
            projectileManager.moveProjectiles(deltaTime); // Move all projectiles based on the elapsed time

           

            // Check for collisions between projectiles and enemies
            int totalGoldEarned = collisionManager.calculateCollisions(deltaTime); // Calculate collisions and get total gold earned


            // Check the explosion type of projectiles

            totalGoldEarned += collisionManager.calculateExplosions(deltaTime); // Check the explosion type of projectiles and add gold earned

            // Add gold to the player's total gold

            if (totalGoldEarned > 0) {
                player.earnGold(totalGoldEarned); // Add the total gold earned to the player's gold
            }


            // Check for synergetic movement behavior
            effectManager.applyEffects(deltaTime);// Apply synergetic movement effects to enemies


            // update collectables
            collectableManager.updateCollectables(deltaTime); // Update collectables based on the elapsed time

            

            // Check if the game is over
            if (player.getCurrentHealth() <= 0) {
                gameState = GameState.GAME_LOST; // Set game state to GAME_LOST
            }

            // Let the view if there is a game update listener

            if (gameUpdateListener != null) {
                gameUpdateListener.onGameUpdate(deltaTime); // Call the update method on the listener
            }
             
           
            // Sleep for a short duration to control the frame rate
            try {
                Thread.sleep((long)((1000/TARGET_FPS)/ gameTimer.getTimeCoefficient())); // Approximately 60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

         }

         // Wait to resume the game if it is paused
            try {
                Thread.sleep((long)((100)/ gameTimer.getTimeCoefficient())); // Approximately 60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
        
    }

    

    // Controller methods
    public void pauseGame() {
        gameState = GameState.PAUSED;
        // Pause game logic
    }
    
    public void resumeGame() {
        gameState = GameState.RUNNING;
        // Resume game logic
    }
    
    public void endGame() {
        gameState = GameState.GAME_LOST;
        running = false; // Stop the game loop
        // Handle game over logic
    }

    public void speedUpGame() {
        // Increase game speed
        gameTimer.setTimeCoefficient(2.0);
    }
    
    public void slowDownGame() {
        // Decrease game speed
        gameTimer.setTimeCoefficient(1);
    }


    /**
     * This method starts the game by creating a new thread and setting the game state to RUNNING.
     * It also resets the game timer to start counting from zero.
     * @requires gameState == GameState.INITIALIZING
     * @modifies gameState, gameTimer
     * @effects Starts the game by creating a new thread and setting the game state to RUNNING.
     *         Resets the game timer to start counting from zero.
     * 
     */
   

    public void startGame() {
        // Create a new thread using this instance (which implements Runnable)
        gameThread = new Thread(this);
        // Start the thread, which will call the run() method

        gameState = GameState.RUNNING; // Set the game state to RUNNING
        gameTimer.resetTimer();
        
        gameThread.start();
    }

    public void killGameThread() {
        if (gameThread != null && gameThread.isAlive()) {
            running = false; // Signal the thread to stop
            gameState = GameState.GAME_LOST; // Force game state to end
            
            try {
                // Wait for the thread to die, but with a timeout
                gameThread.join(1000);
                
                // If the thread is still alive after timeout, interrupt it
                if (gameThread.isAlive()) {
                    gameThread.interrupt();
                    gameThread.join(1000); // Give it a second chance to terminate
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // Clear game resources
        enemies.clear();
        projectiles.clear();
        
        // Reset the game timer
        gameTimer.resetTimer();
    }

    // Info provider methods for the view

    public GameMap getGameMap() {
        return gameMap; // Get the game map
    }


    public List<ITower> getTowers() {
        return towers; // Return the list of towers
    }

    public List<IEnemy> getEnemies() {
        return enemies; // Return the list of enemies
    }

    public List<IProjectile> getProjectiles() {
        return projectiles; // Return the list of projectiles
    }

    public Player getPlayer() {
        return player; // Return the player object
    }

    public GameTimer getGameTimer() {
        return gameTimer; // Return the game timer
    }

    public GameState getGameState() {
        return gameState; // Return the current game state
    }

    public void returnToMainMenu() {
        gameState = GameState.INITIALIZING;
        running = false; // Stop the game loop
        // Handle return to main menu logic
        
    }


    /**
     * Information provider methods for the wave manager
     * This method provides information about the current wave and group index.
     * It only delegates the call to the wave manager.
     * 
     * @returns the current wave index and group index
     */

    public int getCurrentWaveIndex() {
        return enemyManager.getWaveManager().getCurrentWaveIndex(); // Return the current wave index
    }

    /**
     * This method provides information about the current group index.
     * It only delegates the call to the wave manager.
     * 
     * @returns the current group index
     */
    public int getCurrentGroupIndex() {
        return enemyManager.getWaveManager().getCurrentGroupIndex(); // Return the current group index
    }

     /**
     * This metthods delegates the call to the tower manager to build a tower at the specified coordinates.
     * @effects gameMap, player, towers
     * 
     * @param xCoordinate The x coordinate where the tower should be built
     * @param yCoordinate The y coordinate where the tower should be built
     * @param towerType The type of tower to be built
     * @return true if the tower was successfully built, false otherwise
     */
    public boolean buildTower(int xCoordinate, int yCoordinate, TowerType towerType) {
        return towerManager.buildTower(xCoordinate, yCoordinate, towerType); // Build a tower at the specified coordinates
    }
     /**
     * This metthods delegates the call to the tower manager to sell a tower at the specified coordinates.
     * @effects gameMap, player, towers
     * 
     * @param xCoordinate The x coordinate where the tower should be built
     * @param yCoordinate The y coordinate where the tower should be built
     * @param towerType The type of tower to be sold
     * @return true if the tower was successfully sold, false otherwise
     */
    public boolean sellTower(int xCoordinate, int yCoordinate) {
        return towerManager.sellTower(xCoordinate, yCoordinate); // Sell the tower at the specified coordinates
    }
 /**
     * This metthods delegates the call to the tower manager to upgrade a tower at the specified coordinates.
     * @effects gameMap, player, towers
     * 
     * @param xCoordinate The x coordinate where the tower should be built
     * @param yCoordinate The y coordinate where the tower should be built
     * @param towerType The type of tower to be upgraded
     * @return true if the tower was successfully upgraded, false otherwise
     */
    public boolean upgradeTower(int xCoordinate, int yCoordinate) {
        return towerManager.upgradeTower(xCoordinate, yCoordinate); // Upgrade the tower at the specified coordinates
    }

    /**
     * Handle user clicks for collecting items
     * This method delegates the call to the collectable manager to handle clicks on collectable items.
     * @param clickPosition The position where the user clicked
     * @return true if a collectable was collected, false otherwise
     */
    public boolean handleCollectableClick(Point2D clickPosition) {
        return collectableManager.handleClick(clickPosition);
    }


     /**
     * Get collectables for view rendering
     * This method provides the list of collectables to be rendered in the view.
     * It delegates the call to the collectable manager.
     * 
     * @return The list of collectables
     */
    public DynamicArrayList<ICollectable<?>> getCollectables() {
        return collectableManager.getCollectables();
    }

    
}
