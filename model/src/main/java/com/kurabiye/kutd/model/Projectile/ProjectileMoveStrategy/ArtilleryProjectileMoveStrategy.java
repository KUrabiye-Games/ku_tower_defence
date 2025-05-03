package com.kurabiye.kutd.model.Projectile.ProjectileMoveStrategy;

import com.kurabiye.kutd.model.Coordinates.Point2D;

public class ArtilleryProjectileMoveStrategy implements IProjectileMoveStrategy {

    private static final float GRAVITY = 50.81f; // Gravity constant
    

    @Override
    public Point2D getSpeedVector(Point2D startingPoint, Point2D targetPoint, float gravity) {

        // Draw a curve from the starting point to the target point
        // The curve should be a parabola with the starting point as the vertex and the target point as the focus
        // The parabola should be symmetric with respect to the line connecting the starting point and the target point
        // The parabola should use the gravity factor to determine the inital velocity and angle

        // Use the x-distance and y-distance to calculate the angle of the projectile's trajectory

        // Use the gravity factor to determine the initial velocity and angle of the projectile's trajectory

        // v*sin(theta) * t - g * t^2 = yDistance
        // v*cos(theta) * t = xDistance

        double xDistance = targetPoint.getX() - startingPoint.getX(); // Calculate the x-distance from the starting point to the target point
        double yDistance = targetPoint.getY() - startingPoint.getY(); // Calculate the y-distance from the starting point to the target point
        
        // speed^2 = x-dist^2 + (y-dist - g*t^2)^2
        // g*t^2 = y-dist - sqrt(speed^2 - x-dist^2) 
        // t = sqrt( y-dist - sqrt(speed^2 - x-dist^2)  / g)

        float time = (float) Math.sqrt((yDistance - Math.sqrt(Math.pow(GRAVITY, 2) - Math.pow(xDistance, 2))) / GRAVITY); // Calculate the time of flight using the gravity factor and the distances


        // find the angle using the time and x-distance
        double angle = Math.atan2(yDistance, xDistance); // Calculate the angle of the projectile's trajectory using the arctangent function


        // calculate the sppeed vector using the angle and the speed
        double speed = getSpeed(); // Get the speed of the projectile
        Point2D speedVector = new Point2D(speed * Math.cos(angle), speed * Math.sin(angle)); // Calculate the speed vector using the angle and the speed

        return speedVector; // Return the normalized speed vector
    }

    @Override
    public float getGravityFactor() {
        return 0; // Straight line movement does not consider gravity, so return 0
    }

    @Override
    public float getSpeed() {
        return 450.0f;
    }



  


}
