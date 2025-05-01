package com.kurabiye.kutd.model.Managers;

import java.util.ArrayList;

import com.kurabiye.kutd.model.Enemy.Enemy;
import com.kurabiye.kutd.model.Enemy.EnemyFactory;
import com.kurabiye.kutd.model.Map.GameMap;
import com.kurabiye.kutd.model.Player.Player;
import com.kurabiye.kutd.model.Player.UserPreference;
import com.kurabiye.kutd.model.Tile.Tile;
import com.kurabiye.kutd.model.Timer.GameTimer;
import com.kurabiye.kutd.model.Tower.Tower;
import com.kurabiye.kutd.model.Tower.TowerFactory;

import javafx.geometry.Point2D;

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

    public enum GameState {
        INITIALIZING,
        RUNNING,
        PAUSED,
        GAME_OVER
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
    


    public GameManager(GameMap gameMap) {
        this.gameState = GameState.INITIALIZING; // Initialize game state to RUNNING
        this.gameTimer = GameTimer.getInstance(); // Get the singleton instance of GameTimer
        this.gameTimer.setTimeCoefficient(1); // Set the time coefficient to 1 (normal speed)
        this.gameMap = gameMap; // Initialize the game map
        this.userPreferences = UserPreference.getInstance(); // Initialize user preferences
        this.player = new Player(userPreferences); // Initialize the player object

        path = (ArrayList<Point2D>) gameMap.getPointPath(); // Get the path from the game map
    }


    /* Do not use this method directly.
     * 
     * 
     */

    @Override
    public void run() {
        // Game loop
        while (gameState != GameState.GAME_OVER) {
            // Update game state

            // Calculate the delta time
            gameTimer.update(); // Update the game timer
            long deltaTime = gameTimer.getDeltaTime(); // Get the delta time from the game timer

            // Update game logic based on the delta time

            // Update enemies





             
           
            // Sleep for a short duration to control the frame rate
            try {
                Thread.sleep(16); // Approximately 60 FPS
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
        gameState = GameState.GAME_OVER;
        // Handle game over logic
    }

    public GameState getGameState() {
        return gameState;
    }

    public void speedUpGame() {
        // Increase game speed
        long currentTimeCoefficient = gameTimer.getTimeCoefficient();
        if (currentTimeCoefficient < 4) { // Limit the maximum speed to 4x
            gameTimer.setTimeCoefficient(currentTimeCoefficient * 2);
        }
       
    }
    public void slowDownGame() {
        // Decrease game speed
        long currentTimeCoefficient = gameTimer.getTimeCoefficient();
        if (currentTimeCoefficient > 0.25) { // Limit the minimum speed to 0.25x
            gameTimer.setTimeCoefficient(currentTimeCoefficient / 2);
        }
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

        switch (towerType) {
            case 0:
                // Create an archer tower
                break;
            case 1:
                
                break;
            case 2:
                // Create a mage tower
                break;
        
            default:
                break;
        }



        return true;
    }
    public boolean sellTower(int xCoordinate, int yCoordinate, int towerType) {
        // Logic to sell a tower
        return true; // Return true if successful
    }


    public void startGame() {

        // Create a new thread using this instance (which implements Runnable)
        Thread gameThread = new Thread(this);
        // Start the thread, which will call the run() method
        gameThread.start();

    }


 

}
