package com.kurabiye.kutd.model.Enemy.Decorators;

import com.kurabiye.kutd.model.Enemy.IEnemy;


/**
 * Slowdown is a decorator for the IEnemy interface that adds synergetic movement behavior.
 * It extends the EnemyDecorator class to provide additional functionality to the enemy's movement.
 * 
 * @author Atlas Berk Polat
 * @version 1.0
 * @since 2025-04-23
 */

public class SlowDownDecorator extends EnemyDecorator {

    // This class is a decorator for the IEnemy interface
    // It adds synergetic movement behavior to the enemy

    public SlowDownDecorator(IEnemy enemy) {
        super(enemy); // Initialize the decorator with an IEnemy instance
        this.remainigEffectTime = 4.0; // Set the remaining effect time to 5 seconds
        
    }

    @Override
    public void move(double deltaTime) {
        // Implement synergetic movement logic here
        // For example, modify the enemy's move direction based on some conditions
        enemy.move(deltaTime); // Call the original move method from the decorated enemy
        remainigEffectTime -= deltaTime; // Decrease the remaining effect time

    }

    public boolean isEffectActive() {
        return remainigEffectTime > 0; // Check if the effect is still active
    }

    @Override
    public IEnemy removeDecoration(){
        // This method can be used to remove the decoration and return the original enem
        return enemy; // Return the original enemy without the synergetic movement behavior
    }

    @Override
    public EffectTypes getEffectType() {
        return EffectTypes.SLOW_DOWN; // Return the effect type for this decorator
    }

    @Override
    public int getSpeed() {
        if (remainigEffectTime > 0) {
            return (int)(super.getSpeed() * 0.8); // Return the original speed if the effect is not active
        }
        return super.getSpeed(); // Return the original speed if the effect is not active
    }

}
