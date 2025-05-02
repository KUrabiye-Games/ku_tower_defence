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

    private WaveManager waveManager; // Wave manager for handling enemy waves
    

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

    private GameState gameState; // Current state of the game

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
        while (gameState != GameState.GAME_LOST && gameState != GameState.GAME_WON) {
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
            int enemyIndex = 1;
           
            if (enemyIndex > -1 && !hasSpawnedTestEnemy) {
                System.out.println("GameManager.run(): Spawning test enemy (type: KNIGHT)");
                Enemy enemy = enemyFactory.createEnemy(enemyIndex); // Create a new enemy using the factory
                enemies.add(enemy); // Add the enemy to the list of enemies
               
                // DEBUG:
                enemyIndex = -1;
                hasSpawnedTestEnemy = true;

                System.out.println("GameManager.run(): Enemy created: " + enemy.getEnemyType() + " at position " + enemy.getCoordinate() + " (index: " + (enemies.size() - 1) + ")");
                System.out.println("GameManager.run(): hasSpawnedTestEnemy set to true");
            } else if (enemyIndex == -2) {
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

            // Towers look for targets and  create projectiles

            // Print the number of enemies
            System.out.println("Number of total enemies: " + enemies.size());
        

            for (Tower tower : towers) {
                // Check if the tower can attack     
                    // Get the projectile from the tower
                    Projectile projectile = tower.attack(enemies, deltaTime); // Attack enemies and get the projectile
                    if (projectile != null) {
                        projectiles.add(projectile); // Add the projectile to the list of projectiles
                    }
                
            }

            // Update projectiles position

            for (Projectile projectile : projectiles) {
                projectile.move(deltaTime); // Update each projectile's position
               
            }


            // Check for collisions between projectiles and enemies

            // Keep track of projectiles that need to be removed
            // Keep track of the enemies that are dead and need to be removed
            ArrayList<Enemy> enemiesToRemove = new ArrayList<>(); // List of enemies to remove
            ArrayList<Projectile> projectilesToRemove = new ArrayList<>(); // List of projectiles to remove

            for (Projectile projectile : projectiles) {

                // Check if any collision occurred
                boolean collisionOccurred = false; // Flag to check if a collision occurred

                for (Enemy enemy : enemies) {
                    if (projectile.getCoordinate().distance(enemy.getCoordinate()) < projectile.getProjectileAreaDamage()) { // Check for collision
                        enemy.getDamage(projectile.getProjectileType()); // Apply damage to the enemy
                        
                        if (enemy.isDead()) {
                            player.earnGold(enemy.getKillReward()); // Add gold to the player for killing the enemy
                            enemiesToRemove.add(enemy); // Mark the enemy for removal
                        }
                        collisionOccurred = true; // Set the collision flag to true
                        
                        if(projectile.getProjectileAreaDamage() <= 1f){
                            projectilesToRemove.add(projectile); // Mark the projectile for removal
                            break; // Exit the loop if a collision occurred
                        }
                    }
                }

                if(collisionOccurred) {
                    // Remove the projectile if it has collided with an enemy
                    projectilesToRemove.add(projectile); // Mark the projectile for removal
                }
            }


            // Remove dead enemies from the list
            enemies.removeAll(enemiesToRemove); // Remove the marked enemies from the list
            // Remove projectiles that have collided with enemies
            projectiles.removeAll(projectilesToRemove); // Remove the marked projectiles from the list
            // Remove projectiles that have reached their target


            


           
                if (gameUpdateListener != null) {
                    gameUpdateListener.onGameUpdate(deltaTime); // Call the update method on the listener
                }
                
            
             
           
            // Sleep for a short duration to control the frame rate
            try {
                Thread.sleep(160); // Approximately 60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
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
        // Handle game over logic
    }

    public GameState getGameState() {
        return gameState;
    }

    public void speedUpGame() {
        // Increase game speed
        double currentTimeCoefficient = gameTimer.getTimeCoefficient();
        if (currentTimeCoefficient < 4) { // Limit the maximum speed to 4x
            gameTimer.setTimeCoefficient(currentTimeCoefficient * 2);
        }
       
    }
    public void slowDownGame() {
        // Decrease game speed
        double currentTimeCoefficient = gameTimer.getTimeCoefficient();
        if (currentTimeCoefficient > 0.25) { // Limit the minimum speed to 0.25x
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

        return false; // Tower not found at the given coordinates

    }


 
    public void startGame() {
        System.out.println("GameManager.startGame(): Starting the game");
        
        // Create a new thread using this instance (which implements Runnable)
        Thread gameThread = new Thread(this);
        // Start the thread, which will call the run() method

        gameState = GameState.RUNNING; // Set the game state to RUNNING
        gameTimer.resetTimer();
        System.out.println("GameManager.startGame(): Game timer reset");
        
        gameThread.start();
        System.out.println("GameManager.startGame(): Game thread started");
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




 

}
