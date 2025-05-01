package com.kurabiye.kutd.model.Tower;

import java.util.List;

import com.kurabiye.kutd.model.Coordinates.TilePoint2D;
import com.kurabiye.kutd.model.Enemy.Enemy;
import com.kurabiye.kutd.model.Projectile.Projectile;
import com.kurabiye.kutd.model.Tower.AttackStrategy.IAttackStrategy;

public class Tower {


   
    protected float range; // Range of the tower
    protected float attackSpeed; // Attack speed of the tower

    protected TilePoint2D tileCoordinate; // Coordinate of the tower on the map
    protected IAttackStrategy attackStrategy; // Strategy for attacking enemies

    protected int cost; // Cost of the tower
    //private int level; // Level of the tower maybe later
    protected int sellReturn; // The amount of money returned when the tower is sold


    public Tower(int cost, int sellReturn, float range, float attackSpeed) {
        this.cost = cost; // Set the cost of the tower
        this.sellReturn = sellReturn; // Set the sell return of the tower
        this.range = range; // Set the range of the tower
        this.attackSpeed = attackSpeed; // Set the attack speed of the tower
    }

    public void setAttackStrategy(IAttackStrategy attackStrategy) {
        this.attackStrategy = attackStrategy; // Set the attack strategy
    }
    public IAttackStrategy getAttackStrategy() {
        return attackStrategy; // Get the attack strategy
    }

    // and we will need to implement the attack method in the subclasses of the tower class
    public Projectile attack(List<Enemy> enemies){
        // Use the attack strategy to find the target enemy
        Enemy targetEnemy = attackStrategy.findTarget(enemies);
        // Implement the attack logic here
        // For example, iterate through the target enemies and apply damage
        // create a projectile and send it to the target enemy

        new Projectile = 
    }

    public TilePoint2D getTileCoordinate() {
        return tileCoordinate; // Get the tile coordinate of the tower
    }

    /*public void setTileCoordinate(TilePoint2D tileCoordinate) { // In case we add a feature to move the tower
        this.tileCoordinate = tileCoordinate; // Set the tile coordinate of the tower
    }*/

}
