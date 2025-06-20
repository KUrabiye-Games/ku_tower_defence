package com.kurabiye.kutd.model.Enemy;

import java.util.ArrayList;

import com.kurabiye.kutd.model.Coordinates.Point2D;
import com.kurabiye.kutd.model.Enemy.MoveStrategy.IMoveStrategy;
import com.kurabiye.kutd.model.Player.UserPreference;
import com.kurabiye.kutd.model.Projectile.IProjectile;
import com.kurabiye.kutd.model.Projectile.ProjectileType;


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

public class Enemy implements IEnemy {


    UserPreference userPreferences = UserPreference.getInstance(); // User preferences for enemy attributes

    
    private EnemyType enemyType; // Type of the enemy

    private Point2D coordinate = new Point2D(0,0); // Coordinate of the enemy on the map

    private int killReward; // Default health for enemies

    private float health; // Enemy's health

    private int speed; // Enemy's speed

    // This is a normalized vector
    // It is used to calculate the direction of the enemy's movement 
    private Point2D moveDirection = new Point2D(1, 0); // Direction of the enemy's movement
    
    

    private EnemyState enemyState = EnemyState.ALIVE; // Enemy's alive status

    //  Keep track of where the enemy is on the path
    private int pathPointIndex = 0; // Index of the current path point



    // For the moment it will be only the center of the tiles
    private ArrayList<Point2D> movePath = new ArrayList<Point2D>(); // Path of the enemy


    /*
     * For the moment we do not implement any move strategy.
     * The enemies will move from one center of the tile to another center of the tile.
     * The move sttategy will use math and geometry to create more complex paths later.
     * 
     */
    
     public void setMovePathWithStrategy(ArrayList<Point2D> path, IMoveStrategy moveStrategy) {
        
         this.movePath = moveStrategy.createMovePath(path); // Create the move path using the strategy
     }

    public Enemy(EnemyType enemyType) {
        this.enemyType = enemyType; // Set the type of the enemy
        this.health = userPreferences.getEnemyHealth()[enemyType.getValue()]; // Set the health of the enemy based on user preferences
        this.speed =  userPreferences.getEnemyMovementSpeed()[enemyType.getValue()]; // Set the speed of the enemy
        this.killReward = userPreferences.getGoldPerEnemy()[enemyType.getValue()]; // Set the kill reward for the enemy
    }

    public synchronized void getDamage(IProjectile projectile) {
        //System.out.println("Enemy hit! Health before: " + health);


        float damage = userPreferences.getDamageDealt()[projectile.getProjectileType().getValue()][enemyType.getValue()][projectile.getProjectileLevel()]; // Get the damage dealt by the projectile
        health -= damage; // Reduce the health of the enemy by the damage dealt

        if(health <= 0) {
            enemyState = EnemyState.DEAD; // Set the enemy
            //System.out.println("Enemy died at " + coordinate);
            return; // If the enemy's health is less than or equal to 0, set the enemy state to DEAD
        }

        
 
        
}

    public int getKillReward() {
        if(enemyState == EnemyState.DEAD) {
            return killReward; // If the enemy is dead
        } else {
            return 0; // If the enemy is alive
        }
    }



    public synchronized void move(double deltaTime) {

        int targetSpeed = this.getSpeed(); // Get the base speed of the enemy

        
        if(enemyState == EnemyState.DEAD || enemyState == EnemyState.ARRIVED) {
            
            return; // If the enemy is not alive, do not move
        }

        if (enemyState == EnemyState.TELEPORTED) {
            enemyState = EnemyState.ALIVE; // If the enemy is teleported, set the enemy state to ALIVE
            return; // Do not move the enemy if it is teleported
            
        }


        Point2D nextPoint = movePath.get(pathPointIndex); // Get the next point on the path

        if(this.coordinate.distance(nextPoint) < targetSpeed * deltaTime) {
            pathPointIndex++; // Increment the path point index

            // Check if pathPointIndex is within the range of movePath
            
            if (pathPointIndex >= movePath.size()) {
            // Enemy has reached the end of the path
            enemyState = EnemyState.ARRIVED;
            return;
        }
            nextPoint = movePath.get(pathPointIndex); // Get the next point on the path
        }

        Point2D distanceVector = nextPoint.subtract(coordinate); // Calculate the distance vector to the next point
        // Normalize the distance vector
        double distance = distanceVector.magnitude(); // Calculate the magnitude of the distance vector
        if(distance > 0) {
            distanceVector = distanceVector.multiply(targetSpeed * deltaTime / distance); // Scale the distance vector by speed and delta time
        }
        coordinate = coordinate.add(distanceVector); // Update the coordinate of the enemy


        // Set the move direction of the enemy

        moveDirection = distanceVector.normalize(); // Set the move direction to the distance vector

    }


    public synchronized boolean isAlive() {
        return (enemyState == EnemyState.ALIVE); // Check if the enemy is alive
    }

    public synchronized boolean isDead() {
        return (enemyState == EnemyState.DEAD); // Check if the enemy is dead
    }

    public synchronized boolean hasArrived() {
        return (enemyState == EnemyState.ARRIVED); // Check if the enemy has arrived at the destination
    }

    public synchronized float getHealth() {
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

    /*
     * This method is used to get the type of the enemy.
     * It is used to identify the enemy type in the game.
     * 
     * 
     */
    @Override
    public synchronized void locateToStartPoint(){
        pathPointIndex = 0; // Reset the path point index to 0
            coordinate = movePath.get(0); // Set the coordinate of the enemy to the first point in the path
                this.enemyState = EnemyState.TELEPORTED; // Set the enemy state to ALIVE

    }

    public EnemyType getEnemyType() {
        return enemyType; // Get the type of the enemy
    }

    public synchronized Point2D getMoveDirection() {
        return moveDirection; // Get the move direction of the enemy
    }

    public synchronized void setSpeed(int speed) {
        this.speed = speed; // Set the speed of the enemy
    }

    private boolean deathAnimationPlayed = false;

    public boolean isDeathAnimationPlayed() {
        return deathAnimationPlayed;
    }

    public void setDeathAnimationPlayed(boolean deathAnimationPlayed) {
        this.deathAnimationPlayed = deathAnimationPlayed;
    }





}
