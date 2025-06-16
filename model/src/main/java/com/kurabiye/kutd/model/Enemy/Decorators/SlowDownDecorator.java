package com.kurabiye.kutd.model.Enemy.Decorators;


/**
 * Slowdown is a decorator for the IEnemy interface that adds synergetic movement behavior.
 * It extends the EnemyDecorator class to provide additional functionality to the enemy's movement.
 * 
 * @author Atlas Berk Polat
 * @version 1.0
 * @since 2025-04-23
 */

public class SlowDownDecorator extends AbstractEffect implements ISpeedDecorator {

    
 
    public SlowDownDecorator() {
        super(4); // Set the duration for the synergetic movement effec
    }

    @Override
    public int getSpeed(int currentSpeed) {
        // This method returns the speed of the enemy after applying the synergetic movement effect
        return ((int)(currentSpeed * 0.8)); // Set the speed to the target speed for synergetic movement
    }

    @Override
    public EffectTypes getEffectType() {
        return EffectTypes.SLOW_DOWN;
    }

    

    // Two slowdonw affect would be the same no matter how many times it is applied
    @Override
    public boolean equals(Object obj) {
        // Check if the object is an instance of SlowDownDecorator
        return obj instanceof SlowDownDecorator;
    }

    @Override
    public int getPriority() {
        // Slow down effect has the highest priority
        return 0; // Return a high priority value for the slow down effect
    }

}
