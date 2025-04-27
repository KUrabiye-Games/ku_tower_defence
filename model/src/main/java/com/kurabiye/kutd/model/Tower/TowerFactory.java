package com.kurabiye.kutd.model.Tower;

import com.kurabiye.kutd.model.Player.UserPreference;
import com.kurabiye.kutd.model.Tower.AttackStrategy.ArcherStrategy;
import com.kurabiye.kutd.model.Tower.AttackStrategy.ArtilleryStrategy;
import com.kurabiye.kutd.model.Tower.AttackStrategy.MageStrategy;

/*  TowerFactory.java
 *  This class is a singleton factory for creating different types of towers in the game.
 *  It uses the Singleton design pattern to ensure that only one instance of the factory exists.
 *  The factory creates towers based on the user's preferences and the type of tower requested.
 * 
 * 
 * 
 *   Tower types include:
 *   - ARTILLERY: Represents an artillery tower.
 *   - MAGE: Represents a mage tower.
 *   - ARCHER: Represents an archer tower.
 * 
 *   The factory uses the UserPreference class to get the user's preferences for tower attributes such as cost, attack power, range, and attack speed.
 * 
 *  The factory is designed to be used in a game where players can build and upgrade towers to defend against enemies.
 * 
 * @author Atlas Berk Polat
 * @version 1.0
 * @since 2025-04-23
 */

public class TowerFactory{

    public enum TowerType {  // already static enum
        ARTILLERY, // 0 // Artillery tower type
        MAGE, // 1 // Mage tower type
        ARCHER // 2 // Archer tower type
    }

    private static TowerFactory instance = null; // Singleton instance

    private UserPreference userPreferences; // User preferences object

    private TowerFactory(UserPreference userPreferences) {
        // Private constructor to prevent instantiation
        this.userPreferences = userPreferences; // Initialize user preferences
    }

    /*
     * 
     * This method returns the singleton instance of the TowerFactory.
     * 
     * * @return TowerFactory instance
     */
    
    public static TowerFactory getInstance() {
        // Private constructor to prevent instantiation
        if (instance == null) {
            // Initialize the singleton instance with user preferences
            // check if userPreferences is null before creating the instance
            if(UserPreference.getInstance() == null) {
                throw new IllegalStateException("UserPreference instance is not initialized.");
            }

            instance = new TowerFactory(UserPreference.getInstance()); // Initialize the singleton instance with user preferences
        }
        return instance;
    }

    /*
     * This method creates a tower of the specified type.
     * It uses the user preferences to set the tower's attributes such as cost, attack power, range, and attack speed.
     * 
     * @param type The type of tower to create (ARTILLERY, MAGE, ARCHER).
     * @return Tower object of the specified type.
     */
    public Tower createTower(TowerType type) {
        switch (type) {
            case ARCHER:
                Tower productArcher = new ArcherTower(userPreferences.getTowerConstructionCost()[0], // cost
                        (int) userPreferences.getTowerSellReturn()[0] * userPreferences.getTowerConstructionCost()[0], // sell return
                        userPreferences.getDamageDealt()[0], // attack power
                        userPreferences.getTowerEffectiveRange()[0], // range
                        userPreferences.getTowerRateOfFire()[0]); // attack speed

                // Set the attack strategy for the ArcherTower
                productArcher.setAttackStrategy(new ArcherStrategy()); // set attack strategy

                return productArcher; // Return the created ArcherTower object

            case MAGE:
                Tower productMage = new MageTower(userPreferences.getTowerConstructionCost()[1], // cost
                        (int) userPreferences.getTowerSellReturn()[1] * userPreferences.getTowerConstructionCost()[1], // sell return
                        userPreferences.getDamageDealt()[1], // attack power
                        userPreferences.getTowerEffectiveRange()[1], // range
                        userPreferences.getTowerRateOfFire()[1]); // attack speed

                // Set the attack strategy for the MageTower
                productMage.setAttackStrategy(new MageStrategy()); // set attack strategy

                return productMage; // Return the created MageTower object

            case ARTILLERY:
                Tower productArtillery =  new ArtilleryTower(userPreferences.getTowerConstructionCost()[2], // cost
                        (int) userPreferences.getTowerSellReturn()[2] * userPreferences.getTowerConstructionCost()[2], // sell return
                        userPreferences.getDamageDealt()[2], // attack power
                        userPreferences.getTowerEffectiveRange()[2], // range
                        userPreferences.getTowerRateOfFire()[2],
                        userPreferences.getArtilleryRange()); // attack speed

                // Set the attack strategy for the ArtilleryTower
                productArtillery.setAttackStrategy(new ArtilleryStrategy()); // set attack strategy

                return productArtillery; // Return the created ArtilleryTower object
            default:
                throw new IllegalArgumentException("Invalid tower type: " + type); // Invalid tower type
        }

    }
}
