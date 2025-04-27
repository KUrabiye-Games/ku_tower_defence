package com.kurabiye.kutd.model;

import com.kurabiye.kutd.model.*;


public class GameManager {

    // === Game State Enums ===
    public enum GameState {
        SETUP, RUNNING, PAUSED, GAME_OVER, VICTORY
    }

    // === Attributes ===
    private GameState gameState;

    private CustomMap map;
    private Player player;

    private WaveManager waveManager;
    private TowerManager towerManager;
    private List<Enemy> activeEnemies;
    private List<Projectile> activeProjectiles;

    private double gameTime; // total time since start

    // === Constructor ===
    public GameManager(CustomMap map, Player player) {
        this.map = map;
        this.player = player;
        this.waveManager = new WaveManager(map, this);
        this.towerManager = new TowerManager(map);
        this.activeEnemies = new ArrayList<>();
        this.activeProjectiles = new ArrayList<>();
        this.gameState = GameState.SETUP;
        this.gameTime = 0;
    }

    // === Main Game Loop Tick ===
    public void update(double deltaTime) {
        if (gameState != GameState.RUNNING) return;

        gameTime += deltaTime;

        waveManager.update(deltaTime);
        updateEnemies(deltaTime);
        updateTowers(deltaTime);
        updateProjectiles(deltaTime);

        checkVictoryConditions();
    }

    private void updateEnemies(double deltaTime) {
        List<Enemy> deadEnemies = new ArrayList<>();

        for (Enemy enemy : activeEnemies) {
            enemy.update(deltaTime);
            if (!enemy.isAlive()) {
                player.addGold(enemy.getGoldReward());
                deadEnemies.add(enemy);
            } else if (enemy.reachedGoal()) {
                player.reduceHealth(1);
                deadEnemies.add(enemy);
            }
        }

        activeEnemies.removeAll(deadEnemies);
    }

    private void updateTowers(double deltaTime) {
        towerManager.updateTowers(activeEnemies, deltaTime, activeProjectiles);
    }

    private void updateProjectiles(double deltaTime) {
        List<Projectile> expired = new ArrayList<>();
        for (Projectile proj : activeProjectiles) {
            proj.update(deltaTime);
            if (proj.hasHitTarget()) {
                proj.applyEffect();
                expired.add(proj);
            }
        }
        activeProjectiles.removeAll(expired);
    }

    // === Public API ===

    public void startGame() {
        gameState = GameState.RUNNING;
    }

    public void pauseGame() {
        gameState = GameState.PAUSED;
    }

    public void resumeGame() {
        gameState = GameState.RUNNING;
    }

    public void endGame() {
        gameState = GameState.GAME_OVER;
    }

    public boolean isGameOver() {
        return gameState == GameState.GAME_OVER;
    }

    public void spawnEnemy(Enemy enemy) {
        activeEnemies.add(enemy);
    }

    public boolean buildTower(String type, Point2D position) {
        return towerManager.buildTower(type, position, player);
    }

    public boolean upgradeTower(Point2D position) {
        return towerManager.upgradeTower(position, player);
    }

    public boolean sellTower(Point2D position) {
        return towerManager.sellTower(position, player);
    }

    public Player getPlayer() {
        return player;
    }

    public CustomMap getMap() {
        return map;
    }

    public GameState getGameState() {
        return gameState;
    }

    // === Win/Loss Detection ===
    private void checkVictoryConditions() {
        if (player.getHealth() <= 0) {
            gameState = GameState.GAME_OVER;
        } else if (waveManager.isFinished() && activeEnemies.isEmpty()) {
            gameState = GameState.VICTORY;
        }
    }
}


