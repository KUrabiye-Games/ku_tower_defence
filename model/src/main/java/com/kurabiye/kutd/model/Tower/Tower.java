package com.kurabiye.kutd.model.Tower;

import com.kurabiye.kutd.model.Coordinates.TileCoordinate;
import com.kurabiye.kutd.model.Tower.AttackStrategy.AttackStrategy;

public abstract class Tower {

    private TileCoordinate tileCoordinate; // Coordinate of the tower on the map
    private AttackStrategy attackStrategy; // Strategy for attacking enemies

    private int cost; // Cost of the tower
    //private int level; // Level of the tower maybe later
    private int sellReturn; // The amount of money returned when the tower is sold


    public void setAttackStrategy(AttackStrategy attackStrategy) {
        this.attackStrategy = attackStrategy; // Set the attack strategy
    }
    public AttackStrategy getAttackStrategy() {
        return attackStrategy; // Get the attack strategy
    }

    public abstract void attack(); // Abstract method for attacking
    // public abstract void upgrade(); // Abstract method for upgrading the tower maybe later

    public abstract void sell(); // Abstract method for selling the tower

    public TileCoordinate getTileCoordinate() {
        return tileCoordinate; // Get the tile coordinate of the tower
    }

    /*public void setTileCoordinate(TileCoordinate tileCoordinate) { // In case we add a feature to move the tower
        this.tileCoordinate = tileCoordinate; // Set the tile coordinate of the tower
    }*/

}
