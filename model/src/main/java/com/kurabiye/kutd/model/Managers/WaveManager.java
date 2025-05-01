package com.kurabiye.kutd.model.Managers;

import java.util.Random;

import com.kurabiye.kutd.model.Player.UserPreference;
import com.kurabiye.kutd.model.Wave.WaveInfo;

/* WaveManager.java
 * This class is responsible for managing the waves of enemies in the game.
 * It holds the information about the current wave, the number of enemies in each wave,
 * the time between waves, and the type of enemies in each wave.
 * It gets the information from the UserPreferences.
 * 
 * 
 * @author: Atlas Berk Polat
 * @version: 1.0
 * @since: 2025-04-28
 */

public class WaveManager {


    Random random = new Random(); // Random number generator for enemy types

    // This number is not spesified in the user preferences, so it is hardcoded.
    // It is the time between enemy spawns in seconds.

    private static final long GRACE_WAIT_TIME = 4; // Time in milliseconds between waves
    private static final long ENEMY_SPAWN_TIME = 1; // Time in milliseconds between enemy spawns
    

    private WaveInfo waveInfo; // Wave information object

    private int currentWaveIndex = 0; // Current wave index
    private int currentGroupIndex = 0; // Current group index

    public enum EnemyAttackState { // Enum for wave states
        GRACE_PERIOD, // Grace period before the wave starts
        GROUP_WAITING, // Waiting for the next group to spawn
        SPAWNING, // Spawning enemies in the current group
        WAVE_WAITING, // Waiting for the next wave to start
        NO_ENEMY_LEFT // No enemies left to spawn

    }

    private EnemyAttackState waveState = EnemyAttackState.GRACE_PERIOD; // Current state of the wave

    

    private long lastGracePeriodTime = 0; // The Delta Time of the Last time the wave was spawned
    private long lastWaveWaitTime = 0; // The Delta Time of the Last time the wave was spawned
    private long lastGroupWaitTime = 0; // The Delta Time of the Last time the wave was spawned
    private long lastEnemySpawnTime = 0; // The Delta Time of the Last time the wave was spawned


    private int leftEnemiesInGroup = 0; // Number of enemies left in the current group

    private int[] currentGroupDecomposition; // Composition of types of enemies for a given group or wave



    public WaveManager(UserPreference userPreferences) {
        this.waveInfo = new WaveInfo(userPreferences); // Get the wave information from user preferences
    }

    /* This is the method that would keep track of the current wave and group index.
     * It will check if there is enough time passed to spawn a new wave or group.
     * Or enough time passed to spawn a new enemy.
     * 
     * 
     * @param deltaTime The time passed since the last update in milliseconds
     * *@return The index of the enemy to spawn or -1 if no enemy is spawned -2 if there is no enemy left to spawn
     * 
     */
    public int getEnemy(long deltaTime) {

        if(waveState == EnemyAttackState.GRACE_PERIOD) { // If the wave is in grace period
            if (lastGracePeriodTime < GRACE_WAIT_TIME) { // If this is the first time the grace period is started
                lastGracePeriodTime += deltaTime; // Set the last grace period time to the current time
            } else{
                lastGracePeriodTime = 0; // Reset the last grace period time
                waveState = EnemyAttackState.SPAWNING; // Change the state to group waiting
                leftEnemiesInGroup = waveInfo.getTotalEnemyInGroup(0, 0); // Get the number of enemies in the current group
            }
            return -1; // Placeholder for enemy spawning logic
        }

        if (waveState == EnemyAttackState.SPAWNING) { // If the wave is in group waiting state
           // check if there no is any enemy left to spawn in the current group
           if(leftEnemiesInGroup == 0){
                // check if there is any group left to spawn in the current wave
                if(currentGroupIndex < waveInfo.getTotalNumberOfGroupsInWave(currentWaveIndex) - 1){
                    currentGroupIndex++; // Increment the group index
                    leftEnemiesInGroup = waveInfo.getTotalEnemyInGroup(currentWaveIndex, currentGroupIndex); // Get the number of enemies in the current group
                    currentGroupDecomposition = waveInfo.getWaveGroupDecomposition(currentWaveIndex, currentGroupIndex); // Get the decomposition of the current group
                    lastGroupWaitTime = 0; // Reset the last group wait time
                    waveState = EnemyAttackState.GROUP_WAITING; // Change the state to group waiting
                } else {
                    // check if there is any wave left to spawn
                    if(currentWaveIndex < waveInfo.getTotalNumberOfWaves() - 1){
                        currentWaveIndex++; // Increment the wave index
                        currentGroupIndex = 0; // Reset the group index
                        leftEnemiesInGroup = waveInfo.getTotalEnemyInGroup(currentWaveIndex, currentGroupIndex); // Get the number of enemies in the current group
                        currentGroupDecomposition = waveInfo.getWaveGroupDecomposition(currentWaveIndex, currentGroupIndex); // Get the decomposition of the current group
                        lastWaveWaitTime = 0; // Reset the last wave wait time
                        waveState = EnemyAttackState.WAVE_WAITING; // Change the state to wave waiting
                    } else {
                        waveState = EnemyAttackState.NO_ENEMY_LEFT; // Change the state to no enemy left
                    }
                }
                return -1; // Placeholder for enemy spawning logic
           }

              if (lastEnemySpawnTime < ENEMY_SPAWN_TIME) { // If this is the first time the enemy spawn time is started
                 lastEnemySpawnTime += deltaTime; // Set the last enemy spawn time to the current time
                } else {
                 lastEnemySpawnTime = 0; // Reset the last enemy spawn time
                 leftEnemiesInGroup--; // Decrement the number of enemies left in the group

                 // Choose the enemy type based on the wave and group index

                
                    // Possible bug if the currentGroupDecomposition is empty or null
                    // and we 
                    while (true) { // Loop until a valid enemy type is found
                        int enemyTypeIndex = random.nextInt(currentGroupDecomposition.length); // Get a random enemy type index
                        if (currentGroupDecomposition[enemyTypeIndex] > 0) { // Check if the enemy type is available
                            currentGroupDecomposition[enemyTypeIndex]--; // Decrement the number of enemies of that type
                            return enemyTypeIndex; // Return the enemy type index to spawn
                        } 
                    }
                }
                
            
        }


        if (waveState == EnemyAttackState.GROUP_WAITING) { // If the wave is in group waiting state
            if (lastGroupWaitTime < waveInfo.getDefaultDelayBetweenGroups()) { // If this is the first time the group wait time is started
                lastGroupWaitTime += deltaTime; // Set the last group wait time to the current time
            } else {
                lastGroupWaitTime = 0; // Reset the last group wait time
                waveState = EnemyAttackState.SPAWNING; // Change the state to spawning
            }
            return -1; // Placeholder for enemy spawning logic
        }

        if (waveState == EnemyAttackState.WAVE_WAITING) { // If the wave is in wave waiting state
            if (lastWaveWaitTime < waveInfo.getDefaultDelayBetweenWaves()) { // If this is the first time the wave wait time is started
                lastWaveWaitTime += deltaTime; // Set the last wave wait time to the current time
            } else {
                lastWaveWaitTime = 0; // Reset the last wave wait time
                waveState = EnemyAttackState.SPAWNING; // Change the state to spawning
            }
            return -1; // Placeholder for enemy spawning logic
        }

        if (waveState == EnemyAttackState.NO_ENEMY_LEFT) { // If there are no enemies left to spawn
            return -2; // Placeholder for enemy spawning logic
        }
        


        return -1; // Placeholder for enemy spawning logic
    }

}
