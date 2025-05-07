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
import com.kurabiye.kutd.model.Projectile.Projectile.DamageType;

import com.kurabiye.kutd.model.Projectile.Projectile.ProjectileState;
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


    // Add a flag to control test enemy spawning


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


    private ArrayList<Enemy> enemiesToRemove = new ArrayList<>(); // List of enemies to remove
    private ArrayList<Projectile> projectilesToRemove = new ArrayList<>(); // List of projectiles to remove


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

    @Override
    public void run() {
        // Game loop
        while (running && gameState != GameState.GAME_LOST && gameState != GameState.GAME_WON) {

            gameTimer.resetTimer();
            while (running && gameState != GameState.PAUSED) {
                
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

            // Create new enemies
            // Using our test enemy code
            int enemyIndex = waveManager.getEnemy(deltaTime); // Get the index of the enemy to spawn
           
            if (enemyIndex > -1) {
                Enemy enemy = enemyFactory.createEnemy(enemyIndex); // Create a new enemy using the factory
                enemies.add(enemy); // Add the enemy to the list of enemies
            } else if (enemyIndex == -2 && enemies.size() == 0) {
                // No enemies left to spawn
                gameState = GameState.GAME_WON; // Set game state to GAME_WON
            }

            // Update enemies position
            Iterator<Enemy> enemyIterator = enemies.iterator(); // Create an iterator for the list of enemies

            while (enemyIterator.hasNext()) {
                Enemy enemy = enemyIterator.next(); // Get the next enemy
               
                enemy.move(deltaTime); // Update the enemy's position
                
                if (enemy.hasArrived()) {
                    enemyIterator.remove(); // Remove the enemy from the list if it is out of bounds
                    player.loseHealth(); // Deduct health from the player
                }
            }

            // Towers look for targets and create projectiles
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
            
            projectilesToRemove.clear(); // Clear the list of projectiles to remove
            enemiesToRemove.clear(); // Clear the list of enemies to remove

            for (Projectile projectile : projectiles) {
                // Check if any collision occurred

                if (projectile.getProjectileState() == ProjectileState.DEAD) {
                    projectilesToRemove.add(projectile); // Skip if the projectile is stopped
                }

                if (projectile.getProjectileState() == ProjectileState.STOPPED) {
                    continue; // Skip if the projectile is stopped
                }

                if (projectile.getProjectileState() == ProjectileState.MOVING) {
                    continue; // Skip if the projectile is dead
                }

                if (projectile.getExplosionType() == DamageType.AREA) {
                    continue; // Skip if the projectile is an area explosion
                    
                }

                boolean collisionOccurred = false; // Flag to check if a collision occurred

                for (Enemy enemy : enemies) {
                    double distance = projectile.getCoordinate().distance(enemy.getCoordinate());
                    float damageRadius = projectile.getProjectileAreaDamage();

                    double distanceToTarget = projectile.getCoordinate().distance(projectile.getTarget());
                    
                    if (distance < damageRadius) { // Check for collision
                       
                        
                        enemy.getDamage(projectile.getProjectileType()); // Apply damage to the enemy
                        
                        if (enemy.isDead()) {
                            int reward = enemy.getKillReward();
                            player.earnGold(reward); // Add gold to the player for killing the enemy
                            enemiesToRemove.add(enemy); // Mark the enemy for removal
                        }
                        collisionOccurred = true; // Set the collision flag to true
                        
                        if(projectile.getProjectileAreaDamage() <= 1f){
                            projectilesToRemove.add(projectile); // Mark the projectile for removal
                            break; // Exit the loop if a collision occurred
                        }
                    }else if(distanceToTarget < deltaTime * projectile.getSpeedVector().magnitude()){
                        // Check if the projectile has reached its target


                             enemy.getDamage(projectile.getProjectileType()); // Apply damage to the enemy
                        
                            if (enemy.isDead()) {
                            int reward = enemy.getKillReward();
                            player.earnGold(reward); // Add gold to the player for killing the enemy
                            enemiesToRemove.add(enemy); // Mark the enemy for removal
                            }
                            collisionOccurred = true; // Set the collision flag to true
                        
                            projectilesToRemove.add(projectile); // Mark the projectile for removal
                            
                        

                    }
                }

                if(collisionOccurred) {
                    // Remove the projectile if it has collided with an enemy
                    projectilesToRemove.add(projectile); // Mark the projectile for removal
                }
            }


            // Check the explosion type of projectiles

            for (Projectile projectile : projectiles) {
                if (projectile.getExplosionType() == DamageType.AREA) {
                    // check if the projectile is close enough to the its target
                    double distance = projectile.getCoordinate().distance(projectile.getTarget());

                    if (distance < deltaTime * projectile.getSpeedVector().magnitude()) {
                        // Apply area damage to all enemies within the explosion radius
                        for (Enemy enemy : enemies) {
                            double distanceToEnemy = projectile.getCoordinate().distance(enemy.getCoordinate());
                            if (distanceToEnemy < projectile.getProjectileAreaDamage()) {
                                enemy.getDamage(projectile.getProjectileType()); // Apply damage to the enemy
                                if (enemy.isDead()) {
                                    int reward = enemy.getKillReward();
                                    player.earnGold(reward); // Add gold to the player for killing the enemy
                                    enemiesToRemove.add(enemy); // Mark the enemy for removal
                                }
                            }
                        }
                        projectilesToRemove.add(projectile); // Mark the projectile for removal
                    }

                }
            }

            // Remove dead enemies from the list
            enemies.removeAll(enemiesToRemove); // Remove the marked enemies from the list
            // Remove projectiles that have collided with enemies
            projectiles.removeAll(projectilesToRemove); // Remove the marked projectiles from the list

            

            // Check if the game is over
            if (player.getCurrentHealth() <= 0) {
                gameState = GameState.GAME_LOST; // Set game state to GAME_LOST
            }

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
        }
    }

    public GameMap getGameMap() {
        return gameMap; // Get the game map
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

    public GameState getGameState() {
        return gameState;
    }

    public void returnToMainMenu() {
        gameState = GameState.INITIALIZING;
        running = false; // Stop the game loop
        // Handle return to main menu logic
        
    }

    public void speedUpGame() {
        // Increase game speed
        gameTimer.setTimeCoefficient(2.0);
    }
    
    public void slowDownGame() {
        // Decrease game speed
        gameTimer.setTimeCoefficient(1);
    }

    public boolean buildTower(int xCoordinate, int yCoordinate, int towerType) {
        //check if the tile is buildable
        Tile tile = gameMap.getTile(xCoordinate, yCoordinate);
        if (tile == null) {
            return false; // Invalid tile coordinates
        }
        if (!tile.isBuildableTile()) {
            return false; // Tile is not buildable
        }

        // Check if the player has enough resources
        if(player.getCurrentGold() < userPreferences.getTowerConstructionCost()[towerType]) { // Example cost check
            return false; // Not enough gold
        }

        player.buyTower(userPreferences.getTowerConstructionCost()[towerType]); // Deduct cost from player's gold
        // Create the tower using the TowerFactory

        Tower tower = towerFactory.create(TowerType.values()[towerType]); // Create the tower using the factory
        // Set the tower's coordinates
        tower.setTileCoordinate(new TilePoint2D(xCoordinate, yCoordinate));
        // Add the tower to the list of towers
        towers.add(tower);

        int tileCode;

        if (towerType == 0) {
            tileCode = 20; // Example tile code for tower type 0
        } else if (towerType == 1) {
            tileCode = 21; // Example tile code for tower type 1
        } else if (towerType == 2) {
            tileCode = 26; // Example tile code for tower type 2
        } else {
            return false; // Invalid tower type
        }

        Tile towerTile = tileFactory.create(tileCode); // Create the tower tile using the factory
        gameMap.setTile(xCoordinate, yCoordinate, towerTile);

        return true;
    }
    
    public boolean sellTower(int xCoordinate, int yCoordinate, int towerType) {
        // Logic to sell a tower
        // Check if the tower exists at the given coordinates

        // look for a tower in the list of towers
        for (Tower tower : towers) {
            if (tower.getTileCoordinate().getTileX() == xCoordinate && tower.getTileCoordinate().getTileY() == yCoordinate) {
                // Tower found, sell it
                player.sellTower(tower.getSellReturn()); // Add sell return to player's gold
                towers.remove(tower); // Remove the tower from the list
                Tile buildableTile = this.tileFactory.create(15); // Create a buildable tile using the factory
                gameMap.setTile(xCoordinate, yCoordinate, buildableTile);
                return true; // Tower sold successfully
            }
        }
        return false; // Tower not found at the given coordinates
    }

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

    public ArrayList<Enemy> getEnemiesToRemove() {
        return enemiesToRemove; // Return the list of enemies to remove
    }
    public ArrayList<Projectile> getProjectilesToRemove() {
        return projectilesToRemove; // Return the list of projectiles to remove
    }

}
