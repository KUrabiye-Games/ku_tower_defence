package com.kurabiye.kutd.model.Wave;

import java.util.ArrayList;
import java.util.Arrays;

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
    


    private int currentWave; // Current wave number
    private int currentGroup; // Current group number
    


    public WaveInfo(){
        this.userPreferences = UserPreference.getInstance(); // Get the singleton instance of UserPreference

        this.waveDecomposition = userPreferences.getWaveList(); // Get the wave decomposition from user preferences
        
        this.defaultDelayBetweenWaves = userPreferences.getDelayBetweenWaves(); // Get the delay between waves from user preferences
        this.defaultDelayBetweenGroups = userPreferences.getDelayBetweenGroups(); // Get the delay between groups from user preferences

        this.currentWave = 0; // Initialize current wave to 0
        this.currentGroup = 0; // Initialize current group to 0
    }

 
    public int getDefaultDelayBetweenWaves() {
        return defaultDelayBetweenWaves; // Return the default delay between waves
    }
    public int getDefaultDelayBetweenGroups() {
        return defaultDelayBetweenGroups; // Return the default delay between groups
    }
   
    public int getCurrentWave() {
        return currentWave; // Return the current wave number
    }
    public int getCurrentGroup() {
        return currentGroup; // Return the current group number
    }

    public int getCurrentWaveNumber() {
        return this.currentWave; // Return the current wave number  
    }

    public int getCurrentGroupNumber() {
        return this.currentGroup; // Return the current group number
    }
    public int[] getCurrentEnemyDecomposition() {
        return this.waveDecomposition.get(currentWave).get(currentGroup); // Return the current enemy decomposition
    }

    public int getTotalNumberOfWaves() {
        return waveDecomposition.size(); // Return the total number of waves from user preferences
    }

    public int getTotalNumberOfGroupsInCurrentWave() {
        return waveDecomposition.get(currentWave).size(); // Return the total number of groups in the current wave
    }
    public int getTotalNumberOfEnemiesInCurrentWave() {
        return this.waveDecomposition.get(currentWave).stream().flatMapToInt(Arrays::stream).sum(); // Return the total number of enemies in the current wave
    }
    
    public int getTotalNumberOfEnemiesInCurrentGroup() {
        return Arrays.stream(this.waveDecomposition.get(currentWave).get(currentGroup)).sum(); // Return the total number of groups in the current wave
    }


    public boolean nextWave() {
        // check if the current wave is done
        // all groups in the current wave are done

        if (currentGroup == waveDecomposition.get(currentWave).size() - 1) { // Check if the current group is the last one in the current wave
            currentGroup = 0; // Reset current group to 0
        } else {
            return false; // Return false if the current group is not the last one in the current wave
        }

        if (currentWave < waveDecomposition.size() - 1) { // Check if there are more waves
            currentWave++; // Move to the next wave
            currentGroup = 0; // Reset current group to 0
            return true; // Return true if there is a next wave
        } else {
            return false; // Return false if there are no more waves
        }
    }

    public boolean nextGroup() {
        if (currentGroup < waveDecomposition.get(currentWave).size() - 1) { // Check if there are more groups in the current wave
            currentGroup++; // Move to the next group
            return true; // Return true if there is a next group
        } else {
            return false; // Return false if there are no more groups in the current wave
        }
    }




    







}
