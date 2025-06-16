package com.kurabiye.kutd.model.Projectile;

import com.kurabiye.kutd.model.Coordinates.Point2D;


/**
 * Interface defining the core functionality of a Projectile in the tower defense game.
 * Projectiles are fired by towers and move towards enemies, dealing damage on impact.
 * 
 * @author Atlas Berk Polat
 * @version 1.2
 * @since 2025-05-02
 */
public interface IProjectile {
    
    /**
     * Gets the type of projectile
     * 
     * @return The projectile type
     */
    ProjectileType getProjectileType();
    
    /**
     * Gets the area of damage effect for the projectile
     * 
     * @return The area damage value (radius)
     */
    float getProjectileAreaDamage();
    
    /**
     * Updates the projectile's position based on its speed vector and delta time
     * 
     * @param deltaTime Time passed since last update
     */
    void move(double deltaTime);
    
    /**
     * Gets the current position of the projectile
     * 
     * @return The projectile's coordinate
     */
    Point2D getCoordinate();

    /**
     * Get the speed vector of the projectile
     */
    Point2D getSpeedVector();

    /**
     * get the projectile state
     * 
     * @return the projectile state
     */

     ProjectileState getProjectileState();

     /**
      * get the explosion type

        * @return the explosion type
      */

      DamageType getDamageType();

      /**
       * Get the target coordinate of the projectile
       * 
       * @return The target coordinate
       */
        Point2D getTarget();
    

    /**
     * Get the level of the projectile
     * 
     * @return The level of the projectile
     * */
      public int getProjectileLevel();

      boolean hasExplosionAnimated();
      void setExplosionAnimated(boolean value);


}