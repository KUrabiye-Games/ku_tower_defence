package com.kurabiye.kutd.model.Tower;

import java.util.List;

import com.kurabiye.kutd.model.Coordinates.TilePoint2D;
import com.kurabiye.kutd.model.Enemy.Enemy;
import com.kurabiye.kutd.model.Projectile.Projectile;
import com.kurabiye.kutd.model.Projectile.Projectile.ProjectileType;
import com.kurabiye.kutd.model.Tower.AttackStrategy.IAttackStrategy;

/**
 * Interface defining the core functionality of a Tower in the tower defense game.
 * Towers are placed on the map and attack enemies within range.
 * 
 * @author Atlas Berk Polat
 * @version 1.2
 * @since 2025-05-02
 */
public interface ITower {
    
    /**
     * Attacks enemies within range and creates a projectile
     * 
     * @param enemies List of enemies that could be targeted
     * @param deltaTime Time passed since last update
     * @return Projectile created by the attack or null if no attack was performed
     */
    Projectile attack(List<Enemy> enemies, double deltaTime);
    
    /**
     * Gets the tower's position on the map
     * 
     * @return The tile coordinates of the tower
     */
    TilePoint2D getTileCoordinate();
    
    /**
     * Sets the tower's position on the map
     * 
     * @param tileCoordinate The tile coordinates to place the tower
     */
    void setTileCoordinate(TilePoint2D tileCoordinate);
    
    /**
     * Gets the amount of gold returned when selling the tower
     * 
     * @return The sell return value
     */
    int getSellReturn();
    
    /**
     * Sets the attack strategy for the tower
     * 
     * @param attackStrategy The strategy to use for attacking enemies
     */
    void setAttackStrategy(IAttackStrategy attackStrategy);
    
    /**
     * Gets the attack strategy of the tower
     * 
     * @return The tower's attack strategy
     */
    IAttackStrategy getAttackStrategy();
    
    /**
     * Sets the projectile type that this tower will fire
     * 
     * @param projectileType The type of projectile
     */
    void setProjectileType(ProjectileType projectileType);
}