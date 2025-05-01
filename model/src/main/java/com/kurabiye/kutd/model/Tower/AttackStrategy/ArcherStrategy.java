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
    // ArcherStrategy class extends the AttackStrategy class
    // This class defines the attack strategy for the Archer tower

    @Override
    public List<Enemy> findTarget(List<Enemy> enemy) {
        return enemy;
    }
}
