package com.kurabiye.kutd.model.Enemy.Decorators;
import com.kurabiye.kutd.model.Enemy.IEnemy;
import com.kurabiye.kutd.model.Player.UserPreference;


/**
 * SynergeticMoveDecorator is a decorator for the IEnemy interface that adds synergetic movement behavior.
 * It extends the EnemyDecorator class to provide additional functionality to the enemy's movement.
 * 
 * @author Atlas Berk Polat
 * @version 1.0
 * @since 2025-05-28
 */

public class SynergeticMoveDecorator extends EnemyDecorator {

    // This class is a decorator for the IEnemy interface
    // It adds synergetic movement behavior to the enemy

    private int goblinSpeed = UserPreference.getInstance().getEnemyMovementSpeed()[0]; // Speed of the goblin
    private int knightSpeed = UserPreference.getInstance().getEnemyMovementSpeed()[1]; // Speed of the knight

    public SynergeticMoveDecorator(IEnemy enemy) {
        super(enemy); // Initialize the decorator with an IEnemy instance

    }

    @Override
    public IEnemy removeDecoration(){
        // This method can be used to remove the decoration and return the original enemy
        return enemy; // Return the original enemy without the synergetic movement behavior
    }

    @Override
    public EffectTypes getEffectType() {
        return EffectTypes.SYNERGYTIC_MOVEMENT; // Return the effect type for this decorator
    }

    @Override
    public int getSpeed() {
        return (int)((goblinSpeed + knightSpeed) / 2); // Return the speed of the enemy after applying synergetic movement
    }

}
