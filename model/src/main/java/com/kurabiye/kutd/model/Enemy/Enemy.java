package com.kurabiye.kutd.model.Enemy;

import com.kurabiye.kutd.model.Coordinates.Coordinate;

/*
 * This class represents an enemy in the game.
 * It contains attributes such as health, speed, and kill reward.
 * It also provides methods to damage the enemy and check if it is alive.
 * 
 * 
 * @author: Atlas Berk Polat
 * @version: 1.0
 * @since: 2025-04-23
 */

public abstract class Enemy {

    public enum EnemyType { // Enum for different enemy types
        GOBLIN, // Goblin enemy type
        KNIGHT // Knight enemy type
    }

    protected EnemyType enemyType; // Type of the enemy

    protected Coordinate coordinate; // Coordinate of the enemy on the map

    protected int killReward; // Default health for enemies

    protected int health; // Enemy's health

    protected int speed; // Enemy's speed
    
    protected boolean isAlive; // Enemy's alive status

    public void damage(int damage) {
        this.health -= damage; // Reduce health by damage amount
        if (this.health <= 0) {
            this.isAlive = false; // Set alive status to false if health is 0 or less
        }
    }
    public int getKillReward() {
        return killReward; // Get the kill reward for the enemy
    }

    public abstract void move(); // Abstract method for moving the enemy


    public boolean isAlive() {
        return isAlive; // Check if the enemy is alive
    }
    public int getHealth() {
        return health; // Get the health of the enemy
    }
    public int getSpeed() {
        return speed; // Get the speed of the enemy
    }
    public Coordinate getCoordinate() {
        return coordinate; // Get the coordinate of the enemy
    }


    

}
