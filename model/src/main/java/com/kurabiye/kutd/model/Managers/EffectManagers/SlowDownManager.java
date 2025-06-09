package com.kurabiye.kutd.model.Managers.EffectManagers;

import com.kurabiye.kutd.model.Enemy.IEnemy;
import com.kurabiye.kutd.model.Enemy.Decorators.SlowDownDecorator;
import com.kurabiye.kutd.util.DynamicList.DynamicArrayList;

public class SlowDownManager {


    private DynamicArrayList<IEnemy> enemies;


    /**
     * Constructor for the SlowDown class.
     * Initializes the list of enemies.
     *
     * @param enemies List of enemies in the game.
     */
    public SlowDownManager(DynamicArrayList<IEnemy> enemies) {
        this.enemies = enemies; // Set the list of enemies
    }



    private DynamicArrayList<SlowDownDecorator> onEffectEnemies = new DynamicArrayList<>(); // List of enemies that are currently affected by the slow down effect

    /**
     * Applies the slow down effect to the enemies.
     * This method iterates through the list of enemies and applies the slow down effect based on their states.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    public void applySlowDown(double deltaTime) {

        for (SlowDownDecorator enemy : onEffectEnemies) {
                if (enemy.isOver()) {

                    IEnemy originalEnemy = enemy.removeDecoration(); // Remove the slow down effect and get the original enemy
                    enemies.remove(originalEnemy); // Remove the original enemy from the list of enemies
                    onEffectEnemies.removeLater(enemy);
                    enemies.add(originalEnemy); // Add the original enemy back to the list of enemies
                }
        }

        onEffectEnemies.removeCommit();
    }


    public void addEnemyOnEffect(IEnemy enemy) {
        if (enemy != null && !onEffectEnemies.contains(enemy)) {

            // Put the decorator here
            SlowDownDecorator slowedEnemy = new SlowDownDecorator(enemy); // Apply the slow down decorator to the enemy

            onEffectEnemies.add(slowedEnemy); // Add the enemy to the list of enemies affected by the slow down effect

            enemies.remove(enemy); // Remove the original enemy from the list of enemies
            enemies.add(slowedEnemy); // Add the slowed enemy to the list of enemies
        }
    }

}