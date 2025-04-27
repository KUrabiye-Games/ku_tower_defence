package com.kurabiye.kutd.model.Tower;

import java.util.List;

import com.kurabiye.kutd.model.Enemy.Enemy;

public class ArtilleryTower extends Tower {
   
    private static final int DEFAULT_COST = 200; // Default cost of the MageTower
    private static final int DEFAULT_SELL_RETURN = 150; // Default sell return of the MageTower
    private static final float[] DEFAULT_ATTACK_POWER = {15.0f, 10.0f}; // Attack power of the MageTower for different enemy types
    private static final float DEFAULT_RANGE = 5.0f; // Default range of the MageTower
    private static final float DEFAULT_ATTACK_SPEED = 1.0f; // Default attack speed of the MageTower

    public ArtilleryTower() {
        super(DEFAULT_COST, DEFAULT_SELL_RETURN, DEFAULT_ATTACK_POWER, DEFAULT_RANGE, DEFAULT_ATTACK_SPEED);
    }
   

    public ArtilleryTower(int cost, int sellReturn, float[] attackPower, float range, float attackSpeed) {
        super(cost, sellReturn, attackPower, range, attackSpeed);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void attack(List<Enemy> enemies) {
        // Implement the attack logic for ArtilleryTower here
    }

    @Override
    public void sell() {
        // Implement the sell logic for ArtilleryTower here
    }

}
