package com.kurabiye.kutd.model.Tower.AttackStrategy;

import java.util.List;

import com.kurabiye.kutd.model.Enemy.Enemy;

/**
 * MageStrategy class implements the IAttackStrategy interface.
 * This class defines the attack strategy for the Mage tower.
 * It extends the AttackStrategy class and provides a specific implementation for the Mage tower's attack strategy.
 * 
 * @author Atlas Berk Polat
 * @version 1.0
 * @since 2025-04-25
 */

public class ArtilleryStrategy implements IAttackStrategy {


    /**
     * findTarget method is responsible for finding the target enemy to attack.
     * The specific implementation of the attack strategy will be provided in this method.
     * 
     * @param enemy List of enemies to attack.
     * @return List of enemies to be attacked by the Artillery tower.
     */
    @Override
    public List<Enemy> findTarget(List<Enemy> enemy) {
        return enemy;
        // This method returns the list of enemies to be attacked by the Artillery tower
        // The specific implementation of the attack strategy will be provided in this method
    }

}
