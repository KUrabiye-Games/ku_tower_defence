package com.kurabiye.kutd.model.Tower;


import com.kurabiye.kutd.model.Player.UserPreference;
import com.kurabiye.kutd.model.Projectile.ProjectileType;
import com.kurabiye.kutd.model.Tower.AttackStrategy.ArcherStrategy;
import com.kurabiye.kutd.model.Tower.AttackStrategy.ArtilleryStrategy;
import com.kurabiye.kutd.model.Tower.AttackStrategy.MageStrategy;
import com.kurabiye.kutd.util.FactoryPattern.EnumFactory;

/*  TowerFactory.java
 *  This class is a singleton factory for creating different types of towers in the game.
 *  It uses the Singleton design pattern to ensure that only one instance of the factory exists.
 *  The factory creates towers based on the user's preferences and the type of tower requested.
 * 
 *   Tower types include:
 *   - ARTILLERY: Represents an artillery tower.
 *   - MAGE: Represents a mage tower.
 *   - ARCHER: Represents an archer tower.* 
 *   The factory uses the UserPreference class to get the user's preferences for tower attributes such as cost, attack power, range, and attack speed.
 * 
 *  The factory is designed to be used in a game where players can build and upgrade towers to defend against enemies.
 * 
 * @author Atlas Berk Polat
 * @version 1.0
 * @since 2025-04-23

* 
 *   The factory uses the UserPreference class to get the user's preferences for tower attributes such as cost, attack power, range, and attack speed.
 * 
 *  The factory is designed to be used in a game where players can build and upgrade towers to defend against enemies.
 * 
 * @author Atlas Berk Polat
 * @version 1.0
 * @since 2025-04-23
 */

public class TowerFactory implements EnumFactory<Tower, TowerType> { // Implementing the Factory interface for Tower objects



    // Using volatile to ensure visibility across threads
    private static volatile TowerFactory instance = null; // Singleton instance


    /**
     * Thread-safe singleton implementation using double-checked locking
     * @return The singleton instance of TowerFactory
     * @throws IllegalStateException if UserPreference is not initialized
     */
    public static TowerFactory getInstance() {
        // First check (no locking)
        if (instance == null) {
            // Lock for thread safety
            synchronized (TowerFactory.class) {
                // Second check (with locking)
                if (instance == null) {
                    // Check if userPreferences is null before creating the instance
                    UserPreference userPref = UserPreference.getInstance();
                    if(userPref == null) {
                        throw new IllegalStateException("UserPreference instance is not initialized.");
                    }
                    instance = new TowerFactory(); // Initialize with user preferences
                }
            }
        }
        return instance;
    }
    
    /**
     * Resets the singleton instance (useful for testing)
     */
    public static synchronized void resetInstance() {
        instance = null;
    }

    /**
     * This method creates a tower of the specified type.
     * It uses the user preferences to set the tower's attributes such as cost, attack power, range, and attack speed.
     * 
     * @param type The type of tower to create (ARTILLERY, MAGE, ARCHER).
     * @return Tower object of the specified type.
     */
    @Override
    public Tower create(TowerType type) {

        Tower tower = new Tower(type); // Create a new Tower object with the specified type

         // Update the range based on the latest UserPreference values
        tower.setRange(UserPreference.getInstance().getTowerEffectiveRange()[type.getValue()][0]);

        // Update the attack speed based on the latest UserPreference values
        tower.setAttackSpeed(UserPreference.getInstance().getTowerRateOfFire()[type.getValue()][0]);

        // Set the attack strategy based on the tower type

        switch (type) {
            case ARTILLERY:
                tower.setAttackStrategy(new ArtilleryStrategy()); // Set artillery attack strategy
                tower.setProjectileType(ProjectileType.ARTILLERY); // Set projectile type to ARTILLERY
                break;
            case MAGE:
                tower.setAttackStrategy(new MageStrategy()); // Set mage attack strategy
                tower.setProjectileType(ProjectileType.MAGIC); // Set projectile type to MAGIC
                break;
            case ARCHER:
                tower.setAttackStrategy(new ArcherStrategy()); // Set archer attack strategy
                tower.setProjectileType(ProjectileType.ARROW); // Set projectile type to ARROW
                break;
            default:
                throw new IllegalArgumentException("Invalid Tower Type: " + type); // Handle invalid tower types
        }
        

        return tower; // Return the created tower
    
    }
        
        
       

}
