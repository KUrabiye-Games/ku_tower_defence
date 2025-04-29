package com.kurabiye.kutd.model.Player;

import java.io.Serializable;

/* This is the class where the defined game settings on the settings screen are stored.
 * It is used to set the game settings such as waves, speed, sound, music, and difficulty level etc.
 * 
 * It uses the Singleton pattern to ensure there's only one instance of user preferences
 * throughout the application, while keeping the Builder pattern for configuration.
 * 
 * This class is supposed to be serialized and deserialized to/from a file.
 * 
 * @author: Atlas Berk Polat
 * @version: 1.0
 * @since: 2025-04-23
 * 
 */
public class UserPreference implements Serializable {

    // Using volatile to ensure visibility across threads
    private static volatile UserPreference instance; // Singleton instance of UserPreference
    private static final long serialVersionUID = 1L; // Serial version UID for serialization

    // Game settings fields
    private String userName; // Player's name
    private float musicVolume; // Music volume level
    private float soundVolume; // Sound volume level
    private int numberOfWaves; // Total number of waves in the game
    private int numberOfGroupsPerWave; // Number of groups per wave
    private int numberOfEnemiesPerGroup; // Number of enemies per group
    private int delayBetweenWaves; // Delay between waves in milliseconds
    private int delayBetweenGroups; // Delay between groups in milliseconds
    private int[] enemyComposition; // Composition of types of enemies for a given group or wave
    private int startingGold; // Starting amount of gold for the player
    private int[] goldPerEnemy; // Amount of gold earned when defeating an enemy
    private int startingHealth; // Starting hit points of the player
    private int[] enemyHealth; // Health points for each type of enemy
    private float[][] damageDealt; // Damage dealt by each tower type to each enemy type
    private int[] towerConstructionCost; // Cost to construct each tower type
    private float[] towerEffectiveRange; // Effective range for each tower type
    private float[] towerRateOfFire; // Rate of fire for each tower type
    private float artilleryRange; // Special range for artillery towers
    private int[] enemyMovementSpeed; // Movement speed for each enemy type
    private float[] towerSellReturn; // Percentage of cost returned when selling a tower

    // Private constructor to enforce the singleton pattern
    private UserPreference() {
        // Initialize with default values
        initializeDefaultValues();
    }

    // Initialize default values for all settings
    private void initializeDefaultValues() {
        userName = "Player";
        musicVolume = 0.5f;
        soundVolume = 0.5f;
        numberOfWaves = 10;
        numberOfGroupsPerWave = 3;
        numberOfEnemiesPerGroup = 5;
        delayBetweenWaves = 10000; // 10 seconds
        delayBetweenGroups = 3000; // 3 seconds
        enemyComposition = new int[]{1, 1, 1}; // Equal distribution of enemy types
        startingGold = 100;
        goldPerEnemy = new int[]{10, 20, 30}; // Gold earned per enemy type
        startingHealth = 100;
        enemyHealth = new int[]{50, 75, 100}; // Health for each enemy type
        damageDealt = new float[][]{
            {10.0f, 8.0f, 6.0f}, // Damage for tower type 1
            {7.0f, 12.0f, 9.0f}, // Damage for tower type 2
            {8.0f, 7.0f, 15.0f}  // Damage for tower type 3
        };
        towerConstructionCost = new int[]{50, 75, 100}; // Cost for each tower type
        towerEffectiveRange = new float[]{3.0f, 4.0f, 5.0f}; // Range for each tower type
        towerRateOfFire = new float[]{1.0f, 0.75f, 0.5f}; // Attack speed for each tower type
        artilleryRange = 6.0f; // Special long range for artillery
        enemyMovementSpeed = new int[]{2, 3, 5}; // Movement speed for each enemy type
        towerSellReturn = new float[]{0.5f, 0.6f, 0.7f}; // Percentage returned when selling
    }

    /**
     * Thread-safe singleton implementation using double-checked locking
     * @return the singleton instance of UserPreference
     */
    public static UserPreference getInstance() {
        // First check (no locking)
        if (instance == null) {
            // Lock for thread safety
            synchronized (UserPreference.class) {
                // Second check (with locking)
                if (instance == null) {
                    instance = new UserPreference(); // Create a new instance if it doesn't exist
                }
            }
        }
        return instance; // Return the singleton instance
    }

