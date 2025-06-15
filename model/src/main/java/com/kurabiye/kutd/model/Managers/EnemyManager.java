package com.kurabiye.kutd.model.Managers;


import java.util.ArrayList;
import java.util.List;

import com.kurabiye.kutd.model.Coordinates.Point2D;
import com.kurabiye.kutd.model.Enemy.Enemy;
import com.kurabiye.kutd.model.Enemy.EnemyFactory;
import com.kurabiye.kutd.model.Enemy.IEnemy;
import com.kurabiye.kutd.model.Enemy.Decorators.SynergeticMoveDecorator;
import com.kurabiye.kutd.util.DynamicList.DynamicArrayList;

/**
 * 
 * This class manages the enemies in the game.
 * It handles enemy spawning, movement, and interactions with the game world.
 * It also manages the enemy states and their effects.
 * 
 * @author Atlas Berk Polat
 * @version 1.0
 * @since 2025-06-01
 */

public class EnemyManager {


    private WaveManager waveManager = new WaveManager(); // Wave manager to handle waves of enemies

    private EnemyFactory enemyFactory = EnemyFactory.getInstance(); // Enemy factory for creating enemies


    // Here should be the actual enemy list, which is used to keep track of all enemies in the game

    private DynamicArrayList<IEnemy> enemies = new DynamicArrayList<>(); // List of enemies currently in the game 

    /**
     * Constructor for the EnemyManager class.
     * Initializes the enemy list, wave manager, and enemy factory.
     * 
     * @param enemyPath The path that enemies will follow in the game.
     */
    public EnemyManager(ArrayList<Point2D> enemyPath) {
       
        this.enemyFactory.setEnemyPath(enemyPath);
    }
    /**
     * Moves all enemies in the game based on the elapsed time.
     * 
     * @param deltaTime The time elapsed since the last update.
     * @return The number of enemies that have reached to the end.
     */

    public int moveEnemies(double deltaTime) {

        int arrivedEnemiesCount = 0; // Counter for enemies that have arrived at their destination

        // Iterate through the list of enemies and update their positions

        for (IEnemy enemy : enemies) {

            // check if the enemy is the instance of SynergeticMoveDecorator
            if (enemy instanceof SynergeticMoveDecorator) {
                // Debug message to indicate that a synergetic move decorator is being processed
                //System.out.println("Processing SynergeticMoveDecorator for enemy: " + enemy.getEnemyType());
                // Print the enemy speed for debugging
                //System.out.println("Synergetic Speed: " + enemy.getSpeed());
            }


            enemy.move(deltaTime); // Update the enemy's position based on the delta time

            if (enemy.isDead()) {

                System.out.println("Enemy Manager içerisinde Death");
                // If animated then delete
                if (enemy instanceof Enemy && ((Enemy) enemy).isDeathAnimationPlayed()) {
                    enemies.removeLater(enemy);
                }
                continue; // wait for animation
            }
            if (enemy.hasArrived()) {
                enemies.removeLater(enemy); // Remove the enemy from the list if it has arrived at its destination
                arrivedEnemiesCount++; // Increment the count of arrived enemies
            }
        }

        // Remove enemies that have arrived at their destination
        //enemies.removeCommit();

        return arrivedEnemiesCount; // Return the count of enemies that have arrived at their destinatio
    
}

    /**
     * Spawns enemies based on the current wave and game state.
     * 
     * @param deltaTime The time elapsed since the last update.
     * @return true if there are more enemies to spawn, false if there is none left.
     */
    public boolean spawnEnemies(double deltaTime) {

            // Debug message to indicate that the spawnEnemies method has been called
            //System.out.println("Spawn enemies called with deltaTime: " + deltaTime);
        
            // Using our test enemy code
            int enemyIndex = waveManager.getEnemy(deltaTime); // Get the index of the enemy to spawn
           
            if (enemyIndex > WaveManager.NO_NEW_ENEMY_CODE) {
                Enemy enemy = enemyFactory.createEnemy(enemyIndex); // Create a new enemy using the factory
                enemies.add(enemy); // Add the enemy to the list of enemies
                // Debug message to indicate that an enemy has been spawned
                //System.out.println("Enemy spawned: " + enemyIndex);
                return true; // Return true if an enemy was spawned
            }else if (enemyIndex == WaveManager.NO_NEW_ENEMY_CODE) {
                return true; // Return false if there are no more enemies to spawn
            } 
            else if (enemyIndex == WaveManager.NO_ENEMY_LEFT_CODE) {
                // No enemies left to spawn
                return false; // Return false if there are no more enemies to spawn
            }   


            // Unreachable code, but included for completeness
            return true;
    }


    public WaveManager getWaveManager() {
        return waveManager; // Return the wave manager instance
    }


    public synchronized List<IEnemy> getEnemies() {
        return enemies; // Return the list of enemies
    }

    public DynamicArrayList<IEnemy> getDynamicEnemies() {
        return enemies; // Return the dynamic list of enemies
    }
}
