package com.kurabiye.kutd.model.Projectile;


import com.kurabiye.kutd.model.Projectile.ProjectileMoveStrategy.IProjectileMoveStrategy;

import javafx.geometry.Point2D;

public class ProjectileFactory {

    IProjectileMoveStrategy moveStrategy; // Move strategy for the projectiles that the factory will create

    public ProjectileFactory(IProjectileMoveStrategy moveStrategy) {
        this.moveStrategy = moveStrategy; // Initialize the factory with a specific move strategy
    }



    // Create a projectile of a specific type with default values

    public Projectile createProjectile(Projectile.ProjectileType projectileType, Point2D startCoordinate, Point2D targetCoordinate) {
        // Create a new projectile with the specified type and default values from user preferences
        return new Projectile(projectileType,
                startCoordinate, // Starting coordinate of the projectile
                targetCoordinate, // Target coordinate of the projectile       
                moveStrategy); // Set the move strategy for the projectile
    }




}
