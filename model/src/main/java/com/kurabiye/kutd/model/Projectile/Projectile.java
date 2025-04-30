package com.kurabiye.kutd.model.Projectile;

import javafx.geometry.Point2D;

public class Projectile  {

    public enum ProjectileType { // Enum for different projectile types
        ARROW, // Arrow projectile type
        MAGIC, // Magic projectile type
        ARTILLERY // Artillery projectile type
    }


    private ProjectileType projectileType; // Type of the projectile

    private Point2D startCoordinate; // Starting coordinate of the projectile on the map

    private Point2D targetCoordinate; // Ending coordinate of the projectile on the map

    private Point2D gravityFactor; // Gravity factor for the projectile's trajectory

    private Point2D coordinate; // Coordinate of the projectile on the map

    private int damage; // Damage dealt by the projectile

    private int speed; // Speed of the projectile

    





   

}
