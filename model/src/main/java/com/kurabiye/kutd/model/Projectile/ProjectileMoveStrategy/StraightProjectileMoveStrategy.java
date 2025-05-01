package com.kurabiye.kutd.model.Projectile.ProjectileMoveStrategy;

import javafx.geometry.Point2D;

public class StraightProjectileMoveStrategy implements IProjectileMoveStrategy {

    @Override
    public Point2D getSpeedVector(Point2D startingPoint, Point2D targetPoint, float gravity) {
        
        Point2D speedVector = targetPoint.subtract(startingPoint); // Calculate the speed vector from the starting point to the target point
        
        speedVector = speedVector.normalize(); // Normalize the speed vector to get the direction of movement

        return speedVector; // Return the normalized speed vector
    }

    @Override
    public float getGravityFactor() {
        return 0; // Straight line movement does not consider gravity, so return 0
    }

    @Override
    public float getSpeed() {
        return 15.0f;
    }



  


}
