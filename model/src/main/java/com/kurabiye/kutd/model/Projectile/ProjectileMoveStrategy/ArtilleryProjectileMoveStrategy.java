package com.kurabiye.kutd.model.Projectile.ProjectileMoveStrategy;

import com.kurabiye.kutd.model.Coordinates.Point2D;

public class ArtilleryProjectileMoveStrategy implements IProjectileMoveStrategy {

    private static final float GRAVITY = 0; // Gravity constant
    private float ARTILLERY_SPEED = 50; // Speed of the artillery projectile
    
/*
    @Override
    public Point2D[] getSpeedVector(Point2D startingPoint, Point2D targetPoint, float gravity) {

        //return new Point2D[]{new Point2D(0, 1), new Point2D(10f, 9)}; // Return a zero vector if the length is zero

        // Use the class constant instead of the parameter
        gravity = GRAVITY;

        // Calculate the distance between the starting point and target point
        double dx = targetPoint.getX() - startingPoint.getX(); // Horizontal distance
        double dy = (startingPoint.getY() - targetPoint.getY()); // Vertical distance (positive when target is below)
        
      // Angle = 45 degrees

        double angle = Math.atan(2); // 45 degrees in radians


       


        double velocityTimesTime = dx/Math.cos(angle); // Horizontal component of velocity times time

        double timeSquared = (velocityTimesTime * Math.sin(angle) - dy) / gravity; // Time squared

        if (dy > 0) { // If the target is above the starting point
            timeSquared = (velocityTimesTime * Math.sin(angle) + dy) / gravity; // Time squared
        }


        

        double timeOfFlight = 2 * Math.sqrt(Math.abs(timeSquared)); // Time of flight

        double initialVelocity = Math.abs(dx/( timeOfFlight * Math.cos(angle))); // Initial velocity

        ARTILLERY_SPEED = (float) initialVelocity; // Set the speed of the artillery projectile


        // Turn to right or left

        double turnCofficient = 1;

        if (dx < 0) { // If the target is to the left of the starting point
            turnCofficient = -1; // Set the turn coefficient to -1
        } 

        double initialSpeedX = turnCofficient * initialVelocity * Math.cos(angle); // Initial speed in x direction
        double initialSpeedY = -1 * initialVelocity * Math.sin(angle); // Initial speed in y direction

        // Create the speed vector
        Point2D initialSpeedVector = new Point2D(initialSpeedX, initialSpeedY); // Speed vector
        // Calculate the activation time

        // Normalize the speed vector
        double length = initialSpeedVector.magnitude(); // Get the length of the speed vector

        if (length > 0) {
            initialSpeedVector = initialSpeedVector.normalize(); // Normalize the speed vector to get the direction
        } else {
            return new Point2D[]{new Point2D(0, 0), new Point2D(0.1f, 0)}; // If the length is zero, return a zero vector
        }

        // Calculate the activation time

        double activationTime = timeOfFlight - 0.1; // Activation time (0.1 seconds before the projectile reaches the target)

        if (activationTime < 0) {
            activationTime = 0; // Ensure activation time is not negative
        }


        
        return new Point2D[]{initialSpeedVector, new Point2D(timeOfFlight + 0.05f, activationTime)};
    }*/

    public static final double PROJECTILE_LIFE_TIME = 5f; // Life time of the projectile

    @Override
    public Point2D[] getSpeedVector(Point2D startingPoint, Point2D targetPoint, float gravity) {
        
        Point2D speedVector = targetPoint.subtract(startingPoint); // Calculate the speed vector from the starting point to the target point
        
        
        double length = speedVector.magnitude(); // Get the length of the speed vector

        if (length > 0) {
            speedVector = speedVector.normalize(); // Normalize the speed vector to get the direction
        } else {
            return new Point2D[]{new Point2D(0, 0), new Point2D(0, 0.1)}; // If the length is zero, return a zero vector
        }

        return new Point2D[]{speedVector, new Point2D(PROJECTILE_LIFE_TIME, 0)}; // Return the normalized speed vector
    }


    @Override
    public float getGravityFactor() {
        return GRAVITY;
    }

    @Override
    public float getSpeed() {
        return ARTILLERY_SPEED;
    }
}