    /**
     * Resets the singleton instance (useful for testing or when loading new preferences)
     */
    public static synchronized void resetInstance() {
        instance = null;
    }

    /**
     * Applies settings from a Builder to the singleton instance
     * @param builder the builder with configured settings
     */
    public static synchronized void applySettings(Builder builder) {
        instance = builder.build(); // Apply the new settings
    }

    // Getters for all fields - thread-safe by being effectively immutable after construction

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUserName() {
        return userName;
    }

    public float getMusicVolume() {
        return musicVolume;
    }
    
    public float getSoundVolume() {
        return soundVolume;
    }
    
    public int getNumberOfWaves() {
        return numberOfWaves;
    }
    
    public int getNumberOfGroupsPerWave() {
        return numberOfGroupsPerWave;
    }
    
    public int getNumberOfEnemiesPerGroup() {
        return numberOfEnemiesPerGroup;
    }
    
    public int getDelayBetweenWaves() {
        return delayBetweenWaves;
    }
    
    public int getDelayBetweenGroups() {
        return delayBetweenGroups;
    }
    
    public int[] getEnemyComposition() {
        return enemyComposition.clone(); // Return a copy to maintain immutability
    }
    
    public int getStartingGold() {
        return startingGold;
    }
    
    public int[] getGoldPerEnemy() {
        return goldPerEnemy.clone(); // Return a copy to maintain immutability
    }
    
    public int getStartingHealth() {
        return startingHealth;
    }
    
    public int[] getEnemyHealth() {
        return enemyHealth.clone(); // Return a copy to maintain immutability
    }
    
    public float[][] getDamageDealt() {
        // Deep copy to maintain immutability
        float[][] copy = new float[damageDealt.length][];
        for (int i = 0; i < damageDealt.length; i++) {
            copy[i] = damageDealt[i].clone();
        }
        return copy;
    }
    
    public int[] getTowerConstructionCost() {
        return towerConstructionCost.clone(); // Return a copy to maintain immutability
    }
    
    public float[] getTowerEffectiveRange() {
        return towerEffectiveRange.clone(); // Return a copy to maintain immutability
    }
    
    public float[] getTowerRateOfFire() {
        return towerRateOfFire.clone(); // Return a copy to maintain immutability
    }
    
    public float getArtilleryRange() {
        return artilleryRange;
    }
    
    public int[] getEnemyMovementSpeed() {
        return enemyMovementSpeed.clone(); // Return a copy to maintain immutability
    }
    
    public float[] getTowerSellReturn() {
        return towerSellReturn.clone(); // Return a copy to maintain immutability
    }

    // Builder class for constructing UserPreference instances in a fluent manner
    public static class Builder {
        private UserPreference userPreference;

        public Builder() {
            this.userPreference = new UserPreference();
        }

        /* This constructor is used to create a Builder object from an existing UserPreference object.
         * This is useful when you want to modify an existing UserPreference object using the Builder pattern.
         */
        public Builder(UserPreference userPreference) {
            this.userPreference = new UserPreference();
            
            // Copy all fields from the existing preferences
            if (userPreference != null) {
                this.userPreference.userName = userPreference.userName;
                this.userPreference.musicVolume = userPreference.musicVolume;
                this.userPreference.soundVolume = userPreference.soundVolume;
                this.userPreference.numberOfWaves = userPreference.numberOfWaves;
                this.userPreference.numberOfGroupsPerWave = userPreference.numberOfGroupsPerWave;
                this.userPreference.numberOfEnemiesPerGroup = userPreference.numberOfEnemiesPerGroup;
                this.userPreference.delayBetweenWaves = userPreference.delayBetweenWaves;
                this.userPreference.delayBetweenGroups = userPreference.delayBetweenGroups;
                
                if (userPreference.enemyComposition != null) {
                    this.userPreference.enemyComposition = userPreference.enemyComposition.clone();
                }
                
                this.userPreference.startingGold = userPreference.startingGold;
                
                if (userPreference.goldPerEnemy != null) {
                    this.userPreference.goldPerEnemy = userPreference.goldPerEnemy.clone();
                }
                
                this.userPreference.startingHealth = userPreference.startingHealth;
                
                if (userPreference.enemyHealth != null) {
                    this.userPreference.enemyHealth = userPreference.enemyHealth.clone();
                }
                
                if (userPreference.damageDealt != null) {
                    this.userPreference.damageDealt = new float[userPreference.damageDealt.length][];
                    for (int i = 0; i < userPreference.damageDealt.length; i++) {
                        this.userPreference.damageDealt[i] = userPreference.damageDealt[i].clone();
                    }
                }
                
                if (userPreference.towerConstructionCost != null) {
                    this.userPreference.towerConstructionCost = userPreference.towerConstructionCost.clone();
                }
                
                if (userPreference.towerEffectiveRange != null) {
                    this.userPreference.towerEffectiveRange = userPreference.towerEffectiveRange.clone();
                }
                
                if (userPreference.towerRateOfFire != null) {
                    this.userPreference.towerRateOfFire = userPreference.towerRateOfFire.clone();
                }
                
                this.userPreference.artilleryRange = userPreference.artilleryRange;
                
                if (userPreference.enemyMovementSpeed != null) {
                    this.userPreference.enemyMovementSpeed = userPreference.enemyMovementSpeed.clone();
                }
                
                if (userPreference.towerSellReturn != null) {
                    this.userPreference.towerSellReturn = userPreference.towerSellReturn.clone();
                }
            }
        }

