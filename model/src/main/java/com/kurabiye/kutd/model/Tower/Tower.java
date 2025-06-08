package com.kurabiye.kutd.model.Tower;

import java.util.ArrayList;
import java.util.List;

import com.kurabiye.kutd.model.Coordinates.Point2D;
import com.kurabiye.kutd.model.Coordinates.TilePoint2D;
import com.kurabiye.kutd.model.Enemy.IEnemy;
import com.kurabiye.kutd.model.Player.UserPreference;
import com.kurabiye.kutd.model.Projectile.Projectile;

import com.kurabiye.kutd.model.Projectile.ProjectileFactory;
import com.kurabiye.kutd.model.Projectile.ProjectileType;
import com.kurabiye.kutd.model.Tower.AttackStrategy.IAttackStrategy;


/* * Tower.java
 *  This class represents a tower in the tower defense game.
 * It handles the tower's attributes, attack strategy, and projectile creation.
 * 
 * 
 * @author Atlas Berk Polat
 * @version 2.0
 * @since 2025-05-13
 */


public class Tower implements ITower{


    private static final UserPreference userPreferences = UserPreference.getInstance(); // User preferences for tower construction costs

   
    private float range; // Range of the tower
    private float attackSpeed; // Attack speed of the tower

    private TilePoint2D tileCoordinate; // Coordinate of the tower on the map
    private IAttackStrategy attackStrategy; // Strategy for attacking enemies

    private Point2D attackPoint; // Point where the tower attacks

    private double lastAttackTime; // Time of the last attack
   
    private ProjectileFactory projectileFactory = ProjectileFactory.getInstance(); // Factory for creating projectiles

    private ProjectileType projectileType; // Type of projectile used by the tower

    // Level up

    private int towerLevel = 1; // Level of the tower, default is 1
    private int maxLevel = 2; // Maximum level of the tower, can be changed later

    private TowerType towerType; // Type of the tower, can be used for different tower types


    public Tower(TowerType towerType) {
        this.range = userPreferences.getTowerEffectiveRange()[towerType.getValue()][0]; // Set the range of the tower
        this.attackSpeed = userPreferences.getTowerRateOfFire()[towerType.getValue()][0]; // Set the attack speed of the tower

    }

    public void setTileCoordinate(TilePoint2D tileCoordinate) {
        this.tileCoordinate = tileCoordinate; // Set the tile coordinate of the tower
        this.attackPoint = new Point2D(tileCoordinate.getCenter().getX(), (tileCoordinate.getCenter().getY() - TilePoint2D.getTileHeight() / 4 )); // Update the attack point of the tower
    }


    public void setProjectileType(ProjectileType projectileType) {
        this.projectileType = projectileType; // Set the projectile type
    }

    public ProjectileType getProjectileType() {
        return projectileType; // Get the projectile type
    }

    public void setAttackStrategy(IAttackStrategy attackStrategy) {
        this.attackStrategy = attackStrategy; // Set the attack strategy
    }
    public IAttackStrategy getAttackStrategy() {
        return attackStrategy; // Get the attack strategy
    }



    /**
     *
     * This method attacks the enemies within the tower's range.
     * It uses the attack strategy to find the target enemy and creates a projectile to attack that enemy.
     * @param enemies The list of enemies to attack.
     * @param deltaTime The time passed since the last attack.
     * @return A Projectile object representing the attack, or null if no attack can be made.
     */
    @Override
    public Projectile attack(List<IEnemy> enemies, double deltaTime) {
        // Use the attack strategy to find the target enemy

        lastAttackTime += deltaTime; // Update the last attack time
        if (lastAttackTime < attackSpeed) {
            return null; // Not enough time has passed to attack
        }
        lastAttackTime = 0; // Reset the last attack time
        
        // Guard against empty lists
        if (enemies == null || enemies.isEmpty()) {
            return null; // No enemies to attack
        }

        // Filter the enemies based on the range of the tower
        ArrayList<IEnemy> filteredEnemies = enemies.stream()
                .filter(enemy -> enemy.getCoordinate().distance(tileCoordinate.getCenter()) <= range)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        // If no enemies in range, return null
        if (filteredEnemies.isEmpty()) {
            return null; // No enemies in range
        }
        
        // Check if attackStrategy is null
        if (attackStrategy == null) {
           
            return null;
        }
        
        try {
            IEnemy targetEnemy = attackStrategy.findTarget(filteredEnemies);

            if (targetEnemy == null) {
                return null; // No target found
            }
            
            // Create a projectile using the projectile factory
            // Add console logging for debugging
           
            
            Projectile projectile = projectileFactory.createProjectile(projectileType, attackPoint, targetEnemy.getCoordinate(), towerLevel); // Create a projectile using the factory

            // Check if the projectile's speed vector is zero

            if (projectile.getSpeedVector().magnitude() == 0) {
              
                return null; // No attack
            }
            return projectile; // Return the created projectile 
        } catch (Exception e) {
          
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the tile coordinate of the tower.
     * 
     * @return The tile coordinate of the tower.
     */

    public TilePoint2D getTileCoordinate() {
        return tileCoordinate; // Get the tile coordinate of the tower
    }
    /**
     * Gets the range of the tower.
     * 
     * @return The range of the tower.
     */
    public synchronized int getSellReturn() {
        // calculate the total money spend to build and upgrade the tower
        int totalCost = 0;
        for (int i = 1; i <= towerLevel; i++) {
            totalCost += userPreferences.getTowerConstructionCost()[towerType.getValue()][i]; // Add the construction cost for each level
        }

        return (int) (totalCost * userPreferences.getTowerSellReturn()[towerType.getValue()]); // Return 50% of the total cost as the sell return
    }

    @Override
    public boolean upgrade() {
        if (canUpgrade()) {
            towerLevel++; // Increment the tower level
            return true; // Return true to indicate successful upgrade
        }

        return false; // Return false if the tower cannot be upgraded
    }

    @Override
    public boolean canUpgrade() {
        // Check if the tower can be upgraded based on its current level and maximum level
        return towerLevel < maxLevel; // Return true if the tower can be upgraded, false otherwise
    }

    @Override
    public int getTowerLevel() {
        return towerLevel; // Get the current level of the tower
    }

    @Override
    public int getUpgradeCost() {
        if (towerLevel < maxLevel) {
            return UserPreference.getInstance().getTowerConstructionCost()[towerType.getValue()][towerLevel + 1]; // Get the upgrade cost for the next level
            
        }
        return -1; // Return 0 if the tower is already at maximum level
    }

    @Override
    public int getMaxLevel() {
        return maxLevel; // Get the maximum level of the tower
    }

    @Override
    public TowerType getTowerType() {
        return towerType; // Get the type of the tower
    }

}
