package com.kurabiye.kutd.model.Projectile.ProjectileMoveStrategy;

import com.kurabiye.kutd.model.Coordinates.Point2D;

public class MagicProjectileMoveStrategy implements IProjectileMoveStrategy {

    public static final float speed = 600.0f; // Speed of the projectile


    private static final double PROJECTILE_LIFE_TIME = 5f; // Life time of the projectile


/* 
    @Override
    public Point2D[] getSpeedVector(Point2D startingPoint, Point2D targetPoint, float gravity) {
        
        Point2D speedVector = targetPoint.subtract(startingPoint); // Calculate the speed vector from the starting point to the target point
        
        double length = speedVector.magnitude(); // Get the length of the speed vector

        if (length > 0) {
            speedVector = speedVector.normalize(); // Normalize the speed vector to get the direction
        } else {
            return new Point2D[]{new Point2D(0, 0), new Point2D(PROJECTILE_LIFE_TIME, 0.1)}; // If the length is zero, return a zero vector
        }

        return new Point2D[]{speedVector, new Point2D(length, gravity)}; // Return the normalized speed vector
    }
*/


    @Override
    public Point2D[] getSpeedVector(Point2D startingPoint, Point2D targetPoint, float gravity) {
        // Calculate the speed vector from the starting point to the target point
        Point2D speedVector = targetPoint.subtract(startingPoint);
        
        // Get the length of the speed vector
        double length = speedVector.magnitude();

        if (length > 0) {
            // Normalize the speed vector to get the direction
            speedVector = speedVector.normalize();
        } else {
            return new Point2D[]{new Point2D(0, 0), new Point2D(PROJECTILE_LIFE_TIME, 0.1)}; // If the length is zero, return a zero vector
        }

        return new Point2D[]{speedVector, new Point2D(length/speed, gravity)}; // Return the normalized speed vector
    }

    @Override
    public float getGravityFactor() {
        return 0; // Straight line movement does not consider gravity, so return 0
    }

    @Override
    public float getSpeed() {
        return speed; // Return the speed of the projectile
    }




  


}
