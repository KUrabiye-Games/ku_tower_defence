package com.kurabiye.kutd.model.Projectile.ProjectileMoveStrategy;

import com.kurabiye.kutd.model.Coordinates.Point2D;

public class ArrowProjectileMoveStrategy implements IProjectileMoveStrategy {


    private static final double PROJECTILE_LIFE_TIME = 1f; // Life time of the projectile


    private static final float PROJECTILE_SPEED = 800.0f; // Speed of the projectile

    @Override
    public Point2D[] getSpeedVector(Point2D startingPoint, Point2D targetPoint, float gravity) {
        
        Point2D speedVector = targetPoint.subtract(startingPoint); // Calculate the speed vector from the starting point to the target point
        
        
        double length = speedVector.magnitude(); // Get the length of the speed vector

        if (length > 0) {
            speedVector = speedVector.normalize(); // Normalize the speed vector to get the direction
        } else {
            return new Point2D[]{new Point2D(0, 0), new Point2D(PROJECTILE_LIFE_TIME, 0.1)}; // If the length is zero, return a zero vector
        }

        return new Point2D[]{speedVector, new Point2D(length/PROJECTILE_SPEED + 3, 0)}; // Return the normalized speed vector
    }

    @Override
    public float getGravityFactor() {
        return 0; // Straight line movement does not consider gravity, so return 0
    }

    @Override
    public float getSpeed() {
        return PROJECTILE_SPEED;
    }




  


}
