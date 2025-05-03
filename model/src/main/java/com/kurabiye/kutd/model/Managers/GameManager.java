package com.kurabiye.kutd.model.Managers;

import java.util.ArrayList;
import java.util.Iterator;

import com.kurabiye.kutd.model.Coordinates.TilePoint2D;
import com.kurabiye.kutd.model.Coordinates.Point2D;
import com.kurabiye.kutd.model.Enemy.Enemy;
import com.kurabiye.kutd.model.Enemy.EnemyFactory;
import com.kurabiye.kutd.model.Listeners.IGameUpdateListener;
import com.kurabiye.kutd.model.Map.GameMap;
import com.kurabiye.kutd.model.Player.Player;
import com.kurabiye.kutd.model.Player.UserPreference;
import com.kurabiye.kutd.model.Projectile.Projectile;
import com.kurabiye.kutd.model.Tile.Tile;
import com.kurabiye.kutd.model.Tile.TileFactory;
import com.kurabiye.kutd.model.Timer.GameTimer;
import com.kurabiye.kutd.model.Tower.Tower;
import com.kurabiye.kutd.model.Tower.TowerFactory;
import com.kurabiye.kutd.model.Tower.TowerFactory.TowerType;

/* GameManager.java
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


    private static final int TARGET_FPS = 60; // Target frames per second

    private WaveManager waveManager; // Wave manager for handling enemy waves
    
    // Thread management
    private Thread gameThread; // Reference to the game thread
    private volatile boolean running = true; // Flag to control thread execution

    // TODO: Debugging: Remove this flag later
    // Add a flag to control test enemy spawning
    private boolean hasSpawnedTestEnemy = false;

    public enum GameState {
        INITIALIZING,
        RUNNING,
        PAUSED,
        GAME_LOST,
        GAME_WON,
    }

    private volatile GameState gameState; // Current state of the game

    private GameTimer gameTimer; // Game time

    private GameMap gameMap; // Game map

    private Player player; // Player object

    private UserPreference userPreferences; // User preferences object


    private TowerFactory towerFactory; // Tower factory for creating towers

    private EnemyFactory enemyFactory; // Enemy factory for creating enemies


    private ArrayList<Point2D> path; // Path for enemies to follow

    private ArrayList<Tower> towers; // List of towers in the game
    private ArrayList<Enemy> enemies; // List of enemies in the game
    private ArrayList<Projectile> projectiles; // List of projectiles in the game


    // Tile Factory to build new Towers
    private TileFactory tileFactory; // Tile factory for creating tiles


    // The callback for the view update method
    
    private IGameUpdateListener gameUpdateListener; // Listener for game updates


    public GameManager(GameMap gameMap) {
        this.gameState = GameState.INITIALIZING; // Initialize game state to RUNNING
        this.gameTimer = GameTimer.getInstance(); // Get the singleton instance of GameTimer
        this.gameTimer.setTimeCoefficient(1); // Set the time coefficient to 1 (normal speed)
        this.gameMap = gameMap; // Initialize the game map
        this.userPreferences = UserPreference.getInstance(); // Initialize user preferences
        this.player = new Player(userPreferences); // Initialize the player object

        path = (ArrayList<Point2D>) gameMap.getPointPath(); // Get the path from the game map

        this.waveManager = new WaveManager(this.userPreferences); // Initialize the wave manager
        this.towerFactory = TowerFactory.getInstance(); // Initialize the tower factory

        this.enemyFactory = EnemyFactory.getInstance(); // Initialize the enemy factory
        this.enemyFactory.setEnemyPath(path); // Set the enemy path in the factory
        
        this.towers = new ArrayList<>(); // Initialize the list of towers
        this.enemies = new ArrayList<>(); // Initialize the list of enemies
        this.projectiles = new ArrayList<>(); // Initialize the list of projectiles

        this.tileFactory = new TileFactory(); // Initialize the tile factory
    }

    public void setGameUpdateListener(IGameUpdateListener gameUpdateListener) {
        this.gameUpdateListener = gameUpdateListener; // Set the game update listener
    }




    /* Do not use this method directly.
     * 
     * 
     */

    @Override
    public void run() {
        System.out.println("GameManager.run(): Game loop starting");

        // Game loop
        while (running && gameState != GameState.GAME_LOST && gameState != GameState.GAME_WON) {

            gameTimer.resetTimer();
            while (running && gameState != GameState.PAUSED) {
                
            // Check if thread has been requested to stop
            if (!running) {
                System.out.println("GameManager.run(): Thread stop requested, exiting game loop");
                break;
            }
            
            // Update game state
            System.out.println("GameManager.run(): Current game state: " + gameState);

            if (gameState == GameState.PAUSED) {
                // Pause game logic
                System.out.println("GameManager.run(): Game is paused");
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
            System.out.println("GameManager.run(): Delta time: " + deltaTime);

            // Create new enemies
            System.out.println("GameManager.run(): Checking if we should spawn an enemy");
            System.out.println("GameManager.run(): hasSpawnedTestEnemy = " + hasSpawnedTestEnemy);
            // Using our test enemy code
            int enemyIndex = waveManager.getEnemy(deltaTime); // Get the index of the enemy to spawn
            
            /*if (!hasSpawnedTestEnemy) {
                enemyIndex = 0; // Set to 0 to spawn a test enemy
            }*/
            // */ print the enemy index
            System.out.println("GameManager.run(): Enemy index: " + enemyIndex);
           
            if (enemyIndex > -1) {
                System.out.println("GameManager.run(): Spawning test enemy (type: KNIGHT)");
                Enemy enemy = enemyFactory.createEnemy(enemyIndex); // Create a new enemy using the factory
                enemies.add(enemy); // Add the enemy to the list of enemies
               
               

                System.out.println("GameManager.run(): Enemy created: " + enemy.getEnemyType() + " at position " + enemy.getCoordinate() + " (index: " + (enemies.size() - 1) + ")");
                System.out.println("GameManager.run(): hasSpawnedTestEnemy set to true");
            } else if (enemyIndex == -2 && enemies.size() == 0) {
                // No enemies left to spawn
                System.out.println("GameManager.run(): No enemies left to spawn, game won");
                gameState = GameState.GAME_WON; // Set game state to GAME_WON
            }

            // Update enemies position
            System.out.println("GameManager.run(): Updating enemy positions. Current count: " + enemies.size());
            
            Iterator<Enemy> enemyIterator = enemies.iterator(); // Create an iterator for the list of enemies
            int enemyIndex2 = 0;
            while (enemyIterator.hasNext()) {
                Enemy enemy = enemyIterator.next(); // Get the next enemy
                Point2D oldPosition = enemy.getCoordinate();
                enemy.move(deltaTime); // Update the enemy's position

                // Print the delta time
                System.out.println("Delta time: " + deltaTime + " seconds");
                
                // Log the position update
                System.out.println("Enemy " + enemyIndex2 + " (" + enemy.getEnemyType() + ") moved from " + oldPosition + " to " + enemy.getCoordinate());
                
                if (enemy.hasArrived()) {
                    System.out.println("Enemy " + enemyIndex2 + " has arrived at destination and will be removed");
                    enemyIterator.remove(); // Remove the enemy from the list if it is out of bounds
                    player.loseHealth(); // Deduct health from the player
                } else {
                    enemyIndex2++; // Only increment if the enemy wasn't removed
                }
            }

            // Towers look for targets and create projectiles
            System.out.println("Number of total enemies: " + enemies.size());
            System.out.println("Number of towers: " + towers.size());
            System.out.println("Number of projectiles: " + projectiles.size());
        
            for (Tower tower : towers) {
                // Check if the tower can attack     
                // Get the projectile from the tower
                Projectile projectile = tower.attack(enemies, deltaTime); // Attack enemies and get the projectile
                if (projectile != null) {
                    projectiles.add(projectile); // Add the projectile to the list of projectiles
                    System.out.println("New projectile created of type " + projectile.getProjectileType() + 
                                       " at " + projectile.getCoordinate() + 
                                       " heading to " + projectile.getSpeedVector());
                }
            }

            // Update projectiles position
            System.out.println("Updating positions of " + projectiles.size() + " projectiles");
            for (Projectile projectile : projectiles) {
                Point2D oldPos = projectile.getCoordinate();
                projectile.move(deltaTime); // Update each projectile's position
                System.out.println("Projectile moved from " + oldPos + " to " + projectile.getCoordinate());
            }

            // Check for collisions between projectiles and enemies
            System.out.println("Checking for collisions...");

            // Keep track of projectiles that need to be removed
            // Keep track of the enemies that are dead and need to be removed
            ArrayList<Enemy> enemiesToRemove = new ArrayList<>(); // List of enemies to remove
            ArrayList<Projectile> projectilesToRemove = new ArrayList<>(); // List of projectiles to remove
            int collisionCount = 0;

            for (Projectile projectile : projectiles) {
                // Check if any collision occurred
                boolean collisionOccurred = false; // Flag to check if a collision occurred

                for (Enemy enemy : enemies) {
                    double distance = projectile.getCoordinate().distance(enemy.getCoordinate());
                    float damageRadius = projectile.getProjectileAreaDamage();

                    // Console log for debugging

                    System.out.println("Checking collision: Projectile at " + projectile.getCoordinate() + 
                                       " with enemy at " + enemy.getCoordinate() + 
                                       " (distance: " + distance + ", damage radius: " + damageRadius + ")");
                    
                    if (distance < damageRadius) { // Check for collision
                        collisionCount++;
                        System.out.println("Collision detected! Projectile at " + projectile.getCoordinate() + 
                                           " hit enemy at " + enemy.getCoordinate() + 
                                           " (distance: " + distance + ", damage radius: " + damageRadius + ")");
                        
                        System.out.println("Enemy health before damage: " + enemy.getHealth());
                        enemy.getDamage(projectile.getProjectileType()); // Apply damage to the enemy
                        System.out.println("Enemy health after damage: " + enemy.getHealth());
                        
                        if (enemy.isDead()) {
                            int reward = enemy.getKillReward();
                            System.out.println("Enemy killed! Player earned " + reward + " gold");
                            player.earnGold(reward); // Add gold to the player for killing the enemy
                            enemiesToRemove.add(enemy); // Mark the enemy for removal
                        }
                        collisionOccurred = true; // Set the collision flag to true
                        
                        if(projectile.getProjectileAreaDamage() <= 1f){
                            projectilesToRemove.add(projectile); // Mark the projectile for removal
                            System.out.println("Single-target projectile marked for removal");
                            break; // Exit the loop if a collision occurred
                        }
                    }
                }

                if(collisionOccurred) {
                    // Remove the projectile if it has collided with an enemy
                    projectilesToRemove.add(projectile); // Mark the projectile for removal
                    System.out.println("Projectile marked for removal after collision");
                }
            }

            System.out.println("Total collisions this frame: " + collisionCount);
            System.out.println("Enemies to remove: " + enemiesToRemove.size());
            System.out.println("Projectiles to remove: " + projectilesToRemove.size());

            // Remove dead enemies from the list
            enemies.removeAll(enemiesToRemove); // Remove the marked enemies from the list
            // Remove projectiles that have collided with enemies
            projectiles.removeAll(projectilesToRemove); // Remove the marked projectiles from the list
            
            System.out.println("After cleanup - Enemies: " + enemies.size() + ", Projectiles: " + projectiles.size());

            if (gameUpdateListener != null) {
                gameUpdateListener.onGameUpdate(deltaTime); // Call the update method on the listener
            }
             
           
            // Sleep for a short duration to control the frame rate
            try {
                Thread.sleep(1000/TARGET_FPS); // Approximately 60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

         }
        }
        System.out.println("GameManager.run(): Game loop ended with state: " + gameState);
    }




    public GameMap getGameMap() {
        return gameMap; // Get the game map
    }



    // Conroller methods

    // Controller methods
    public void pauseGame() {
        System.out.println("GameManager.pauseGame(): Pausing the game");
        gameState = GameState.PAUSED;
        // Pause game logic
    }
    public void resumeGame() {
        System.out.println("GameManager.resumeGame(): Resuming the game");
        gameState = GameState.RUNNING;
        // Resume game logic
    }
    public void endGame() {
        System.out.println("GameManager.endGame(): Ending the game");
        gameState = GameState.GAME_LOST;
        running = false; // Stop the game loop
        // Handle game over logic
    }

    public GameState getGameState() {
        return gameState;
    }

    public void returnToMainMenu() {
        System.out.println("GameManager.returnToMainMenu(): Returning to main menu");
        gameState = GameState.INITIALIZING;
        running = false; // Stop the game loop
        // Handle return to main menu logic
        
    }

    public void speedUpGame() {
        // Increase game speed
        double currentTimeCoefficient = gameTimer.getTimeCoefficient();
        if (currentTimeCoefficient < 2) { // Limit the maximum speed to 4x
            gameTimer.setTimeCoefficient(currentTimeCoefficient * 2);
        }
       
    }
    public void slowDownGame() {
        // Decrease game speed
        double currentTimeCoefficient = gameTimer.getTimeCoefficient();
        if (currentTimeCoefficient > 0.5) { // Limit the minimum speed to 0.25x
            gameTimer.setTimeCoefficient(currentTimeCoefficient / 2);
        }
    }

    public boolean buildTower(int xCoordinate, int yCoordinate, int towerType) {
        System.out.println("GameManager.buildTower(): Attempting to build tower type " + towerType + " at position (" + xCoordinate + "," + yCoordinate + ")");
        
        //check if the tile is buildable

        Tile tile = gameMap.getTile(xCoordinate, yCoordinate);
        if (tile == null) {
            System.out.println("GameManager.buildTower(): Invalid tile coordinates");
            return false; // Invalid tile coordinates
        }
        if (!tile.isBuildableTile()) {
            System.out.println("GameManager.buildTower(): Tile is not buildable");
            return false; // Tile is not buildable
        }

        // Check if the player has enough resources

        if(player.getCurrentGold() < userPreferences.getTowerConstructionCost()[towerType]) { // Example cost check
            System.out.println("GameManager.buildTower(): Not enough gold. Required: " + userPreferences.getTowerConstructionCost()[towerType] + ", Available: " + player.getCurrentGold());
            return false; // Not enough gold
        }

        System.out.println("GameManager.buildTower(): Deducting " + userPreferences.getTowerConstructionCost()[towerType] + " gold from player");
        player.buyTower(userPreferences.getTowerConstructionCost()[towerType]); // Deduct cost from player's gold
        // Create the tower using the TowerFactory

        Tower tower = towerFactory.create(TowerType.values()[towerType]); // Create the tower using the factory
        // Set the tower's coordinates
        tower.setTileCoordinate(new TilePoint2D(xCoordinate, yCoordinate));
        // Add the tower to the list of towers
        towers.add(tower);
        System.out.println("GameManager.buildTower(): Tower built successfully. Total towers: " + towers.size());


        int tileCode;

        if (towerType == 0) {
            tileCode = 20; // Example tile code for tower type 0
        } else if (towerType == 1) {
            tileCode = 21; // Example tile code for tower type 1
        } else if (towerType == 2) {
            tileCode = 26; // Example tile code for tower type 2
        } else {
            System.out.println("GameManager.buildTower(): Invalid tower type");
            return false; // Invalid tower type
            
        }

        Tile towerTile = tileFactory.create(tileCode); // Create the tower tile using the factory



        gameMap.setTile(xCoordinate, yCoordinate, towerTile);

        return true;
    }
    public boolean sellTower(int xCoordinate, int yCoordinate, int towerType) {
        // Logic to sell a tower
        // Check if the tower exists at the given coordinates

        // loof for a tower in the list of towers

        for (Tower tower : towers) {
            if (tower.getTileCoordinate().getTileX() == xCoordinate && tower.getTileCoordinate().getTileY() == yCoordinate) {
                // Tower found, sell it
                player.sellTower(tower.getSellReturn()); // Add sell return to player's gold
                towers.remove(tower); // Remove the tower from the list
                return true; // Tower sold successfully
            }
        }

        // Remove the tile from the map

        

        Tile buildableTile = this.tileFactory.create(15); // Create a buildable tile using the factory

        gameMap.setTile(xCoordinate, yCoordinate, buildableTile); // Set the tile to a buildable tile
    
        return false; // Tower not found at the given coordinates

    }


 
    public void startGame() {
        System.out.println("GameManager.startGame(): Starting the game");
        
        // Create a new thread using this instance (which implements Runnable)
        gameThread = new Thread(this);
        // Start the thread, which will call the run() method

        gameState = GameState.RUNNING; // Set the game state to RUNNING
        gameTimer.resetTimer();
        System.out.println("GameManager.startGame(): Game timer reset");
        
        gameThread.start();
        System.out.println("GameManager.startGame(): Game thread started");
    }

    public void killGameThread() {
        System.out.println("GameManager.killGameThread(): Attempting to kill game thread");
        if (gameThread != null && gameThread.isAlive()) {
            running = false; // Signal the thread to stop
            gameState = GameState.GAME_LOST; // Force game state to end
            
            try {
                // Wait for the thread to die, but with a timeout
                gameThread.join(1000);
                
                // If the thread is still alive after timeout, interrupt it
                if (gameThread.isAlive()) {
                    System.out.println("GameManager.killGameThread(): Thread didn't terminate normally, interrupting");
                    gameThread.interrupt();
                    gameThread.join(1000); // Give it a second chance to terminate
                }
                
                System.out.println("GameManager.killGameThread(): Game thread successfully terminated");
            } catch (InterruptedException e) {
                System.out.println("GameManager.killGameThread(): Interrupted while waiting for game thread to terminate");
                e.printStackTrace();
            }
        } else {
            System.out.println("GameManager.killGameThread(): No active game thread to kill");
        }
        
        // Clear game resources
        enemies.clear();
        projectiles.clear();
        
        // Reset the game timer
        gameTimer.resetTimer();
    }

    // Info provider methods for the view


    public ArrayList<Tower> getTowers() {
        return towers; // Return the list of towers
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies; // Return the list of enemies
    }

    public ArrayList<Projectile> getProjectiles() {
        return projectiles; // Return the list of projectiles
    }

    public Player getPlayer() {
        return player; // Return the player object
    }

    public GameTimer getGameTimer() {
        return gameTimer; // Return the game timer
    }

    public UserPreference getUserPreferences() {
        return userPreferences; // Return the user preferences
    }

    public int getCurrentWaveIndex() {
        return waveManager.getCurrentWaveIndex(); // Return the current wave index
    }

    public int getCurrentGroupIndex() {
        return waveManager.getCurrentGroupIndex(); // Return the current group index
    }

}
