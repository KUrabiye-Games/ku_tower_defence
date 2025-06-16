package com.kurabiye.kutd.model.Managers;


import com.kurabiye.kutd.model.Enemy.IEnemy;
import com.kurabiye.kutd.model.Managers.EffectManagers.SlowDownManager;
import com.kurabiye.kutd.model.Projectile.DamageType;
import com.kurabiye.kutd.model.Projectile.IProjectile;
import com.kurabiye.kutd.model.Projectile.ProjectileState;
import com.kurabiye.kutd.model.Projectile.ProjectileType;
import com.kurabiye.kutd.util.DynamicList.DynamicArrayList;

public class CollisionManager {



    private DynamicArrayList<IEnemy> enemies;
    private DynamicArrayList<IProjectile> projectiles;
    private SlowDownManager slowDownManager; // Manager for slow down effects
    private CollactableManager collectableManager; // Manager for collectable items

    /**
     * Constructor for the CollisionManager class.
     * @param enemies List of enemies in the game.
     * @param projectiles List of projectiles in the game.
     * 
     */
    public CollisionManager(DynamicArrayList<IEnemy> enemies, DynamicArrayList<IProjectile> projectiles) {
        this.enemies = enemies;
        this.projectiles = projectiles;
    }


    public void setSlowDownManager(SlowDownManager slowDownManager) {
        this.slowDownManager = slowDownManager; // Set the slow down manager
    }

    // set the collectable manager
    public void setCollectableManager(CollactableManager collectableManager) {
        // This method can be used to set the collectable manager if needed
        this.collectableManager = collectableManager; // Set the collectable manager
    }
        


    /**
     * Calculate the collisions between projectiles and enemies.
     * This method checks if any projectile collides with any enemy and handles the collision.
     * 
     * @param deltaTime The time elapsed since the last update.
     * @return The amount of gold earned by the player from enemy kills.
     */
    public int calculateCollisions(double deltaTime) {

        int totalGoldEarned = 0; // Total gold earned by the player from enemy kills

        for (IProjectile projectile : projectiles) {
                // Check if any collision occurred

                if (projectile.getProjectileState() == ProjectileState.DEAD) {
                    
                    projectiles.removeLater(projectile); // Remove the projectile if it is dead
                }

                if (projectile.getProjectileState() == ProjectileState.STOPPED) {
                    continue; // Skip if the projectile is stopped
                }

                if (projectile.getProjectileState() == ProjectileState.MOVING) {
                    continue; // Skip if the projectile is dead
                }

                if (projectile.getDamageType() == DamageType.AREA) {
                    continue; // Skip if the projectile is an area explosion
                    
                }

                boolean collisionOccurred = false; // Flag to check if a collision occurred
               
                double distanceToTarget = projectile.getCoordinate().distance(projectile.getTarget());

                for (IEnemy enemy : enemies) {

                    double distance = projectile.getCoordinate().distance(enemy.getCoordinate());
                    float damageRadius = projectile.getProjectileAreaDamage();

                    
                    if (distance < damageRadius) { // Check for collision
                       
                        
                        enemy.getDamage(projectile); // Apply damage to the enemy

                        if (enemy.isDead()) {
                            int reward = enemy.getKillReward();
                            totalGoldEarned += reward; // Add gold to the total for killing the enemy

                            // Spawn a collectable item if the enemy is dead
                            if (collectableManager != null) {

                                double randomValue =  Math.random(); // Generate a random value between 0 and 1
                                // 50% chance to spawn a gold bag
                                if (randomValue < 0.5) { 
                                                collectableManager.spawnGoldBag(enemy.getCoordinate()); // Spawn a gold bag at the enemy's coordinate

                                }

                            }
                            enemies.removeLater(enemy); // Mark the enemy for removal
                        }else if (projectile.getProjectileType() == ProjectileType.MAGIC 
                        && projectile.getProjectileLevel() > 0) {
                            // If the projectile is magic, and its level is greater than 1, apply slow down effect
                            slowDownManager.addEnemyOnEffect(enemy); // Add the enemy to the slow down effect manager
                        }

                        collisionOccurred = true; // Set the collision flag to true
                        
                        if(projectile.getProjectileAreaDamage() <= 1f){

                            projectiles.removeLater(projectile);
                            break; // Exit the loop if a collision occurred
                        }


                        // If the projectile type is magic, there is a chance to teleport the enemy back to the start of the path
                        if (projectile.getProjectileType() == ProjectileType.MAGIC && Math.random() < 0.03) {
      
                                enemy.locateToStartPoint(); // Teleport the enemy back to the start of the path
                            
                        }

                    }else if(distanceToTarget < deltaTime * projectile.getSpeedVector().magnitude()){
                        // Check if the projectile has reached its target

                            projectile.getCoordinate().add(projectile.getTarget().subtract(projectile.getCoordinate()) ); // Move the projectile to its target coordinate
                            // projectile.setProjectileState(ProjectileState.STOPPED); // Set the projectile state to stopped
                    }
                }

                if(collisionOccurred) {
                    projectiles.removeLater(projectile); // Remove the projectile if a collision occurred
                }
            }


            
        // Remove dead enemies from the list
        enemies.commitAll();
        // Remove dead projectiles from the list
        projectiles.commitAll();


        return totalGoldEarned; // Return the total gold earned by the player from enemy kills
    }



    /**
     * 
     */

    public int calculateExplosions(double deltaTime){

        int totalGoldEarned = 0; // Total gold earned by the player from enemy kills

        for (IProjectile projectile : projectiles) {
                if (projectile.getDamageType() == DamageType.AREA) {
                    // check if the projectile is close enough to the its target
                    double distance = projectile.getCoordinate().distance(projectile.getTarget());

                    if (distance < deltaTime * projectile.getSpeedVector().magnitude()) {
                        // Apply area damage to all enemies within the explosion radius
                        for (IEnemy enemy : enemies) {
                            double distanceToEnemy = projectile.getCoordinate().distance(enemy.getCoordinate());
                            if (distanceToEnemy < projectile.getProjectileAreaDamage()) {
                                enemy.getDamage(projectile); // Apply damage to the enemy
                                if (enemy.isDead()) {
                                    int reward = enemy.getKillReward();
                                    totalGoldEarned += reward; // Add gold to the total for killing the enemy
                                    enemies.removeLater(enemy); // Mark the enemy for removal
                                }
                            }
                        }
                        projectiles.removeLater(projectile); // Remove the projectile after the explosion
                    }

                }
            }

        // Remove dead enemies from the list
        enemies.removeCommit();
        // Remove dead projectiles from the list
        projectiles.removeCommit();
        return totalGoldEarned; // Return the total gold earned by the player from enemy kills

    }
    

}
