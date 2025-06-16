package com.kurabiye.kutd.model.Player;

import java.io.Serializable;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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

 @JsonIgnoreProperties(ignoreUnknown = true) // Ignore unknown properties during deserialization
public class UserPreference implements Serializable {

    // Using volatile to ensure visibility across threads
    private static volatile UserPreference instance; // Singleton instance of UserPreference
    private static final long serialVersionUID = 1L; // Serial version UID for serialization

    @JsonProperty("userName")
    private String userName;
    
    @JsonProperty("musicVolume")
    private float musicVolume;
    
    @JsonProperty("soundVolume")
    private float soundVolume;
    
    @JsonProperty("waveList")
    private ArrayList<ArrayList<int[]>> waveList;
    
    @JsonProperty("delayBetweenWaves")
    private int delayBetweenWaves;
    
    @JsonProperty("delayBetweenGroups")
    private int delayBetweenGroups;
    
    @JsonProperty("delayBetweenEnemies")
    private int delayBetweenEnemies;
    
    @JsonProperty("startingGold")
    private int startingGold;
    
    @JsonProperty("goldPerEnemy")
    private int[] goldPerEnemy;
    
    @JsonProperty("startingHealth")
    private int startingHealth;
    
    @JsonProperty("enemyHealth")
    private int[] enemyHealth;
    
    @JsonProperty("damageDealt")
    private float[][][] damageDealt;
    
    @JsonProperty("towerEffectiveRange")
    private float[][] towerEffectiveRange;
    
    @JsonProperty("towerRateOfFire")
    private float[][] towerRateOfFire;
    
    @JsonProperty("towerConstructionCost")
    private int[][] towerConstructionCost;
    
    @JsonProperty("artilleryRange")
    private float artilleryRange;
    
    @JsonProperty("enemyMovementSpeed")
    private int[] enemyMovementSpeed;
    
    @JsonProperty("towerSellReturn")
    private float[] towerSellReturn;
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
        delayBetweenEnemies = 1000; // 1 second between enemies

        waveList = new ArrayList<>(); // Initialize with an empty list
        ArrayList<int[]> wave1 = new ArrayList<>();
        wave1.add(new int[]{2,1}); // Example wave with different enemy types
        wave1.add(new int[]{3,2}); // Example group with different enemy types
        waveList.add(wave1); // Add the first wave to the list
        ArrayList<int[]> wave2 = new ArrayList<>();
        wave2.add(new int[]{2,1}); // Example wave with different enemy types
        wave2.add(new int[]{3,1}); // Example group with different enemy types
        waveList.add(wave2); // Add the second wave to the list
    
        startingGold = 1000;
        goldPerEnemy = new int[]{15, 20}; // Gold earned per enemy type
        startingHealth = 5;
        enemyHealth = new int[]{50, 75}; // Health for each enemy type
        // Damage dealt by each tower type to each enemy type
        // damageDealt[TowerType][EnemyType][Level]
        damageDealt = new float[][][]
            {
                {{10.0f, 13.0f}, {5.0f, 7.0f}}, // Damage for artillery type 0
                {{7.0f, 9.0f}, {10.0f, 10.0f}}, // Damage for artillery type 1
                {{7f, 10f}, {10f, 13f}},
            }  // Damage for artillery type 2
        ;
        // Tower construction costs for each type and level
        // towerConstructionCost[TowerType][Level]
        towerConstructionCost = new int[][]{
            {50, 60},
            {75, 85},
            {100, 120}
        }; // Cost for each tower type [TowerType][Level]
        towerEffectiveRange = new float[][]
        {{320.0f, 350.0f},
        {240.0f, 270.0f},
        {280.0f, 320.0f} // Effective range for each tower type [Type][Level]
    }; // Range fr each tower type

        // Tower rate of fire for each type and level
        // towerRateOfFire[TowerType][Level]
        towerRateOfFire = new float[][]{
            {4f, 3.5f},
            {1.2f, 1f},
            {0.25f, 0.15f}
        }; // Attack speed for each tower type

        artilleryRange = 3.0f; // Special long range for artillery
        towerConstructionCost = new int[][]{{50, 75, 100},{50, 75, 100},{50, 75, 100}}; // Cost for each tower type
        towerEffectiveRange = new float[][]{{300.0f, 300.0f, 200.0f},{300.0f, 300.0f, 200.0f},{300.0f, 300.0f, 200.0f}}; // Range for each tower type
        towerRateOfFire = new float[][]{{0.5f, 1f, 5f},{0.5f, 1f, 5f}, {0.5f, 1f, 5f}}; // Attack speed for each tower type
        artilleryRange = 300.0f; // Special long range for artillery
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
    
