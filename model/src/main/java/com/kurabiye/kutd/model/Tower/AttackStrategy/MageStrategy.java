package com.kurabiye.kutd.model.Tower.AttackStrategy;

import java.util.List;
import java.util.Random; // Added for random target selection


import com.kurabiye.kutd.model.Enemy.IEnemy;

/**
 * MageStrategy class implements the IAttackStrategy interface.
 * This class defines the attack strategy for the Mage tower.
 * It extends the AttackStrategy class and provides a specific implementation for the Mage tower's attack strategy.
 * 
 * @author Atlas Berk Polat
 * @version 1.0
 * @since 2025-04-25
 */

public class MageStrategy implements IAttackStrategy {
    
    private Random random = new Random(); // Added for random target selection

    /**
     * findTarget method is responsible for finding the target enemy to attack.
     * The specific implementation of the attack strategy will be provided in this method.
     * 
     * @param enemy List of enemies to attack.
     * @return Enemy to be attacked by the Mage tower, or null if no valid target.
     */
    @Override
    public IEnemy findTarget(List<IEnemy> enemies) {
        if (enemies == null || enemies.isEmpty()) {
            return null;
        }
        // Pick a random enemy from the list
        return enemies.get(random.nextInt(enemies.size()));
    }

}
