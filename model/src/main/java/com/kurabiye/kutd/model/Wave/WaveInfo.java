package com.kurabiye.kutd.model.Wave;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

import com.kurabiye.kutd.model.Player.UserPreference;

/*  WaveInfo.java
 *  This class contains information about the waves in the game.
 *  Each instance will be used in a new game session.
 * 
 * 
 * @author: Atlas Berk Polat
 * @version: 1.0
 * @since: 2025-04-29
 */

public class WaveInfo {

    /*
     * 
     * private int numberOfWaves; // Total number of waves in the game
    private int numberOfGroupsPerWave; // Number of groups per wave
    private int numberOfEnemiesPerGroup; // Number of enemies per group
    private int delayBetweenWaves; // Delay between waves in milliseconds
    private int delayBetweenGroups; // Delay between groups in milliseconds
    private int[] enemyComposition; // Composition of types of enemies for a given group or wave
     */

    private UserPreference userPreferences; // Singleton instance of UserPreference

     private ArrayList<ArrayList<int[]>> waveDecomposition; // Number of groups per wave // Composition of types of enemies for a given group or wave
    
    private int defaultDelayBetweenWaves; // Default delay between waves in milliseconds
    private int defaultDelayBetweenGroups; // Default delay between groups in milliseconds
    


    public WaveInfo(UserPreference userPreferences) {
        this.userPreferences = userPreferences; // Get the singleton instance of UserPreference

        this.waveDecomposition = userPreferences.getWaveList(); // Get the wave decomposition from user preferences
        
        this.defaultDelayBetweenWaves = userPreferences.getDelayBetweenWaves(); // Get the delay between waves from user preferences
        this.defaultDelayBetweenGroups = userPreferences.getDelayBetweenGroups(); // Get the delay between groups from user preferences

    }

 
    public int getDefaultDelayBetweenWaves() {
        return defaultDelayBetweenWaves; // Return the default delay between waves
    }
    public int getDefaultDelayBetweenGroups() {
        return defaultDelayBetweenGroups; // Return the default delay between groups
    }

    public int getTotalNumberOfWaves() {
        return waveDecomposition.size(); // Return the total number of waves from user preferences
    }

    public int getTotalNumberOfGroupsInWave(int waveIndex) {
        return waveDecomposition.get(waveIndex).size(); // Return the total number of groups in the specified wave
    }

    public int[] getWaveGroupDecomposition(int waveIndex, int groupIndex) {
        return waveDecomposition.get(waveIndex).get(groupIndex).clone(); // Return the decomposition of the specified wave and group clone it to avoid modification
    }

    public int getTotalEnemyInGroup(int waveIndex, int groupIndex) {
        return Arrays.stream(waveDecomposition.get(waveIndex).get(groupIndex)).sum(); // Return the total number of enemies in the specified group
    }

    public int getTotalEnemyInWave(int waveIndex) {
        return IntStream.range(0, waveDecomposition.get(waveIndex).size())
                .map(i -> getTotalEnemyInGroup(waveIndex, i)).sum(); // Return the total number of enemies in the specified wave
    }
}
