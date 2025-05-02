package com.kurabiye.kutd.model.Enemy.MoveStrategy;

import java.util.ArrayList;

import com.kurabiye.kutd.model.Coordinates.Point2D;


/* GoblinMoveStrategy.java
 * This class implements the IMoveStrategy interface and defines the move strategy for the Goblin enemy.
 * It currently does not modify the path, but can be extended in the future.
 * I leave it as non-singleton for now, but it can be changed to singleton if needed.
 * But I believe we can add more stateful methods to this class in the future.
 * 
 * 
 * @author: Atlas Berk Polat
 * @version: 1.0
 * @since: 2025-05-01
 * 
 */

public class GoblinMoveStrategy implements IMoveStrategy {

    @Override
    public ArrayList<Point2D> createMovePath(ArrayList<Point2D> path) {
        return path;
    }
    // GoblinMoveStrategy class implements the IMoveStrategy interface
    // This class defines the move strategy for the Goblin enemy



}
