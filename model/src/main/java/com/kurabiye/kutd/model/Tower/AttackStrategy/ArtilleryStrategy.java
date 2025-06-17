package com.kurabiye.kutd.model.Tower.AttackStrategy;

import java.util.List;

import com.kurabiye.kutd.model.Enemy.IEnemy;
import com.kurabiye.kutd.model.Coordinates.Point2D; // Required for getCoordinate


/**
 * MageStrategy class implements the IAttackStrategy interface.
 * This class defines the attack strategy for the Mage tower.
 * It extends the AttackStrategy class and provides a specific implementation for the Mage tower's attack strategy.
 * 
 * @author Atlas Berk Polat
 * @version 2.0
 * @since 2025-06-25
 */

public class ArtilleryStrategy implements IAttackStrategy {

    private static final double CLUSTER_RADIUS = 100.0; // Example radius, adjust as needed

    /**
     * findTarget method is responsible for finding the target enemy to attack.
     * It targets the enemy that is part of the largest cluster.
     * 
     * @param enemies List of enemies to attack.
     * @return Enemy to be attacked by the Artillery tower, or null if no valid target.
     */
    @Override
    public IEnemy findTarget(List<IEnemy> enemies) {
        if (enemies == null || enemies.isEmpty()) {
            return null;
        }

        if (enemies.size() == 1) {
            return enemies.get(0); // If only one enemy, target it
        }

        IEnemy bestTarget = null;
        int maxEnemiesInCluster = 0;

        for (IEnemy currentEnemy : enemies) {
            Point2D currentPosition = currentEnemy.getCoordinate();
            int enemiesInProximity = 0;
            for (IEnemy otherEnemy : enemies) {
                if (currentEnemy == otherEnemy) {
                    continue; // Don't count self
                }
                Point2D otherPosition = otherEnemy.getCoordinate();
                if (currentPosition.distance(otherPosition) <= CLUSTER_RADIUS) {
                    enemiesInProximity++;
                }
            }

            if (enemiesInProximity > maxEnemiesInCluster) {
                maxEnemiesInCluster = enemiesInProximity;
                bestTarget = currentEnemy;
            } else if (bestTarget == null && enemiesInProximity == 0) {
                // If no clusters found yet and this enemy is also isolated, pick the first one encountered
                // This handles the case where all enemies are too far apart
                if (bestTarget == null) {
                    bestTarget = currentEnemy;
                }
            }
        }
        
        // If no clusters were found (all enemies are far apart or only one enemy)
        // and bestTarget is still null (e.g. if list had enemies but all were filtered out by some logic not present here)
        // default to the first enemy in the list as a fallback.
        if (bestTarget == null && !enemies.isEmpty()) {
            return enemies.get(0);
        }

        return bestTarget;
    }

}
