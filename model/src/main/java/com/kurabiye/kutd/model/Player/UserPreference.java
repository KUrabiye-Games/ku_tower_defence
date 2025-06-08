package com.kurabiye.kutd.model.Player;

import java.io.Serializable;

import java.util.ArrayList;

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
    private ArrayList<ArrayList<int[]>> waveList; // Number of groups per wave
    private int delayBetweenWaves; // Delay between waves in milliseconds
    private int delayBetweenGroups; // Delay between groups in milliseconds
    private int startingGold; // Starting amount of gold for the player
    private int[] goldPerEnemy; // Amount of gold earned when defeating an enemy
    private int startingHealth; // Starting hit points of the player
    private int[] enemyHealth; // Health points for each type of enemy
    
   
    private float artilleryRange; // Special range for artillery towers
    private int[] enemyMovementSpeed; // Movement speed for each enemy type
    private float[] towerSellReturn; // Percentage of cost returned when selling a tower

    // There should be certain changes in the certain fields for tower upgrades

    // The first index is the tower type, and the second index is the level of the tower
    // For example, towerEffectiveRange[0][1] is the effective range of tower type 0 at level 1
     private float[][] damageDealt; // Damage dealt by each tower type to each enemy type
     private float[][] towerEffectiveRange; // Effective range for each tower type
     private float[][] towerRateOfFire; // Rate of fire for each tower type
     private int[][] towerConstructionCost; // Cost to construct each tower type

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
       
        delayBetweenWaves = 8; // 5 seconds
        delayBetweenGroups = 4; // 3 seconds

        waveList = new ArrayList<>(); // Initialize with an empty list
        ArrayList<int[]> wave1 = new ArrayList<>();
        wave1.add(new int[]{1,1}); // Example wave with different enemy types
        wave1.add(new int[]{3,0}); // Example group with different enemy types
        waveList.add(wave1); // Add the first wave to the list
        ArrayList<int[]> wave2 = new ArrayList<>();
        wave2.add(new int[]{2,1}); // Example wave with different enemy types
        wave2.add(new int[]{3,1}); // Example group with different enemy types
        waveList.add(wave2); // Add the second wave to the list
    
        startingGold = 100;
        goldPerEnemy = new int[]{15, 20}; // Gold earned per enemy type
        startingHealth = 5;
        enemyHealth = new int[]{50, 75}; // Health for each enemy type
        damageDealt = new float[][]{
            //Enemy0 Enemy1
            {5.0f, 1.0f}, // Damage for artillery type 0
            {7.0f, 15.0f}, // Damage for artillery type 1
            {8.0f, 7.0f}  // Damage for artillery type 2
        };
        towerConstructionCost = new int[][]{{50, 75, 100},{50, 75, 100}}; // Cost for each tower type
        towerEffectiveRange = new float[][]{{300.0f, 300.0f, 200.0f},{300.0f, 300.0f, 200.0f}}; // Range for each tower type
        towerRateOfFire = new float[][]{{0.5f, 1f, 5f},{0.5f, 1f, 5f}}; // Attack speed for each tower type
        artilleryRange = 3.0f; // Special long range for artillery
        enemyMovementSpeed = new int[]{60, 35}; // Movement speed for each enemy type
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
    
   
    
    
    
    public int getDelayBetweenWaves() {
        return delayBetweenWaves;
    }
    
    public int getDelayBetweenGroups() {
        return delayBetweenGroups;
    }
    
    public ArrayList<ArrayList<int[]>> getWaveList() {
        // Return a deep copy to maintain immutability
        if (waveList == null) {
            return null;
        }
        
        ArrayList<ArrayList<int[]>> copyList = new ArrayList<>();
        for (ArrayList<int[]> wave : waveList) {
            ArrayList<int[]> copyWave = new ArrayList<>();
            for (int[] group : wave) {
                copyWave.add(group.clone());
            }
            copyList.add(copyWave);
        }
        return copyList;
    }
    
    public int getStartingGold() {
        return startingGold;
    }
    
    public int[] getGoldPerEnemy() {
        return goldPerEnemy; 
    }
    
    public int getStartingHealth() {
        return startingHealth;
    }
    
    public int[] getEnemyHealth() {
        return enemyHealth; 
    }
    
    public float[][] getDamageDealt() {
        // Deep copy to maintain immutability
        float[][] copy = new float[damageDealt.length][];
        for (int i = 0; i < damageDealt.length; i++) {
            copy[i] = damageDealt[i].clone();
        }
        return copy;
    }
    
    public int[][] getTowerConstructionCost() {
        return towerConstructionCost.clone(); // Return a copy to maintain immutability
    }
    
    public float[][] getTowerEffectiveRange() {
        return towerEffectiveRange.clone(); // Return a copy to maintain immutability
    }
    
    public float[][] getTowerRateOfFire() {
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
                
                
                this.userPreference.delayBetweenWaves = userPreference.delayBetweenWaves;
                this.userPreference.delayBetweenGroups = userPreference.delayBetweenGroups;
                
                // Copy waveList with deep copy to maintain immutability
                if (userPreference.waveList != null) {
                    this.userPreference.waveList = new ArrayList<>();
                    for (ArrayList<int[]> wave : userPreference.waveList) {
                        ArrayList<int[]> copyWave = new ArrayList<>();
                        for (int[] group : wave) {
                            copyWave.add(group.clone());
                        }
                        this.userPreference.waveList.add(copyWave);
                    }
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

       

        public Builder setDelayBetweenWaves(int delayBetweenWaves) {
            userPreference.delayBetweenWaves = delayBetweenWaves;
            return this;
        }

        public Builder setDelayBetweenGroups(int delayBetweenGroups) {
            userPreference.delayBetweenGroups = delayBetweenGroups;
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

        public Builder setTowerConstructionCost(int[][] towerConstructionCost) {
            userPreference.towerConstructionCost = towerConstructionCost;
            return this;
        }

        public Builder setTowerEffectiveRange(float[][] towerEffectiveRange) {
            userPreference.towerEffectiveRange = towerEffectiveRange;
            return this;
        }

        public Builder setTowerRateOfFire(float[][] towerRateOfFire) {
            userPreference.towerRateOfFire = towerRateOfFire;
            return this;
        }
        
        public Builder setWaveList(ArrayList<ArrayList<int[]>> waveList) {
            // Create a deep copy to ensure immutability
            if (waveList == null) {
                userPreference.waveList = null;
            } else {
                userPreference.waveList = new ArrayList<>();
                for (ArrayList<int[]> wave : waveList) {
                    ArrayList<int[]> copyWave = new ArrayList<>();
                    for (int[] group : wave) {
                        copyWave.add(group.clone());
                    }
                    userPreference.waveList.add(copyWave);
                }
            }
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
