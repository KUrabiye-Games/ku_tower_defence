package com.kurabiye.kutd.model.Managers;


import com.kurabiye.kutd.model.Enemy.IEnemy;
import com.kurabiye.kutd.model.Projectile.DamageType;
import com.kurabiye.kutd.model.Projectile.IProjectile;
import com.kurabiye.kutd.model.Projectile.ProjectileState;
import com.kurabiye.kutd.util.DynamicList.DynamicArrayList;

public class CollisionManager {



    private DynamicArrayList<IEnemy> enemies;
    private DynamicArrayList<IProjectile> projectiles;

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

                for (IEnemy enemy : enemies) {

                    double distance = projectile.getCoordinate().distance(enemy.getCoordinate());
                    float damageRadius = projectile.getProjectileAreaDamage();

                    double distanceToTarget = projectile.getCoordinate().distance(projectile.getTarget());
                    
                    if (distance < damageRadius) { // Check for collision
                       
                        
                        enemy.getDamage(projectile.getProjectileType()); // Apply damage to the enemy
                        
                        if (enemy.isDead()) {
                            int reward = enemy.getKillReward();
                            totalGoldEarned += reward; // Add gold to the total for killing the enemy
                            enemies.removeLater(enemy); // Mark the enemy for removal
                        }
                        collisionOccurred = true; // Set the collision flag to true
                        
                        if(projectile.getProjectileAreaDamage() <= 1f){

                            projectiles.removeLater(projectile);
                            break; // Exit the loop if a collision occurred
                        }
                    }else if(distanceToTarget < deltaTime * projectile.getSpeedVector().magnitude()){
                        // Check if the projectile has reached its target


                             enemy.getDamage(projectile.getProjectileType()); // Apply damage to the enemy
                        
                            if (enemy.isDead()) {
                            int reward = enemy.getKillReward();
                            totalGoldEarned += reward; // Add gold to the total for killing the enemy
                            enemies.removeLater(enemy); // Mark the enemy for removal
                            }
                            collisionOccurred = true; // Set the collision flag to true
                        
                        projectiles.removeLater(projectile); // Remove the projectile if it has reached its target
                            
                        

                    }
                }

                if(collisionOccurred) {
                    projectiles.removeLater(projectile); // Remove the projectile if a collision occurred
                }
            }


            
        // Remove dead enemies from the list
        enemies.removeCommit();
        // Remove dead projectiles from the list
        projectiles.removeCommit();


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
                                enemy.getDamage(projectile.getProjectileType()); // Apply damage to the enemy
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
