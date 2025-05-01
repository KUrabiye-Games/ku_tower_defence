package com.kurabiye.kutd.controller;

import com.kurabiye.kutd.model.Managers.GameManager;
import com.kurabiye.kutd.model.Map.GameMap;
import com.kurabiye.kutd.view.GamePlayView;

import javafx.application.Platform;
import javafx.animation.AnimationTimer;

/**
 * GamePlayController.java
 * This class is responsible for handling user inputs and connecting the 
 * game model with the view components. It processes user interactions and
 * updates the UI accordingly based on the game state.
 * 
 */
public class GamePlayController extends Controller {
    private GamePlayView gamePlayView;
    private GameManager gameManager;
    private AnimationTimer uiUpdateTimer;
    
    /**
     * Constructor for GamePlayController
     * 
     * @param gamePlayView The UI view component
     */
    public GamePlayController(GamePlayView gamePlayView) {
        this.gamePlayView = gamePlayView;
        
        GameMap gameMap = GameMap.getDefaultMap();
        this.gameManager = new GameManager(gameMap); // Create GameManager with GameMap
        
        initialize();
        startUIUpdateTimer();
        gameManager.startGame(); // Start the game loop immediately
    }
    
    /**
     * Initialize the controller and set up event handlers
     */
    @Override
    protected void initialize() {
        // Set up event handlers from the view
        gamePlayView.setOnPlaceTower(this::onPlaceTower);
        gamePlayView.setOnSellTower(this::onSellTower);
        gamePlayView.setOnUpgradeTower(this::onUpgradeTower);
        gamePlayView.setOnSpeedUpGame(this::onSpeedUpGame); 
        gamePlayView.setOnSlowDownGame(this::onSlowDownGame);
        gamePlayView.setOnPauseGame(this::onPauseGame);
        gamePlayView.setOnResumeGame(this::onResumeGame);
        gamePlayView.setOnStartNextWave(this::onStartNextWave);
        gamePlayView.setOnQuitGame(this::onQuitGame);
    }
    
    /**
     * Handler for tower placement
     * 
     * @param x X coordinate on the grid
     * @param y Y coordinate on the grid
     * @param towerType The type of tower to place
     */
    private void onPlaceTower(int x, int y, String towerType) {
        int type;
        try {
            type = Integer.parseInt(towerType);
        } catch (NumberFormatException e) {
            gamePlayView.showError("Invalid tower type.");
            return;
        }
        
        boolean success = gameManager.buildTower(x, y, type);
        if (success) {
            gamePlayView.showTowerPlaced(x, y, towerType);
            updateResourcesDisplay(); // Update gold after purchase
        } else {
            gamePlayView.showError("Cannot place tower here.");
        }
    }
    
    /**
     * Handler for selling a tower
     * 
     * @param x X coordinate on the grid
     * @param y Y coordinate on the grid
     */
    private void onSellTower(int x, int y) {
        // Attempt to sell the tower at the specified location
        boolean success = gameManager.sellTower(x, y, 0); // You may want to get the type dynamically
        if (success) {
            gamePlayView.showTowerSold(x, y);
            updateResourcesDisplay(); // Update gold after selling
        } else {
            gamePlayView.showError("No tower to sell at this location.");
        }
    }
    
    /**
     * Handler for upgrading a tower
     * 
     * @param x X coordinate on the grid
     * @param y Y coordinate on the grid
     */
    private void onUpgradeTower(int x, int y) {
        // Upgrade functionality not implemented in GameManager yet
        // This can be expanded when the feature is added to GameManager
        gamePlayView.showError("Upgrade functionality not yet implemented.");
    }
    
    /**
     * Handler for speeding up the game
     */
    private void onSpeedUpGame() {
        gameManager.speedUpGame();
        gamePlayView.showGameSpeedChanged(gameManager.getGameTimer().getTimeCoefficient());
    }
    
    /**
     * Handler for slowing down the game
     */
    private void onSlowDownGame() {
        gameManager.slowDownGame();
        gamePlayView.showGameSpeedChanged(gameManager.getGameTimer().getTimeCoefficient());
    }
    
    /**
     * Handler for pausing the game
     */
    private void onPauseGame() {
        gameManager.pauseGame();
        gamePlayView.showGamePaused();
    }
    
