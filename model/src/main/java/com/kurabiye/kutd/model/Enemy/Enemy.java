package com.kurabiye.kutd.model.Enemy;

import javafx.geometry.Point2D;

import java.util.ArrayList;

import com.kurabiye.kutd.model.Enemy.MoveStrategy.IMoveStrategy;

import com.kurabiye.kutd.util.ObserverPattern.*;;

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

public class Enemy implements Observable{

    public enum EnemyType { // Enum for different enemy types
        GOBLIN, // Goblin enemy type
        KNIGHT // Knight enemy type
    }

    protected EnemyType enemyType; // Type of the enemy

    protected Point2D coordinate = new Point2D(0,0); // Coordinate of the enemy on the map

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

    public synchronized void damage(int damage) {
        this.health -= damage; // Reduce health by damage amount
        if (this.health <= 0) {
            this.isAlive = false; // Set alive status to false if health is 0 or less
        }
    }
    public int getKillReward() {
        return killReward; // Get the kill reward for the enemy
    }

    public synchronized void move(Point2D target){


    }


    public synchronized boolean isAlive() {
        return isAlive; // Check if the enemy is alive
    }

    public synchronized boolean isDead() {
        return !isAlive; // Check if the enemy is dead
    }
    public synchronized int getHealth() {
        return health; // Get the health of the enemy
    }
    public int getSpeed() {
        return speed; // Get the speed of the enemy
    }
    public synchronized Point2D getCoordinate() {
        return coordinate; // Get the coordinate of the enemy
    }

    /*
     * 
     * This method is used to set the coordinate of the enemy to a new coordinate.
     * It is used when the enemy is spawned or when it is moved to a new location.
     * 
     * @param coordinate The new coordinate of the enemy.
     */
    public synchronized void locate(Point2D newCoordinate) {
        this.coordinate = newCoordinate; // Set the coordinate of the enemy to the new point
    }




    // Obserbable interface methods
    // Boilerplate code for the Observable interface


    ArrayList<Observer> observers = new ArrayList<>(); // List of observers

    @Override
    public void addObserver(Observer observer) {
        if (observer == null) {
            throw new NullPointerException("Null Observer");
        }
        if (!observers.contains(observer)) {
            observers.add(observer); // Add the observer to the list
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        
        if (observer == null) {
            throw new NullPointerException("Null Observer");
        }
        if (observers.contains(observer)) {
            observers.remove(observer); // Remove the observer from the list
        }
    }

    @Override
    public void notifyObservers(Object arg) {
        for (Observer observer : observers) {
            observer.update(arg); // Notify each observer with the argument
        }
    }

    


    

}
