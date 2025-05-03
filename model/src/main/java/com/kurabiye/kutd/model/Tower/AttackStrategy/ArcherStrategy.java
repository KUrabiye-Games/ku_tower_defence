package com.kurabiye.kutd.model.Tower.AttackStrategy;

import java.util.List;

import com.kurabiye.kutd.model.Enemy.Enemy;


/**
 * ArcherStrategy class implements the IAttackStrategy interface.
 * This class defines the attack strategy for the Archer tower.
 * It extends the AttackStrategy class and provides a specific implementation for the Archer tower's attack strategy.
 * 
 * @author Atlas Berk Polat
 * @version 1.0
 * @since 2025-04-25
 */

public class ArcherStrategy implements IAttackStrategy {
    /**
     * findTarget method is responsible for finding the target enemy to attack.
     * The specific implementation of the attack strategy will be provided in this method.
     * 
     * @param enemy List of enemies to attack.
     * @return Enemy to be attacked by the tower, or null if no valid target.
     */
    @Override
    public Enemy findTarget(List<Enemy> enemies) {
        if (enemies == null || enemies.isEmpty()) {
            return null;
        }
        // For now, just return the first enemy in the list
        // In a more advanced implementation, this could select the enemy closest to the end
        // or with the lowest health, etc.
        return enemies.get(0);
    }
}
