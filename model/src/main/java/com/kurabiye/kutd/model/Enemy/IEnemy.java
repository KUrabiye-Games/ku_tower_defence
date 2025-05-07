package com.kurabiye.kutd.model.Enemy;

import java.util.ArrayList;

import com.kurabiye.kutd.model.Coordinates.Point2D;
import com.kurabiye.kutd.model.Enemy.Enemy.EnemyType;
import com.kurabiye.kutd.model.Enemy.MoveStrategy.IMoveStrategy;
import com.kurabiye.kutd.model.Projectile.Projectile.ProjectileType;

/**
 * Interface defining the core functionality of an Enemy in the tower defense game.
 * Enemies move along a path towards a destination and can be damaged by towers.
 * 
 * @author Atlas Berk Polat
 * @version 1.5
 * @since 2025-04-20
 */
public interface IEnemy {
    
    /**
     * Sets the path for the enemy to follow using a movement strategy
     * 
     * @param path The path points to follow
     * @param moveStrategy The strategy that controls how the enemy moves
     */
    void setMovePathWithStrategy(ArrayList<Point2D> path, IMoveStrategy moveStrategy);
    
    /**
     * Applies damage to the enemy when hit by a projectile
     * 
     * @param projectileType The type of projectile that hit the enemy
     */
    void getDamage(ProjectileType projectileType);
    
    /**
     * Gets the gold reward when the enemy is killed
     * 
     * @return The amount of gold awarded
     */
    int getKillReward();
    
    /**
     * Updates the enemy's position based on deltaTime
     * 
     * @param deltaTime Time passed since last update in milliseconds
     */
    void move(double deltaTime);
    
    /**
     * Checks if the enemy is alive
     * 
     * @return true if the enemy is alive, false otherwise
     */
    boolean isAlive();
    
    /**
     * Checks if the enemy is dead
     * 
     * @return true if the enemy is dead, false otherwise
     */
    boolean isDead();
    
    /**
     * Checks if the enemy has arrived at the destination
     * 
     * @return true if the enemy has arrived, false otherwise
     */
    boolean hasArrived();
    
    /**
     * Gets the current health of the enemy
     * 
     * @return The enemy's health
     */
    float getHealth();
    
    /**
     * Gets the speed of the enemy
     * 
     * @return The enemy's speed
     */
    int getSpeed();
    
    /**
     * Gets the current position of the enemy
     * 
     * @return The enemy's coordinate
     */
    Point2D getCoordinate();
    
    /**
     * Sets the position of the enemy
     * 
     * @param newCoordinate The new coordinate to place the enemy
     */
    void locate(Point2D newCoordinate);
    
    /**
     * Gets the type of the enemy
     * 
     * @return The enemy type
     */
    EnemyType getEnemyType();
}