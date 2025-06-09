package com.kurabiye.kutd.controller;

import com.kurabiye.kutd.model.Listeners.IGameUpdateListener;
import com.kurabiye.kutd.model.Managers.GameManager;
import com.kurabiye.kutd.util.ObserverPattern.Observer;
import com.kurabiye.kutd.model.Map.GameMap;
import com.kurabiye.kutd.model.Map.StaticMap;

public class GamePlayController {

    private final GameManager gameManager;

    public GameManager getGameManager() {
        return gameManager;
    }

    public GamePlayController(GameMap gameMap) {
        if (gameMap == null) {
            gameMap = StaticMap.getPrebuiltMap(); // Fallback to default map
        }
        gameManager = new GameManager(gameMap);
       
    }

    public void setGameUpdateListener(IGameUpdateListener listener) {

        this.gameManager.setGameUpdateListener(listener);
    }

    public void setPlayerObserver(Observer observer){
        this.gameManager.getPlayer().addObserver(observer);
    }

    public void setGameMapObserver(Observer observer){
        this.gameManager.getGameMap().addObserver(observer);
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
        terminateGameThread(); // Terminates the game thread
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

    private void terminateGameThread() {
        gameManager.killGameThread();
    }

}
