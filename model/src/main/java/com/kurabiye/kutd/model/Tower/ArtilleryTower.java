package com.kurabiye.kutd.model.Tower;

import java.util.List;

import com.kurabiye.kutd.model.Enemy.Enemy;

public class ArtilleryTower extends Tower {

    // ArtilleryTower class extends the Tower class
    // This class defines the properties and behavior of the Artillery tower
    // It also includes the range of the artillery shells

    private static final float DEFAULT_ARTILLERY_RANGE = 1.0f; // Default range of the artillery tower
    private float artilleryRange; // Range of the artillery tower
   
    private static final int DEFAULT_COST = 200; // Default cost of the MageTower
    private static final int DEFAULT_SELL_RETURN = 150; // Default sell return of the MageTower
    private static final float[] DEFAULT_ATTACK_POWER = {15.0f, 10.0f}; // Attack power of the MageTower for different enemy types
    private static final float DEFAULT_RANGE = 5.0f; // Default range of the MageTower
    private static final float DEFAULT_ATTACK_SPEED = 1.0f; // Default attack speed of the MageTower

    public ArtilleryTower() {
        super(DEFAULT_COST, DEFAULT_SELL_RETURN, DEFAULT_ATTACK_POWER, DEFAULT_RANGE, DEFAULT_ATTACK_SPEED);
        this.artilleryRange = DEFAULT_ARTILLERY_RANGE; // Set the default artillery range
    }
   

    public ArtilleryTower(int cost, int sellReturn, float[] attackPower, float range, float attackSpeed) {
        super(cost, sellReturn, attackPower, range, attackSpeed);

        this.artilleryRange = DEFAULT_ARTILLERY_RANGE; // Set the default artillery range
    }
    public ArtilleryTower(int cost, int sellReturn, float[] attackPower, float range, float attackSpeed, float artilleryRange) {
        super(cost, sellReturn, attackPower, range, attackSpeed);  
        this.artilleryRange = artilleryRange; // Set the artillery range of the tower
    }

    @Override
    public void attack(List<Enemy> enemies) {
        // Implement the attack logic for ArtilleryTower here
    }

    @Override
    public void sell() {
        // Implement the sell logic for ArtilleryTower here
    }

    public float getArtilleryRange() {
        return artilleryRange; // Get the artillery range of the tower
    }

}
