package com.kurabiye.kutd.model.Tower;

import java.util.ArrayList;
import java.util.List;

import com.kurabiye.kutd.model.Coordinates.Point2D;
import com.kurabiye.kutd.model.Coordinates.TilePoint2D;
import com.kurabiye.kutd.model.Enemy.Enemy;
import com.kurabiye.kutd.model.Projectile.Projectile;
import com.kurabiye.kutd.model.Projectile.ProjectileFactory;
import com.kurabiye.kutd.model.Projectile.Projectile.ProjectileType;
import com.kurabiye.kutd.model.Tower.AttackStrategy.IAttackStrategy;


public class Tower implements ITower{


   
    private float range; // Range of the tower
    private float attackSpeed; // Attack speed of the tower

    private TilePoint2D tileCoordinate; // Coordinate of the tower on the map
    private IAttackStrategy attackStrategy; // Strategy for attacking enemies

    private Point2D attackPoint; // Point where the tower attacks

    private double lastAttackTime; // Time of the last attack
   
    //private int level; // Level of the tower maybe later
    private int sellReturn; // The amount of money returned when the tower is sold

    private ProjectileFactory projectileFactory = ProjectileFactory.getInstance(); // Factory for creating projectiles

    private ProjectileType projectileType; // Type of projectile used by the tower


    public Tower(int sellReturn, float range, float attackSpeed) {
        
        this.sellReturn = sellReturn; // Set the sell return of the tower
        this.range = range; // Set the range of the tower
        this.attackSpeed = attackSpeed; // Set the attack speed of the tower

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



    // and we will need to implement the attack method in the subclasses of the tower class
    public Projectile attack(List<Enemy> enemies, double deltaTime) {
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
        ArrayList<Enemy> filteredEnemies = enemies.stream()
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
            Enemy targetEnemy = attackStrategy.findTarget(filteredEnemies);

            if (targetEnemy == null) {
                return null; // No target found
            }
            
            // Create a projectile using the projectile factory
            // Add console logging for debugging
           
            
            Projectile projectile = projectileFactory.createProjectile(projectileType, attackPoint, targetEnemy.getCoordinate()); // Create a projectile using the factory

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

    public TilePoint2D getTileCoordinate() {
        return tileCoordinate; // Get the tile coordinate of the tower
    }

    /*public void setTileCoordinate(TilePoint2D tileCoordinate) { // In case we add a feature to move the tower
        this.tileCoordinate = tileCoordinate; // Set the tile coordinate of the tower
    }*/


    public synchronized int getSellReturn() {
        return sellReturn; // Get the sell return of the tower
    }

}
