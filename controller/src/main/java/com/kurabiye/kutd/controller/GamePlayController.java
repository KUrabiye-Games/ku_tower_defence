package com.kurabiye.kutd.controller;

import com.kurabiye.kutd.model.Managers.GameManager;
import com.kurabiye.kutd.model.Map.GameMap;

public class GamePlayController {

    private final GameManager gameManager;

    public GamePlayController(GameManager gameManager) {
        this.gameManager = gameManager;
    }
    public void startGame() {
        gameManager.startGame(); // Starts the game thread and logic loop
    }

    public void pauseGame() {
        gameManager.pauseGame();
    }

    public void resumeGame() {
        gameManager.resumeGame();
    }

    public void endGame() {
        gameManager.endGame();
    }

    public void speedUpGame() {
        gameManager.speedUpGame();
    }

    public void slowDownGame() {
        gameManager.slowDownGame();
    }

    public boolean buildTower(int x, int y, int towerType) {
        return gameManager.buildTower(x, y, towerType);
    }

    public boolean sellTower(int x, int y, int towerType) {
        return gameManager.sellTower(x, y, towerType);
    }

}
