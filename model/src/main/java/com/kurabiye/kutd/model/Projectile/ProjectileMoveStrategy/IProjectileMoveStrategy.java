package com.kurabiye.kutd.model.Projectile.ProjectileMoveStrategy;

import com.kurabiye.kutd.model.Coordinates.Point2D;

public interface IProjectileMoveStrategy {
    Point2D[] getSpeedVector(Point2D startingPoint, Point2D targetPoint, float gravity); // Method to calculate the angle of the projectile's trajectory
    float getGravityFactor(); // Method to get the gravity factor of the projectile's trajectory
    float getSpeed(); // Method to get the speed of the projectile's trajectory at the rooute

  
}
