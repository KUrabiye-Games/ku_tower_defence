package com.kurabiye.kutd.model.Enemy;

import javafx.geometry.Point2D;

public class KnightEnemy extends Enemy {
    // KnightEnemy class extends the Enemy class
    // This class represents a knight enemy in the game

    public KnightEnemy(int health, int speed, int killReward) {
        super(EnemyType.KNIGHT, health, speed, killReward);
    }

    @Override
    public void move(Point2D target) {
        this.moveStrategy.move(target); // Call the move method of the move strategy
    }

}
