package com.kurabiye.kutd.model.Enemy.MoveStrategy;

import javafx.geometry.Point2D;

public class GoblinMoveStrategy implements IMoveStrategy {
    // GoblinMoveStrategy class implements the IMoveStrategy interface
    // This class defines the move strategy for the Goblin enemy

    @Override
    public void move(Point2D target) {
        // Implement the movement logic for Goblin enemy here
        // For example, move towards the target or follow a path
        System.out.println("Goblin is moving!");
    }

}
