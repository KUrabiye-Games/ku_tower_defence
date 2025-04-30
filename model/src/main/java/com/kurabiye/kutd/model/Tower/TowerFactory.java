package com.kurabiye.kutd.model.Tower;

import com.kurabiye.kutd.model.Player.UserPreference;
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

public class TowerFactory implements EnumFactory<Tower, TowerFactory.TowerType> { // Implementing the Factory interface for Tower objects

    public enum TowerType {  // already static enum
        ARTILLERY, // 0 // Artillery tower type
        MAGE, // 1 // Mage tower type
        ARCHER // 2 // Archer tower type
    }

    // Using volatile to ensure visibility across threads
    private static volatile TowerFactory instance = null; // Singleton instance

    private final UserPreference userPreferences; // User preferences object

    private TowerFactory(UserPreference userPreferences) {
        // Private constructor to prevent instantiation
        this.userPreferences = userPreferences; // Initialize user preferences
    }

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
                    instance = new TowerFactory(userPref); // Initialize with user preferences
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
        
        
       
        Tower tower = null;
        
        switch(type) {
            case ARCHER:
                // Create archer tower with parameters from userPreferences
                tower = new Tower(userPreferences.getTowerConstructionCost()[0], 
                                       (int) userPreferences.getTowerSellReturn()[0] * userPreferences.getTowerConstructionCost()[0], 
                                        userPreferences.getTowerEffectiveRange()[0], 
                                        userPreferences.getTowerRateOfFire()[0]);
                tower.setAttackStrategy(new ArcherStrategy());
                // Set other properties from userPreferences
                break;

            case ARTILLERY:
                // Create artillery tower with parameters from userPreferences
                tower = new Tower(userPreferences.getTowerConstructionCost()[1], 
                                          (int) userPreferences.getTowerSellReturn()[1] * userPreferences.getTowerConstructionCost()[1], 
                                          userPreferences.getTowerEffectiveRange()[1], 
                                          userPreferences.getTowerRateOfFire()[1]);
                tower.setAttackStrategy(new ArtilleryStrategy());
                // Set other properties from userPreferences
                break;
                
            case MAGE:
                // Create mage tower with parameters from userPreferences
                tower = new Tower(userPreferences.getTowerConstructionCost()[2], 
                                     (int) userPreferences.getTowerSellReturn()[2] * userPreferences.getTowerConstructionCost()[2], 
                                     userPreferences.getTowerEffectiveRange()[2], 
                                     userPreferences.getTowerRateOfFire()[2]);
                tower.setAttackStrategy(new MageStrategy());
                // Set other properties from userPreferences
                break;
            default:
                throw new IllegalArgumentException("Invalid tower type: " + type);
                
        }
        
        return tower;
    }


}
