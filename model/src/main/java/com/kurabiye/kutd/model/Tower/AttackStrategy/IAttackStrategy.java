package com.kurabiye.kutd.model.Tower.AttackStrategy;

import com.kurabiye.kutd.model.Enemy.Enemy;
import java.util.List;

/**
 * AttackStrategy interface defines the strategy for attacking enemies.
 * It contains a method to perform the attack on a list of enemies.
 * The strategy also chooses the target enemy to attack. 
 * It might be the one with the lowest health, the closest one, or any other criteria.
 * The specific implementation of the attack strategy will be provided in the classes that implement this interface.
 * 
 * 
 * @author: Atlas Berk Polat
 * @version: 1.0
 * @since: 2025-04-23
 */

public interface IAttackStrategy {
    

    /**
     * Attack method to be implemented by concrete attack strategies.
     * The strategy chooses the target enemy to attack. 
     * It might be the one with the lowest health, the closest one, or any other criteria.
     * @param enemy List of enemies to attack.
     */
    List<Enemy> findTarget(List<Enemy> enemy); 


}
