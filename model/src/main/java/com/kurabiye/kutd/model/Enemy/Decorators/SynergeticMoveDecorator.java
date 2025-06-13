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

public class SynergeticMoveDecorator extends AbstractEffect implements ISpeedDecorator {

    private static final int TILE_SIZE = 120; // Size of the tile in pixels, used for calculating movement


    private static final int targetSpeed = ((int) (UserPreference.getInstance().getKnightSpeed() + UserPreference.getInstance().getKnightSpeed() / 2)); // Target speed for synergetic movement

    IEnemy subject;
    IEnemy partner;
 
    public SynergeticMoveDecorator(IEnemy subject, IEnemy partner) {
        super(AbstractEffect.INFINITY_EFFECT_DURATION); // Set the duration for the synergetic movement effec
        this.subject = subject; // The subject is the enemy that is being decorated
        this.partner = partner; // The partner is the enemy that is being synergetically moved with
    }

    @Override
    public int getSpeed(int currentSpeed) {
        // This method returns the speed of the enemy after applying the synergetic movement effect
        return targetSpeed; // Set the speed to the target speed for synergetic movement
    }

    @Override
    public EffectTypes getEffectType() {
        return EffectTypes.SYNERGYTIC_MOVEMENT;
    }

    @Override
    public void update(double deltaTime) {
        // Update the remaining effect time
        if (remainingEffectTime > 0) {
            remainingEffectTime -= deltaTime; // Update the remaining effect time based on the elapsed time
        }
        
       // Check the distance between the subject and partner
        double distance = subject.getCoordinate().distance(partner.getCoordinate());
        
        // If the distance is greater than a certain threshold, move the subject towards the partner
        if (distance > TILE_SIZE) {
            remainingEffectTime = 0;
            
        }
    }

    // Two synergetic affects would be the same if they have the same subject and partner
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Check if the same object
        if (!(obj instanceof SynergeticMoveDecorator)) return false; // Check if the object is an instance of SynergeticMoveDecorator
        SynergeticMoveDecorator other = (SynergeticMoveDecorator) obj; // Cast the object to SynergeticMoveDecorator
        return subject.equals(other.subject) && partner.equals(other.partner); // Check if both subject and partner are equal
    }

    // applyPriority is not used in this decorator, so it returns 0
    @Override
    public int getPriority() {
        return 100; // Default priority is 0, as this decorator does not have a specific priority
    }



}
