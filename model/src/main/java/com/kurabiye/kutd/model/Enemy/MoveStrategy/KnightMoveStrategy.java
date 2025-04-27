package com.kurabiye.kutd.model.Enemy.MoveStrategy;

import com.kurabiye.kutd.model.Coordinates.Coordinate;

public class KnightMoveStrategy implements IMoveStrategy {
    // KnightMoveStrategy class implements the IMoveStrategy interface
    // This class defines the move strategy for the Knight enemy

    @Override
    public void move(Coordinate target) {
        // Implement the movement logic for Knight enemy here
        // For example, move towards the target or follow a path
        System.out.println("Knight is moving!");
    }

}
