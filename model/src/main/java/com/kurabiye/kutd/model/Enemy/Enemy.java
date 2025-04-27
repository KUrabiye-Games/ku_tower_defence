package com.kurabiye.kutd.model.Enemy;

import com.kurabiye.kutd.model.Coordinates.Coordinate;
import com.kurabiye.kutd.model.Enemy.MoveStrategy.IMoveStrategy;

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

    protected Coordinate coordinate = new Coordinate(0,0); // Coordinate of the enemy on the map

    protected int killReward; // Default health for enemies

    protected int health; // Enemy's health

    protected int speed; // Enemy's speed
    
    protected boolean isAlive; // Enemy's alive status

    protected IMoveStrategy moveStrategy; // Move strategy for the enemy


    public Enemy(EnemyType enemyType, int health, int speed, int killReward) {
        this.enemyType = enemyType; // Set the type of the enemy
        this.health = health; // Set the health of the enemy
        this.speed = speed; // Set the speed of the enemy
        this.killReward = killReward; // Set the kill reward for the enemy
        this.isAlive = true; // Set alive status to true by default
    }

    public void damage(int damage) {
        this.health -= damage; // Reduce health by damage amount
        if (this.health <= 0) {
            this.isAlive = false; // Set alive status to false if health is 0 or less
        }
    }
    public int getKillReward() {
        return killReward; // Get the kill reward for the enemy
    }

    public abstract void move(Coordinate target); // Abstract method for moving the enemy


    public boolean isAlive() {
        return isAlive; // Check if the enemy is alive
    }

    public boolean isDead() {
        return !isAlive; // Check if the enemy is dead
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

    /*
     * 
     * This method is used to set the coordinate of the enemy to a new coordinate.
     * It is used when the enemy is spawned or when it is moved to a new location.
     * 
     * @param coordinate The new coordinate of the enemy.
     */
    public void locate(Coordinate coordinate) {
        this.coordinate.setX(coordinate.getX());// Set the coordinate of the enemy to the starting point
    }

    


    

}
