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
public class UserPreference implements Serializable{

    private static UserPreference instance; // Singleton instance of UserPreference
    private static final long serialVersionUID = 1L; // Serial version UID for serialization

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
    private int[] enemyHealth; // Hit points of different types of enemies
    private int[][] damageDealt; // Damage dealt by different types of attacks, this is a 2D array where the first index is the type of attack and the second index is the type of enemy
    private int[] towerConstructionCost; // Construction cost of each type of tower
    private int[] towerEffectiveRange; // Effective range of each type of tower
    private int[] towerRateOfFire; // Rate of fire for each type of tower
    private int artilleryRange; // Range of AOE damage for artillery shells
    private int[] enemyMovementSpeed; // Movement speed of different types of enemies


    private UserPreference() {
        initializeDefaultValues(); // Initialize default values for all preferences
    }

      /**
     * Initialize default values for all preferences
     */
    private void initializeDefaultValues() {
        userName = "Player";
        musicVolume = 0.5f;
        soundVolume = 0.5f;
        numberOfWaves = 10;
        numberOfGroupsPerWave = 3;
        numberOfEnemiesPerGroup = 5;
        delayBetweenWaves = 10000; // 10 seconds
        delayBetweenGroups = 5000; // 5 seconds
        enemyComposition = new int[]{5, 3}; // 5 goblins, 3 knights
        startingGold = 100;
        goldPerEnemy = new int[]{5, 10}; // 5 gold per goblin, 10 gold per knight
        startingHealth = 100;
        enemyHealth = new int[]{50, 100}; // 50 health for goblin, 100 health for knight
        damageDealt = new int[][]{{10, 10}, {20, 20}, {15, 15}}; // arrow, artillery, magic
        towerConstructionCost = new int[]{50, 100, 150}; // arrow, artillery, magic
        towerEffectiveRange = new int[]{5, 4, 3}; // arrow, artillery, magic
        towerRateOfFire = new int[]{1000, 2000, 1500}; // ms between shots
        artilleryRange = 2;
        enemyMovementSpeed = new int[]{2, 1}; // goblin, knight
    }

    /**
     * Get the singleton instance of UserPreference
     * @return the singleton instance of UserPreference
     * 
     * This method is synchronized to ensure thread safety when creating the singleton instance
      */
    public static synchronized UserPreference getInstance() {
        if (instance == null) {
            instance = new UserPreference(); // Create a new instance if it doesn't exist
        }
        return instance; // Return the singleton instance
    }

    
    /**
     * Resets the singleton instance (useful for testing or when loading new preferences)
     */
    public static void resetInstance() {
        instance = null;
    }


    /**
     * Applies settings from a Builder to the singleton instance
     * @param builder the builder with configured settings
     */
    public static void applySettings(Builder builder) {
        instance = builder.build(); // Apply the new settings
    }


    // Getters for all the fields

    public static long getSerialVersionUID() {
        return serialVersionUID; // Return the serial version UID for serialization
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
        return enemyComposition;
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
    public int[][] getDamageDealt() {
        return damageDealt;
    }
    public int[] getTowerConstructionCost() {
        return towerConstructionCost;
    }
    public int[] getTowerEffectiveRange() {
        return towerEffectiveRange;
    }

    public int[] getTowerRateOfFire() {
        return towerRateOfFire;
    }
    public int getArtilleryRange() {
        return artilleryRange;
    }
    public int[] getEnemyMovementSpeed() {
        return enemyMovementSpeed;
    }
    // Use the Builder pattern to create an instance of UserPreference
    // 

    public static class Builder {
        private UserPreference userPreference;

        public Builder() {
            this.userPreference = new UserPreference();
            // Copy all fields from the existing preferences
            this.userPreference.userName = userPreference.userName;
            this.userPreference.musicVolume = userPreference.musicVolume;
            this.userPreference.soundVolume = userPreference.soundVolume;
            this.userPreference.numberOfWaves = userPreference.numberOfWaves;
            this.userPreference.numberOfGroupsPerWave = userPreference.numberOfGroupsPerWave;
            this.userPreference.numberOfEnemiesPerGroup = userPreference.numberOfEnemiesPerGroup;
            this.userPreference.delayBetweenWaves = userPreference.delayBetweenWaves;
            this.userPreference.delayBetweenGroups = userPreference.delayBetweenGroups;
            this.userPreference.enemyComposition = userPreference.enemyComposition;
            this.userPreference.startingGold = userPreference.startingGold;
            this.userPreference.goldPerEnemy = userPreference.goldPerEnemy;
            this.userPreference.startingHealth = userPreference.startingHealth;
            this.userPreference.enemyHealth = userPreference.enemyHealth;
            this.userPreference.damageDealt = userPreference.damageDealt;
            this.userPreference.towerConstructionCost = userPreference.towerConstructionCost;
            this.userPreference.towerEffectiveRange = userPreference.towerEffectiveRange;
            this.userPreference.towerRateOfFire = userPreference.towerRateOfFire;
            this.userPreference.artilleryRange = userPreference.artilleryRange;
            this.userPreference.enemyMovementSpeed = userPreference.enemyMovementSpeed;
        }


        /* This constructor is used to create a Builder object from an existing UserPreference object.
         * This is useful when you want to modify an existing UserPreference object using the Builder pattern.
         * 
         */
        public Builder(UserPreference userPreference) {
            this.userPreference = userPreference;
        }

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

        public Builder setDamageDealt(int[][] damageDealt) {
            userPreference.damageDealt = damageDealt;
            return this;
        }

        public Builder setTowerConstructionCost(int[] towerConstructionCost) {
            userPreference.towerConstructionCost = towerConstructionCost;
            return this;
        }

        public Builder setTowerEffectiveRange(int[] towerEffectiveRange) {
            userPreference.towerEffectiveRange = towerEffectiveRange;
            return this;
        }

        public Builder setTowerRateOfFire(int[] towerRateOfFire) {
            userPreference.towerRateOfFire = towerRateOfFire;
            return this;
        }
        public Builder setArtilleryRange(int artilleryRange) {
            userPreference.artilleryRange = artilleryRange;
            return this;
        }
        public Builder setEnemyMovementSpeed(int[] enemyMovementSpeed) {
            userPreference.enemyMovementSpeed = enemyMovementSpeed;
            return this;
        }
        public UserPreference build() {
            return userPreference;
        }

    }









}
