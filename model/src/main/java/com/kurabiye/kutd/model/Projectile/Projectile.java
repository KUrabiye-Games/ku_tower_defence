package com.kurabiye.kutd.model.Projectile;

import com.kurabiye.kutd.model.Coordinates.Point2D;
import com.kurabiye.kutd.model.Projectile.ProjectileMoveStrategy.IProjectileMoveStrategy;

public class Projectile  {

    public enum ProjectileType { // Enum for different projectile types
        ARROW(0), // Arrow projectile type
        MAGIC(1), // Magic projectile type
        ARTILLERY(2); // Artillery projectile type

        private final int value;

        ProjectileType(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }


    //private final static double EPSILON = 1; // Epsilon value for floating point comparison


    private ProjectileType projectileType; // Type of the projectile

    //private Point2D startCoordinate; // Starting coordinate of the projectile on the map

    private Point2D targetCoordinate; // Ending coordinate of the projectile on the map

    private float gravityFactor; // Gravity factor for the projectile's trajectory

    private Point2D coordinate; // Coordinate of the projectile on the map

    private Point2D speedVector;

    private float projectileAreaDamage = 1f; // Area damage of the projectile

    private float speed; // Speed of the projectile

    public enum ProjectileState { // Enum for projectile states
        MOVING, // Projectile is alive
        STOPPED // Projectile is dead
    }

    private ProjectileState projectileState = ProjectileState.MOVING; // Projectile's alive status


    public Projectile(ProjectileType projectileType, Point2D startCoordinate, Point2D targetCoordinate, IProjectileMoveStrategy moveStrategy, float projectileAreaDamage) {
        this.projectileType = projectileType;
        //this.startCoordinate = startCoordinate;
        this.targetCoordinate = targetCoordinate;
        this.gravityFactor = moveStrategy.getGravityFactor(); // Get the gravity factor from the move strategy
        this.speed = moveStrategy.getSpeed(); // Get the speed from the move strategy
        
        this.projectileAreaDamage = projectileAreaDamage; // Set the area damage of the projectile
        this.speedVector = moveStrategy.getSpeedVector(startCoordinate, targetCoordinate, gravityFactor).multiply(this.speed); // Calculate the speed vector using the provided move strategy and then multiply it by the speed of the projectile
   
   
    }


    public ProjectileType getProjectileType() {
        return projectileType;
    }

    public float getProjectileAreaDamage() {
        return projectileAreaDamage; // Get the area damage of the projectile
    }

    public synchronized void move(double deltaTime) {
        if (projectileState == ProjectileState.MOVING) {

            speedVector.add(0, gravityFactor * deltaTime); // Update the speed vector with the gravity factor and delta time

            // Check if the speed vector is zero
            if (speedVector.magnitude() == 0) {
                return;
            }

            // Update the projectile's coordinate based on the speed vector and delta time
            coordinate = coordinate.add(speedVector.multiply(deltaTime)); // Update the coordinate of the projectile based on the speed vector and delta time

            // Check if the projectile has reached its target coordinate
            if (coordinate.distance(targetCoordinate) < speedVector.magnitude() * deltaTime) {
                coordinate = targetCoordinate; // Set the coordinate to the target coordinate
                projectileState = ProjectileState.STOPPED; // Stop the projectile if it has reached the target
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

    





    





   

}
