package com.kurabiye.kutd.model.Projectile.ProjectileMoveStrategy;

import com.kurabiye.kutd.model.Coordinates.Point2D;

public interface IProjectileMoveStrategy {
    /*
     * The first Point2D in the array is the speed vector of the projectile
     * The second Point2D.x in the array is the gravity factor of the projectile
     * The second Point2D.y the array is the speed of the projectile
     */
    Point2D[] getSpeedVector(Point2D startingPoint, Point2D targetPoint, float gravity); // Method to calculate the angle of the projectile's trajectory
    float getGravityFactor(); // Method to get the gravity factor of the projectile's trajectory
    float getSpeed(); // Method to get the speed of the projectile's trajectory at the rooute

  
}
