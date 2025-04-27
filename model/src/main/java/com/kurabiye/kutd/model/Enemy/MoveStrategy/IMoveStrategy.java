package com.kurabiye.kutd.model.Enemy.MoveStrategy;

import com.kurabiye.kutd.model.Coordinates.Coordinate;

public interface IMoveStrategy {

    // This interface defines the move strategy for enemies
    // It contains a method to define how the enemy moves

    void move(Coordinate target); // Method to define the movement of the enemy

}
