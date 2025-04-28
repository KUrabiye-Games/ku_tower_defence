package com.kurabiye.kutd.model.Enemy.MoveStrategy;

import javafx.geometry.Point2D;

public interface IMoveStrategy {

    // This interface defines the move strategy for enemies
    // It contains a method to define how the enemy moves

    void move(Point2D target); // Method to define the movement of the enemy

}
