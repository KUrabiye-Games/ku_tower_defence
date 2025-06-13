package com.kurabiye.kutd.model.Tower;

import java.util.List;

import com.kurabiye.kutd.model.Coordinates.TilePoint2D;

import com.kurabiye.kutd.model.Enemy.IEnemy;

import com.kurabiye.kutd.model.Projectile.Projectile;

import com.kurabiye.kutd.model.Projectile.ProjectileType;
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
    Projectile attack(List<IEnemy> enemies, double deltaTime);
    
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
     * Gets the type of tower
     * @return  The type of tower
     */
    TowerType getTowerType();
    
    /**
     * Sets the projectile type that this tower will fire
     * 
     * @param projectileType The type of projectile
     */
    void setProjectileType(ProjectileType projectileType);

    /**
     * Gets the projectile type that this tower will fire
     * 
     * @return The type of projectile
     */
    ProjectileType getProjectileType();

    /**
     * Upgrades the tower to the next level
     * 
     * @return true if the upgrade was successful, false otherwise
     */
    boolean upgrade();

    /**
     * Checks if the tower can be upgraded
     * 
     * @return true if the tower can be upgraded, false otherwise
     */
    boolean canUpgrade();


    /**
     * Gets the current level of the tower
     * 
     * @return The current level of the tower
     */
    int getTowerLevel();


    /**
     * Gets the cost to upgrade the tower to the next level
     * 
     * @return The cost of upgrading the tower
     */
    int getUpgradeCost();


    /**
     * Gets the maximum level the tower can reach
     * 
     * @return The maximum level of the tower
     */
    int getMaxLevel();


    /**
     * get the range of the tower
     * 
     * @return The range of the tower
     */
    float getRange();
}