    @JsonProperty("totalWaves")
    public int getTotalWaves() {
        return waveList != null ? waveList.size() : 0; // Return the number of waves
    }    
    
    public int getGroupsPerWave() {
        if (waveList == null || waveList.isEmpty()) {
            return 0; // No waves defined
        }
        return waveList.get(0).size(); // Return the number of groups in the first wave
    }

    public int getEnemiesPerGroup() {
        if (waveList == null || waveList.isEmpty() || waveList.get(0).isEmpty()) {
            return 0; // No groups defined
        }
        return waveList.get(0).get(0).length; // Return the number of enemies in the first group
    }

    public int getDelayBetweenWaves() {
        return delayBetweenWaves;
    }
    
    public int getDelayBetweenGroups() {
        return delayBetweenGroups;
    }

    public int getDelayBetweenEnemies() {
        // Assuming delay between enemies is the same as delay between groups
        return delayBetweenEnemies; 
    }

    // Number of individual enemies for a group or wave
    public int getGoblinsPerGroup() {
        if (waveList == null || waveList.isEmpty() || waveList.get(0).isEmpty()) {
            return 0; // No groups defined
        }
        return waveList.get(0).get(0)[0]; // Return the number of goblins in the first group
    }

    public int getKnightsPerGroup() {
        if (waveList == null || waveList.isEmpty() || waveList.get(0).isEmpty()) {
            return 0; // No groups defined
        }
        return waveList.get(0).get(0)[1]; // Return the number of knights in the first group
    }
    
    public ArrayList<ArrayList<int[]>> getWaveList() {
        return waveList;
    }

    /**
     * Gets the starting gold amount for the player.
     * @return the starting gold amount
     */
    
    public int getStartingGold() {
        return startingGold;
    }

    /**
     * Gets the gold earned per enemy type.
     * @return the array of gold earned per enemy type
     */
    
    public int[] getGoldPerEnemy() {
        return goldPerEnemy; 
    }
    
    public int getStartingHealth() {
        return startingHealth;
    }

    /**
     * Gets the health points for each type of enemy.
     * * The first index is the enemy type.
     * @return the array of enemy health points
     */
    
    public int[] getEnemyHealth() {
        return enemyHealth; 
    }
    /**
     * Gets the damage dealt by each tower type to each enemy type.
     * * The first index is the tower type, the second index is the enemy type,
     * and the third index is the level of the tower.
     * [TowerType][EnemyType][Level]
     * @return
     */
    public float[][][] getDamageDealt() {
        // Deep copy to maintain immutability
        float[][][] copy = new float[damageDealt.length][][];
        for (int i = 0; i < damageDealt.length; i++) {
            copy[i] = new float[damageDealt[i].length][];
            for (int j = 0; j < damageDealt[i].length; j++) {
                copy[i][j] = damageDealt[i][j].clone();
            }
        }
        return copy;
    }
    
    // Getters for tower-related properties
    public int[][] getTowerConstructionCost() {
        return towerConstructionCost.clone(); // Return a copy to maintain immutability
    }

    public int getArcherTowerCost() {
        return towerConstructionCost != null && towerConstructionCost.length > 0 ? towerConstructionCost[0][0] : 0;
    }

    public int getArtilleryTowerCost() {
        return towerConstructionCost != null && towerConstructionCost.length > 2 ? towerConstructionCost[2][0] : 0;
    }

    public int getMagicTowerCost() {
        return towerConstructionCost != null && towerConstructionCost.length > 1 ? towerConstructionCost[1][0] : 0;
    }
    
    public float[][] getTowerEffectiveRange() {
        return towerEffectiveRange.clone(); // Return a copy to maintain immutability
    }


    /**
     * Gets the rate of fire for each tower type.
     * The first index is the tower type, and the second index is the level of the tower.
     * [TowerType][Level]
     * @return the rate of fire for each tower type
     */
    
    public float[][] getTowerRateOfFire() {
        return towerRateOfFire.clone(); // Return a copy to maintain immutability
    }

