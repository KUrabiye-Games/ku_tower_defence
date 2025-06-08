package com.kurabiye.kutd.model.Projectile;

import com.kurabiye.kutd.model.Coordinates.Point2D;
import com.kurabiye.kutd.model.Projectile.ProjectileMoveStrategy.IProjectileMoveStrategy;

/* Projectile.java
 * This class represents a projectile in the game.
 * It handles the projectile's movement, state, and damage.
 * 
 * @author Atlas Berk Polat
 * @version 2
 * @since 2025-05-13
 * 
 */

public class Projectile implements IProjectile {




    //private final static double EPSILON = 1; // Epsilon value for floating point comparison


    private ProjectileType projectileType; // Type of the projectile

    // Todo: Add projecctile level

    //private Point2D startCoordinate; // Starting coordinate of the projectile on the map

    private Point2D targetCoordinate; // Ending coordinate of the projectile on the map

    private float gravityFactor; // Gravity factor for the projectile's trajectory

    private Point2D coordinate; // Coordinate of the projectile on the map

    private Point2D speedVector;

    private float projectileAreaDamage = 30f; // Area damage of the projectile

    private float speed; // Speed of the projectile


    private double projectileLifeTime = 3f; // Life time of the projectile

    private double projectileLifeTimeCounter = 0; // Counter for the projectile's life time

    private double projectileExplosiveActtionTime = 0.1f; // Time to explode after reaching the target


    private ProjectileState projectileState = ProjectileState.MOVING; // Projectile's alive status


    private DamageType damageType = DamageType.TARGET; // Type of explosion


    // Phase 2:

    private int projectileLevel = 1; // Level of the projectile




    public Projectile(ProjectileType projectileType, Point2D startCoordinate, Point2D targetCoordinate, IProjectileMoveStrategy moveStrategy, float projectileAreaDamage, DamageType damageType, int projectileLevel) {
        this.damageType = damageType; // Set the explosion type
        this.projectileType = projectileType;
        //this.startCoordinate = startCoordinate;
        this.targetCoordinate = targetCoordinate;
        this.gravityFactor = moveStrategy.getGravityFactor(); // Get the gravity factor from the move strategy
        this.speed = moveStrategy.getSpeed(); // Get the speed from the move strategy
        
        this.projectileAreaDamage = projectileAreaDamage; // Set the area damage of the projectile

        Point2D[] dataArray = moveStrategy.getSpeedVector(startCoordinate, targetCoordinate, gravityFactor); // Get the speed vector and life time from the move strategy
        this.speedVector = dataArray[0].multiply(this.speed); // Calculate the speed vector using the provided move strategy and then multiply it by the speed of the projectile



        this.projectileLifeTime = dataArray[1].getX(); // Get the life time of the projectile from the move strategy

        this.projectileExplosiveActtionTime = dataArray[1].getY(); // Get the explosive action time of the projectile from the move strategy


        this.coordinate = startCoordinate; // Set the starting coordinate of the projectile
   
        this.projectileLevel = projectileLevel; // Set the level of the projectile
    }

    
    public DamageType getDamageType() {
        return damageType; // Get the explosion type
    }

    public Point2D getTarget(){
        return targetCoordinate; // Get the target coordinate of the projectile
    }
  


    public ProjectileType getProjectileType() {
        return projectileType;
    }

    public float getProjectileAreaDamage() {
        return projectileAreaDamage; // Get the area damage of the projectile
    }


    //private static final double EPSILON = 1e-1; // Epsilon value for floating point comparison

    private double expirationTime = 0;

    public synchronized void move(double deltaTime) {
        if (projectileState == ProjectileState.MOVING || projectileState == ProjectileState.ACTIVE) {

            speedVector = speedVector.add(0, 1 * gravityFactor * deltaTime); // Update the speed vector with the gravity factor and delta time

            // Check if the speed vector is zero
            if (speedVector.magnitude() == 0) {
                return;
            }

            // Update the projectile's coordinate based on the speed vector and delta time
            coordinate = coordinate.add(speedVector.multiply(deltaTime)); // Update the coordinate of the projectile based on the speed vector and delta time

            // Check if the projectile has reached its target coordinate
            if (coordinate.distance(targetCoordinate) < deltaTime * speed) {
                coordinate = targetCoordinate; // Set the coordinate to the target coordinate
                projectileState = ProjectileState.STOPPED; // Stop the projectile if it has reached the target
            }

            // Check if the projectile has reached its explosive action time

            if (projectileLifeTimeCounter > projectileExplosiveActtionTime) {
                projectileState = ProjectileState.ACTIVE; // Set the projectile state to ACTIVE if it has reached its explosive action time
            }


            // Check if the projectile has exceeded its life time
            if (projectileLifeTimeCounter > projectileLifeTime) {
                projectileState = ProjectileState.STOPPED; // Set the projectile state to DEAD if it has exceeded its life time
            } else {
                projectileLifeTimeCounter += deltaTime; // Increment the life time counter
            }


            
           
        
        }else if (projectileState == ProjectileState.STOPPED) {
            expirationTime += deltaTime; // Update the expiration time of the projectile
            if (expirationTime > 5f) {
                projectileState = ProjectileState.DEAD; // Set the projectile state to DEAD after a certain time
            }
        }
    }

    public Point2D getCoordinate() {
        return coordinate; // Get the current coordinate of the projectile
    }

    // Get the projectile's speed vector
    public Point2D getSpeedVector() {
        return speedVector; // Get the speed vector of the projectile
    }

    public ProjectileState getProjectileState() {
        return projectileState; // Get the projectile's alive status
    }


    public int getProjectileLevel() {
        return projectileLevel; // Get the projectile's level
    }

}