    /**
     * Handler for resuming the game
     */
    private void onResumeGame() {
        gameManager.resumeGame();
        gamePlayView.showGameResumed();
    }
    
    /**
     * Handler for starting the next wave of enemies
     */
    private void onStartNextWave() {
        // This method would call a method in GameManager to start the next wave
        // For now, it just updates the UI
        gamePlayView.showNextWaveStarted();
    }
    
    /**
     * Handler for quitting the game
     */
    private void onQuitGame() {
        gameManager.endGame();
        gamePlayView.navigateToMainMenu();
    }
    
    /**
     * Start the UI update timer to refresh the game view periodically
     */
    private void startUIUpdateTimer() {
        uiUpdateTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Only update UI if game is running or paused
                if (gameManager.getGameState() != GameManager.GameState.GAME_OVER &&
                    gameManager.getGameState() != GameManager.GameState.INITIALIZING) {
                    updateUI();
                } else if (gameManager.getGameState() == GameManager.GameState.GAME_OVER) {
                    handleGameOver();
                    this.stop(); // Stop the timer when game is over
                }
            }
        };
        uiUpdateTimer.start();
    }
    
    /**
     * Update the UI to reflect current game state
     */
    private void updateUI() {
        // Update on JavaFX thread
        Platform.runLater(() -> {
            // Update player resources
            updateResourcesDisplay();
            
            // Update enemy positions and state
            updateEnemiesDisplay();
            
            // Update tower states
            updateTowersDisplay();
            
            // Update game timer display
            updateGameTimeDisplay();
        });
    }
    
    /**
     * Update player resources display
     */
    private void updateResourcesDisplay() {
        // Update player gold, lives, score in the UI
        if (gameManager.getPlayer() != null) {
            gamePlayView.updateGoldDisplay(gameManager.getPlayer().getCurrentGold());
            gamePlayView.updateLivesDisplay(gameManager.getPlayer().getCurrentLives());
            gamePlayView.updateScoreDisplay(gameManager.getPlayer().getCurrentScore());
        }
    }
    
    /**
     * Update enemy positions and states in the UI
     */
    private void updateEnemiesDisplay() {
        // Clear previous enemy displays
        gamePlayView.clearEnemies();
        
        // If enemies exist in the game manager, update their display
        if (gameManager.getEnemies() != null) {
            gameManager.getEnemies().forEach(enemy -> {
                gamePlayView.showEnemy(
                    enemy.getPosition().getX(), 
                    enemy.getPosition().getY(),
                    enemy.getType(),
                    enemy.getHealthPercentage()
                );
            });
        }
    }
    
    /**
     * Update tower states in the UI
     */
    private void updateTowersDisplay() {
        // Clear previous tower displays
        gamePlayView.clearTowerEffects();
        
        // If towers exist in the game manager, update their display
        if (gameManager.getTowers() != null) {
            gameManager.getTowers().forEach(tower -> {
                gamePlayView.updateTowerState(
                    tower.getPosition().getX(),
                    tower.getPosition().getY(),
                    tower.isAttacking(),
                    tower.getCurrentTarget()
                );
            });
        }
    }
    
    /**
     * Update game timer display
     */
    private void updateGameTimeDisplay() {
        if (gameManager.getGameTimer() != null) {
            gamePlayView.updateGameTime(gameManager.getGameTimer().getElapsedTime());
        }
    }
    
    /**
     * Handle game over scenario
     */
    private void handleGameOver() {
        Platform.runLater(() -> {
            if (gameManager.getPlayer() != null) {
                gamePlayView.showGameOverScreen(
                    gameManager.getPlayer().getCurrentScore(),
                    gameManager.getCurrentWave(),
                    gameManager.getTotalWaves()
                );
            } else {
                gamePlayView.showGameOverScreen(0, 0, 0);
            }
        });
    }
    
    /**
     * Get the current game state
     * 
     * @return Current game state
     */
    public GameManager.GameState getGameState() {
        return gameManager.getGameState();
    }
    
    /**
     * Clean up resources when controller is no longer needed
     */
    public void cleanup() {
        if (uiUpdateTimer != null) {
            uiUpdateTimer.stop();
        }
        
        if (gameManager.getGameState() != GameManager.GameState.GAME_OVER) {
            gameManager.endGame();
        }
    }
}