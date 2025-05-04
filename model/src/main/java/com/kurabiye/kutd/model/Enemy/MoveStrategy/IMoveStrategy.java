package com.kurabiye.kutd.model.Enemy.MoveStrategy;

import java.util.ArrayList;

import com.kurabiye.kutd.model.Coordinates.Point2D;

public interface IMoveStrategy {

    // This interface defines the move strategy for enemies
    // It contains a method to define how the enemy moves

    ArrayList<Point2D> createMovePath(ArrayList<Point2D> path); // Method to define the movement of the enemy

}