        // Builder setter methods - each returns this Builder instance for method chaining

        public Builder setUserName(String userName) {
            userPreference.userName = userName;
            return this;
        }

        public Builder setMusicVolume(float musicVolume) {
            userPreference.musicVolume = musicVolume;
            return this;
        }

        public Builder setSoundVolume(float soundVolume) {
            userPreference.soundVolume = soundVolume;
            return this;
        }

        public Builder setNumberOfWaves(int numberOfWaves) {
            userPreference.numberOfWaves = numberOfWaves;
            return this;
        }

        public Builder setNumberOfGroupsPerWave(int numberOfGroupsPerWave) {
            userPreference.numberOfGroupsPerWave = numberOfGroupsPerWave;
            return this;
        }

        public Builder setNumberOfEnemiesPerGroup(int numberOfEnemiesPerGroup) {
            userPreference.numberOfEnemiesPerGroup = numberOfEnemiesPerGroup;
            return this;
        }

        public Builder setDelayBetweenWaves(int delayBetweenWaves) {
            userPreference.delayBetweenWaves = delayBetweenWaves;
            return this;
        }

        public Builder setDelayBetweenGroups(int delayBetweenGroups) {
            userPreference.delayBetweenGroups = delayBetweenGroups;
            return this;
        }

        public Builder setEnemyComposition(int[] enemyComposition) {
            userPreference.enemyComposition = enemyComposition;
            return this;
        }

        public Builder setStartingGold(int startingGold) {
            userPreference.startingGold = startingGold;
            return this;
        }

        public Builder setGoldPerEnemy(int[] goldPerEnemy) {
            userPreference.goldPerEnemy = goldPerEnemy;
            return this;
        }

        public Builder setStartingHealth(int startingHealth) {
            userPreference.startingHealth = startingHealth;
            return this;
        }

        public Builder setEnemyHealth(int[] enemyHealth) {
            userPreference.enemyHealth = enemyHealth;
            return this;
        }

        public Builder setDamageDealt(float[][] damageDealt) {
            userPreference.damageDealt = damageDealt;
            return this;
        }

        public Builder setTowerConstructionCost(int[] towerConstructionCost) {
            userPreference.towerConstructionCost = towerConstructionCost;
            return this;
        }

        public Builder setTowerEffectiveRange(float[] towerEffectiveRange) {
            userPreference.towerEffectiveRange = towerEffectiveRange;
            return this;
        }

        public Builder setTowerRateOfFire(float[] towerRateOfFire) {
            userPreference.towerRateOfFire = towerRateOfFire;
            return this;
        }
        
        public Builder setArtilleryRange(float artilleryRange) {
            userPreference.artilleryRange = artilleryRange;
            return this;
        }
        
        public Builder setEnemyMovementSpeed(int[] enemyMovementSpeed) {
            userPreference.enemyMovementSpeed = enemyMovementSpeed;
            return this;
        }
        
        public Builder setTowerSellReturn(float[] towerSellReturn) {
            userPreference.towerSellReturn = towerSellReturn;
            return this;
        }
        
        public UserPreference build() {
            return userPreference;
        }
    }
}
