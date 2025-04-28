package com.kurabiye.kutd.model.Enemy.MoveStrategy;

import javafx.geometry.Point2D;

public class KnightMoveStrategy implements IMoveStrategy {
    // KnightMoveStrategy class implements the IMoveStrategy interface
    // This class defines the move strategy for the Knight enemy

    @Override
    public void move(Point2D target) {
        // Implement the movement logic for Knight enemy here
        // For example, move towards the target or follow a path
        System.out.println("Knight is moving!");
    }

}