    // Getters for specific tower fire rates
    public float getArcherFireRate() {
        return towerRateOfFire != null && towerRateOfFire.length > 0 ? towerRateOfFire[0][0] : 0.0f;
    }
    
    public float getArtilleryFireRate() {
        return towerRateOfFire != null && towerRateOfFire.length > 2 ? towerRateOfFire[2][0] : 0.0f;
    }
    
    public float getMagicFireRate() {
        return towerRateOfFire != null && towerRateOfFire.length > 1 ? towerRateOfFire[1][0] : 0.0f;
    }

    // Artillery AOE
    public float getArtilleryAoeRange() {
        return towerEffectiveRange != null && towerEffectiveRange.length > 2 ? towerEffectiveRange[2][0] : 0.0f;
    }

    // Getters for tower construction costs
    public int getArcherCost() {
        return towerConstructionCost != null && towerConstructionCost.length > 0 ? towerConstructionCost[0][0] : 0;
    }
    
    public int getArtilleryCost() {
        return towerConstructionCost != null && towerConstructionCost.length > 2 ? towerConstructionCost[2][0] : 0;
    }
    
    public int getMagicCost() {
        return towerConstructionCost != null && towerConstructionCost.length > 1 ? towerConstructionCost[1][0] : 0;
    }
    
    // Getters for specific tower ranges
    public float getArtilleryRange() {
        return artilleryRange;
    }

    public float getArcherRange() {
        return towerEffectiveRange != null && towerEffectiveRange.length > 0 ? towerEffectiveRange[0][0] : 0.0f;
    }

    public float getMagicRange() {
        return towerEffectiveRange != null && towerEffectiveRange.length > 1 ? towerEffectiveRange[1][0] : 0.0f;
    }

    // Getters for individual tower damages
    public float getArrowDamage() {
        return damageDealt != null && damageDealt.length > 0 ? damageDealt[0][0][0] : 0.0f;
    }

    public float getArtilleryDamage() {
        return damageDealt != null && damageDealt.length > 2 ? damageDealt[2][0][0] : 0.0f;
    }

    public float getMagicDamage() {
        return damageDealt != null && damageDealt.length > 1 ? damageDealt[1][0][0] : 0.0f;
    }
    
    public int[] getEnemyMovementSpeed() {
        return enemyMovementSpeed.clone(); // Return a copy to maintain immutability
    }

    // Individual enemy movement speeds 
    public int getGoblinSpeed() {
        return enemyMovementSpeed != null && enemyMovementSpeed.length > 0 ? enemyMovementSpeed[0] : 0;
    }
    
    public int getKnightSpeed() {
        return enemyMovementSpeed != null && enemyMovementSpeed.length > 1 ? enemyMovementSpeed[1] : 0;
    }
    
    // Individual enemy health getters
    public int getGoblinHealth() {
        return enemyHealth != null && enemyHealth.length > 0 ? enemyHealth[0] : 0;
    }

    public int getKnightHealth() {
        return enemyHealth != null && enemyHealth.length > 1 ? enemyHealth[1] : 0;
    }

    // Individual gold per enemy getters
    public int getGoblinGoldReward() {
        return goldPerEnemy != null && goldPerEnemy.length > 0 ? goldPerEnemy[0] : 0;
    }

