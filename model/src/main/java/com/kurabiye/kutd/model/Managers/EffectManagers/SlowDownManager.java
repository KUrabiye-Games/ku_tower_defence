package com.kurabiye.kutd.model.Managers.EffectManagers;

import com.kurabiye.kutd.model.Enemy.Enemy;
import com.kurabiye.kutd.model.Enemy.IEnemy;
import com.kurabiye.kutd.model.Enemy.Decorators.EnemyDecorator;
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

       
    }


    public void addEnemyOnEffect(IEnemy enemy) {

        // check if the enemey has a decorator

        if (enemy instanceof EnemyDecorator) {
            SlowDownDecorator slowDownDecorator = new SlowDownDecorator();
            ((EnemyDecorator)enemy).addEffect(slowDownDecorator);
            

        }else {
            // If the enemy is not decorated, decorate it with the slow down effect
            EnemyDecorator decoratedEnemy = new EnemyDecorator(enemy);
            SlowDownDecorator slowDownDecorator = new SlowDownDecorator();
            decoratedEnemy.addEffect(slowDownDecorator);
            enemies.addLater(decoratedEnemy); // Add the decorated enemy to the list of enemies
            enemies.removeLater(enemy); // Remove the original enemy from the list of enemies
        }
        
    }

}