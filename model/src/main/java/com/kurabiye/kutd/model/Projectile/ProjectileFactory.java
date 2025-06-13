package com.kurabiye.kutd.model.Projectile;


import com.kurabiye.kutd.model.Coordinates.Point2D;
import com.kurabiye.kutd.model.Player.UserPreference;


import com.kurabiye.kutd.model.Projectile.ProjectileMoveStrategy.ArrowProjectileMoveStrategy;
import com.kurabiye.kutd.model.Projectile.ProjectileMoveStrategy.ArtilleryProjectileMoveStrategy;
import com.kurabiye.kutd.model.Projectile.ProjectileMoveStrategy.IProjectileMoveStrategy;
import com.kurabiye.kutd.model.Projectile.ProjectileMoveStrategy.MagicProjectileMoveStrategy;


public class ProjectileFactory {

    private static ProjectileFactory instance; // Singleton instance

    private float artilleryRange;
    
    // Private constructor to prevent instantiation from outside
    private ProjectileFactory() {
        // Private constructor implementation
        artilleryRange = UserPreference.getInstance().getArtilleryRange(); // Get the artillery range from user preferences
    }
    
    // Method to get the singleton instance of the ProjectileFactory
    public static synchronized ProjectileFactory getInstance() {
        if (instance == null) {
            instance = new ProjectileFactory();
        }
        return instance;
    }

    // Create a projectile of a specific type with default values
    public Projectile createProjectile(ProjectileType projectileType, Point2D startCoordinate, Point2D targetCoordinate, int projectileLevel) {
        // Create a new projectile with the specified type and default values from user preferences
        float projectileAreaDamage = 40f; // Default area damage for the projectile
        IProjectileMoveStrategy moveStrategy = null; // Initialize the move strategy
        DamageType explosionType = DamageType.TARGET; // Default explosion type for the projectile
        switch (projectileType) {
            case ARROW:
                moveStrategy = new ArrowProjectileMoveStrategy(); // Set the move strategy for arrow projectiles
                break;
            case MAGIC:
                moveStrategy = new MagicProjectileMoveStrategy(); // Set the move strategy for magic projectiles
                break;
            case ARTILLERY:
                moveStrategy = new ArtilleryProjectileMoveStrategy(); // Set the move strategy for artillery projectiles
                projectileAreaDamage = artilleryRange * projectileAreaDamage; // Set the area damage for artillery projectiles
                explosionType = DamageType.AREA; // Set the explosion type for artillery projectiles
                break;
            default:
                throw new IllegalArgumentException("Invalid projectile type: " + projectileType); // Handle invalid projectile types
        }

        Projectile product = new Projectile(projectileType,
                startCoordinate, // Starting coordinate of the projectile
                targetCoordinate, // Target coordinate of the projectile       
                moveStrategy,
                projectileAreaDamage,
                explosionType,
                projectileLevel); // Create a new projectile with the specified parameters
               


        
        return product; // Return the created projectile
        
    }
}