    public int getKnightGoldReward() {
        return goldPerEnemy != null && goldPerEnemy.length > 1 ? goldPerEnemy[1] : 0;
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
                    this.userPreference.damageDealt = new float[userPreference.damageDealt.length][][];
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

        public Builder setTotalWaves(int totalWaves) {
            // Ensure waveList has the correct size
            if (userPreference.waveList == null) {
                userPreference.waveList = new ArrayList<>();
            }
            while (userPreference.waveList.size() < totalWaves) {
                userPreference.waveList.add(new ArrayList<>()); // Add empty waves
            }
            return this;
        }

        public Builder setGroupsPerWave(int groupsPerWave) {
            // Ensure each wave has the correct number of groups
            for (ArrayList<int[]> wave : userPreference.waveList) {
                while (wave.size() < groupsPerWave) {
                    wave.add(new int[]{0, 0}); // Add empty groups
                }
            }
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

        public Builder setEnemiesPerGroup(int enemiesPerGroup) {
            // Ensure each group has the correct number of enemies
            for (ArrayList<int[]> wave : userPreference.waveList) {
                for (int[] group : wave) {
                    if (group.length < enemiesPerGroup) {
                        int[] newGroup = new int[enemiesPerGroup];
                        System.arraycopy(group, 0, newGroup, 0, group.length);
                        wave.set(wave.indexOf(group), newGroup); // Replace with new group
                    }
                }
            }
            return this;
        }

        public Builder setDelayBetweenEnemies(int delayBetweenEnemies) {
            userPreference.delayBetweenEnemies = delayBetweenEnemies;
            return this;
        }

        public Builder setGoblinsPerGroup(int goblinsPerGroup) {
            // Ensure the first enemy type in each group is set to the specified number of goblins
            for (ArrayList<int[]> wave : userPreference.waveList) {
                for (int[] group : wave) {
                    if (group.length > 0) {
                        group[0] = goblinsPerGroup; // Set the first enemy type to goblins
                    }
                }
            }
            return this;
        }

        public Builder setKnightsPerGroup(int knightsPerGroup) {
            // Ensure the second enemy type in each group is set to the specified number of knights
            for (ArrayList<int[]> wave : userPreference.waveList) {
                for (int[] group : wave) {
                    if (group.length > 1) {
                        group[1] = knightsPerGroup; // Set the second enemy type to knights
                    }
                }
            }
            return this;
        }

        public Builder setGoblinGoldReward(int goblinGoldReward) {
            // Ensure the first gold reward is set to the specified amount
            if (userPreference.goldPerEnemy == null || userPreference.goldPerEnemy.length < 1) {
                userPreference.goldPerEnemy = new int[2]; // Initialize if not set
            }
            userPreference.goldPerEnemy[0] = goblinGoldReward; // Set the first enemy type's gold reward
            return this;
        }

        public Builder setKnightGoldReward(int knightGoldReward) {
            // Ensure the second gold reward is set to the specified amount
            if (userPreference.goldPerEnemy == null || userPreference.goldPerEnemy.length < 2) {
                userPreference.goldPerEnemy = new int[2]; // Initialize if not set
            }
            userPreference.goldPerEnemy[1] = knightGoldReward; // Set the second enemy type's gold reward
            return this;
        }

        public Builder setGoblinHealth(int goblinHealth) {
            // Ensure the first enemy type's health is set to the specified amount
            if (userPreference.enemyHealth == null || userPreference.enemyHealth.length < 1) {
                userPreference.enemyHealth = new int[2]; // Initialize if not set
            }
            userPreference.enemyHealth[0] = goblinHealth; // Set the first enemy type's health
            return this;
        }

        public Builder setKnightHealth(int knightHealth) {
            // Ensure the second enemy type's health is set to the specified amount
            if (userPreference.enemyHealth == null || userPreference.enemyHealth.length < 2) {
                userPreference.enemyHealth = new int[2]; // Initialize if not set
            }
            userPreference.enemyHealth[1] = knightHealth; // Set the second enemy type's health
            return this;
        }

        public Builder setArrowDamage(float arrowDamage) {
            // Ensure the first tower type's damage is set to the specified amount
            if (userPreference.damageDealt == null || userPreference.damageDealt.length < 1) {
                userPreference.damageDealt = new float[3][2][2]; // Initialize if not set
            }
            userPreference.damageDealt[0][0][0] = arrowDamage; // Set the first tower type's damage
            return this;
        }

        public Builder setArtilleryDamage(float artilleryDamage) {
            // Ensure the third tower type's damage is set to the specified amount
            if (userPreference.damageDealt == null || userPreference.damageDealt.length < 3) {
                userPreference.damageDealt = new float[3][2][2]; // Initialize if not set
            }
            userPreference.damageDealt[2][0][0] = artilleryDamage; // Set the third tower type's damage
            return this;
        }

        public Builder setMagicDamage(float magicDamage) {
            // Ensure the second tower type's damage is set to the specified amount
            if (userPreference.damageDealt == null || userPreference.damageDealt.length < 2) {
                userPreference.damageDealt = new float[3][2][2]; // Initialize if not set
            }
            userPreference.damageDealt[1][0][0] = magicDamage; // Set the second tower type's damage
            return this;
        }

        public Builder setArcherTowerCost(int archerTowerCost) {
            // Ensure the first tower type's cost is set to the specified amount
            if (userPreference.towerConstructionCost == null || userPreference.towerConstructionCost.length < 1) {
                userPreference.towerConstructionCost = new int[3][3]; // Initialize if not set
            }
            userPreference.towerConstructionCost[0][0] = archerTowerCost; // Set the first tower type's cost
            return this;
        }

        public Builder setArtilleryTowerCost(int artilleryTowerCost) {
            // Ensure the third tower type's cost is set to the specified amount
            if (userPreference.towerConstructionCost == null || userPreference.towerConstructionCost.length < 3) {
                userPreference.towerConstructionCost = new int[3][3]; // Initialize if not set
            }
            userPreference.towerConstructionCost[2][0] = artilleryTowerCost; // Set the third tower type's cost
            return this;
        }

        public Builder setMagicTowerCost(int magicTowerCost) {
            // Ensure the second tower type's cost is set to the specified amount
            if (userPreference.towerConstructionCost == null || userPreference.towerConstructionCost.length < 2) {
                userPreference.towerConstructionCost = new int[3][3]; // Initialize if not set
            }
            userPreference.towerConstructionCost[1][0] = magicTowerCost; // Set the second tower type's cost
            return this;
        }

        public Builder setArcherRange(float archerRange) {
            // Ensure the first tower type's effective range is set to the specified amount
            if (userPreference.towerEffectiveRange == null || userPreference.towerEffectiveRange.length < 1) {
                userPreference.towerEffectiveRange = new float[3][3]; // Initialize if not set
            }
            userPreference.towerEffectiveRange[0][0] = archerRange; // Set the first tower type's effective range
            return this;
        }

        public Builder setMagicRange(float magicRange) {
            // Ensure the second tower type's effective range is set to the specified amount
            if (userPreference.towerEffectiveRange == null || userPreference.towerEffectiveRange.length < 2) {
                userPreference.towerEffectiveRange = new float[3][3]; // Initialize if not set
            }
            userPreference.towerEffectiveRange[1][0] = magicRange; // Set the second tower type's effective range
            return this;
        }

        public Builder setArcherFireRate(float archerFireRate) {
            // Ensure the first tower type's rate of fire is set to the specified amount
            if (userPreference.towerRateOfFire == null || userPreference.towerRateOfFire.length < 1) {
                userPreference.towerRateOfFire = new float[3][3]; // Initialize if not set
            }
            userPreference.towerRateOfFire[0][0] = archerFireRate; // Set the first tower type's rate of fire
            return this;
        }

        public Builder setArtilleryFireRate(float artilleryFireRate) {
            // Ensure the third tower type's rate of fire is set to the specified amount
            if (userPreference.towerRateOfFire == null || userPreference.towerRateOfFire.length < 3) {
                userPreference.towerRateOfFire = new float[3][3]; // Initialize if not set
            }
            userPreference.towerRateOfFire[2][0] = artilleryFireRate; // Set the third tower type's rate of fire
            return this;
        }

        public Builder setMagicFireRate(float magicFireRate) {
            // Ensure the second tower type's rate of fire is set to the specified amount
            if (userPreference.towerRateOfFire == null || userPreference.towerRateOfFire.length < 2) {
                userPreference.towerRateOfFire = new float[3][3]; // Initialize if not set
            }
            userPreference.towerRateOfFire[1][0] = magicFireRate; // Set the second tower type's rate of fire
            return this;
        }

        public Builder setArtilleryAoeRange(float artilleryAoeRange) {
            // Ensure the artillery tower's AOE range is set to the specified amount
            userPreference.artilleryRange = artilleryAoeRange; // Set the artillery tower's AOE range
            return this;
        }

        public Builder setGoblinSpeed(int goblinSpeed) {
            // Ensure the first enemy type's speed is set to the specified amount
            if (userPreference.enemyMovementSpeed == null || userPreference.enemyMovementSpeed.length < 1) {
                userPreference.enemyMovementSpeed = new int[2]; // Initialize if not set
            }
            userPreference.enemyMovementSpeed[0] = goblinSpeed; // Set the first enemy type's speed
            return this;
        }

        public Builder setKnightSpeed(int knightSpeed) {
            // Ensure the second enemy type's speed is set to the specified amount
            if (userPreference.enemyMovementSpeed == null || userPreference.enemyMovementSpeed.length < 2) {
                userPreference.enemyMovementSpeed = new int[2]; // Initialize if not set
            }
            userPreference.enemyMovementSpeed[1] = knightSpeed; // Set the second enemy type's speed
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

        public Builder setDamageDealt(float[][][] damageDealt) {
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
