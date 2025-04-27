package com.kurabiye.kutd.controller;

import com.kurabiye.kutd.view.GamePlayView;
import com.kurabiye.kutd.model.GameManager;
import com.kurabiye.kutd.model.Item;

public class GamePlayController extends Controller{
    private GamePlayView gamePlayView;
    private GameManager gameManager;


    public GamePlayController(GamePlayView gamePlayView, GameManager gameManager) {
        this.gamePlayView = gamePlayView;
        this.gameManager = gameManager;

        initialize();
    }

    @Override
    protected void initialize() {
        gamePlayView.setOnPlaceTower(this::onPlaceTower);
        gamePlayView.setOnSellTower(this::onSellTower);
        gamePlayView.setOnUpgradeTower(this::onUpgradeTower);
        gamePlayView.setOnCollectItem(this::onCollectItem);
        gamePlayView.setOnChangeGameSpeed(this::onChangeGameSpeed);
        gamePlayView.setOnPauseGame(this::onPauseGame);
        gamePlayView.setOnResumeGame(this::onResumeGame);
        gamePlayView.setOnStartNextWave(this::onStartNextWave);
    }

    private void onPlaceTower(int x, int y, String towerType) {
        boolean success = gameManager.placeTower(x, y, towerType);
        if (success) {
            gamePlayView.showTowerPlaced(x, y, towerType);
        } else {
            gamePlayView.showError("Cannot place tower here.");
        }
    }

    private void onSellTower(int x, int y) {
        boolean success = gameManager.sellTower(x, y);
        if (success) {
            gamePlayView.showTowerSold(x, y);
        } else {
            gamePlayView.showError("No tower to sell at this location.");
        }
    }

    private void onUpgradeTower(int x, int y) {
        boolean success = gameManager.upgradeTower(x, y);
        if (success) {
            gamePlayView.showTowerUpgraded(x, y);
        } else {
            gamePlayView.showError("Upgrade failed. Insufficient resources or max level reached.");
        }
    }

    private void onChangeGameSpeed(double speedMultiplier) {
        gameManager.setGameSpeed(speedMultiplier);
        gamePlayView.showGameSpeedChanged(speedMultiplier);
    }

    private void onPauseGame() {
        gameManager.pauseGame();
        gamePlayView.showGamePaused();
    }

    private void onResumeGame() {
        gameManager.resumeGame();
        gamePlayView.showGameResumed();
    }

    private void onStartNextWave() {
        gameManager.startNextWave();
        gamePlayView.showNextWaveStarted();
    }
}
