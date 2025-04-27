package com.kurabiye.kutd.model.Enemy.MoveStrategy;

import com.kurabiye.kutd.model.Coordinates.Coordinate;

public class GoblinMoveStrategy implements IMoveStrategy {
    // GoblinMoveStrategy class implements the IMoveStrategy interface
    // This class defines the move strategy for the Goblin enemy

    @Override
    public void move(Coordinate target) {
        // Implement the movement logic for Goblin enemy here
        // For example, move towards the target or follow a path
        System.out.println("Goblin is moving!");
    }

}
