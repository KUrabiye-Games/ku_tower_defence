package com.kurabiye.kutd.model.Enemy;

import javafx.geometry.Point2D;

public class GoblinEnemy extends Enemy {
    // GoblinEnemy class extends the Enemy class
    // This class represents a goblin enemy in the game

    public GoblinEnemy(int health, int speed, int killReward) {
        super(EnemyType.GOBLIN, health, speed, killReward);
    }

    @Override
    public void move(Point2D target) {
        this.moveStrategy.move(target); // Call the move method of the move strategy
    }

}
