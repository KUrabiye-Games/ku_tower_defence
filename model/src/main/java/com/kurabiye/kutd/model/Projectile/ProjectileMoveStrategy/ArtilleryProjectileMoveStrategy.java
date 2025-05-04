package com.kurabiye.kutd.model.Projectile.ProjectileMoveStrategy;

import com.kurabiye.kutd.model.Coordinates.Point2D;

public class ArtilleryProjectileMoveStrategy implements IProjectileMoveStrategy {

    private static final float GRAVITY = 100.81f; // Gravity constant


    private static final float ARTILLERY_SPEED = 180.0f; // Speed of the artillery projectile
    

    @Override
    public Point2D[] getSpeedVector(Point2D startingPoint, Point2D targetPoint, float gravity) {

        // Draw a curve from the starting point to the target point
        // The curve should be a parabola with the starting point as the vertex and the target point as the focus
        // The parabola should be symmetric with respect to the line connecting the starting point and the target point
        // The parabola should use the gravity factor to determine the inital velocity and angle

        // Use the x-distance and y-distance to calculate the angle of the projectile's trajectory

        // Use the gravity factor to determine the initial velocity and angle of the projectile's trajectory

        double xDistance = targetPoint.getX() - startingPoint.getX(); // Calculate the x-distance
        double yDistance = targetPoint.getY() - startingPoint.getY(); // Calculate the y-distance
        double v = ARTILLERY_SPEED; // Initial speed
        double g = GRAVITY; // Use the constant GRAVITY

        // Solve for the launch angle (theta) using the trajectory equation:
        // y = x*tan(theta) - (g*x^2)/(2*v^2*cos^2(theta))
        // Let u = tan(theta). Use 1/cos^2(theta) = 1 + tan^2(theta) = 1 + u^2
        // y = x*u - (g*x^2)/(2*v^2) * (1 + u^2)
        // Rearrange into quadratic form: (g*x^2)/(2*v^2)*u^2 - x*u + (y + (g*x^2)/(2*v^2)) = 0
        // a*u^2 + b*u + c = 0

        double term1 = (g * xDistance * xDistance) / (2.0 * v * v);

        // Quadratic equation coefficients
        double a = term1;
        double b = -xDistance;
        double c = yDistance + term1;

        float directionCoefficient = 1.0f; // Default direction coefficient


        // check if the target is above or below the starting point

        double explosiveActtionTime = 1.5f; // Time to explode after reaching the target
        double projectileLifeTime = 3.5f; // Life time of the projectile

        if (targetPoint.getY() > startingPoint.getY()) {
            // If the target is below the starting point, we need to adjust the projectile life time
            projectileLifeTime = 4.5f;
            explosiveActtionTime = 3.2; // Time to explode
        }

        // check if the target is on the left side of the starting point or not
        if (targetPoint.getX() < startingPoint.getX()) {
            directionCoefficient = -1.0f; // Reverse the direction if the target is on the left side
         }

        // Calculate the discriminant
        double discriminant = b * b - 4.0 * a * c;

        // Check if a real solution exists (discriminant must be non-negative)
        if (discriminant < 0) {
            // Target is out of range or cannot be hit with this speed
            //System.err.println("Target out of range or unreachable for artillery projectile.");
            // Return a default vector or handle the error appropriately
            // For example, aim horizontally or directly at the target without gravity compensation
            // Returning a horizontal vector might be safer than NaN results later

            // Pick a random number between -8 and -5

            float randomY = (float) (-4 + -1* Math.random() * 4); // Random y value between -3 and 0

             return  new Point2D[]{ new Point2D(1 * directionCoefficient, randomY).normalize(), new Point2D(projectileLifeTime, explosiveActtionTime)}; // Aim horizontally as a fallback
        }

        // Solve for tan(theta) using the quadratic formula: u = [-b +/- sqrt(discriminant)] / (2a)
        // We typically choose one solution. The '+' root often gives the lower trajectory (faster flight),
        // while the '-' root gives the higher trajectory. Let's choose the lower one.
        // Note: If xDistance is negative, the interpretation might change. Assuming xDistance >= 0.
        double tanTheta = (-b + Math.sqrt(discriminant)) / (2.0 * a);
        // double tanThetaHigh = (-b - Math.sqrt(discriminant)) / (2.0 * a); // Higher trajectory option

        // Calculate the angle in radians
        double angle = Math.atan(tanTheta);

        
        

        // Calculate the initial velocity vector components based on the chosen angle
        double initialVx = v * Math.cos(angle) * directionCoefficient; // Adjusted for direction
        double initialVy = v * Math.sin(angle);

        if(initialVy > 0) {
            initialVy = -initialVy; // Ensure the vertical component is positive
        }

        // Create the initial velocity vector (magnitude = ARTILLERY_SPEED)
        Point2D speedVector = new Point2D(initialVx, initialVy);

        // Return the normalized direction vector
        return new Point2D[]{speedVector.normalize(), new Point2D(projectileLifeTime, explosiveActtionTime)}; // Return the normalized speed vector
    }

    @Override
    public float getGravityFactor() {
        return GRAVITY; // Straight line movement does not consider gravity, so return 0
    }

    @Override
    public float getSpeed() {
        return ARTILLERY_SPEED;
    }

    



  


}
