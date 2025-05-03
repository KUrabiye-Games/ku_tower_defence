package com.kurabiye.kutd.model.Enemy.MoveStrategy;

import java.util.ArrayList;
import java.util.Random;

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


    Random random = new Random();


    private static final double THRESHOLD = 10; // Threshold for checking if two points are equal

    @Override
    public ArrayList<Point2D> createMovePath(ArrayList<Point2D> path) {


        ArrayList<Point2D> newPath = new ArrayList<Point2D>();



        // Check the tiles as three groups
        // If they are in the same 


        for(int i = 1; i < path.size() - 1; i++) {
            Point2D start = path.get(i -1);
            Point2D mid = path.get(i);
            Point2D end = path.get(i + 1);

            // check if the tiles create a straight line

            Point2D difVect1 = new Point2D(mid.getX() - start.getX(), mid.getY() - start.getY());
            Point2D difVect2 = new Point2D(end.getX() - mid.getX(), end.getY() - mid.getY());

            // check if the tiles create a straight line
            if(difVect1.dotProduct(difVect2) > THRESHOLD) {
                // if they are in the same line, add the middle tile to the new path
                newPath.add(mid);
            } else {
                
                // if not in the same line, find the middle of the end and start tiles

                Point2D mPoint2D = start.midpoint(end);

                // then find the middle of mPoint2D and mid

                // pick a random point between mid and mPoint2D
                // this is the new middle point

                // pick a random number between 0.25 and 0.5
                double randomNum = random.nextDouble() * 0.25 + 0.25;

                Point2D mPoint2D2 = mid.interpolate(mPoint2D, randomNum);

                // add the middle point to the new path

                newPath.add(mPoint2D2);
            }   

        }
        return newPath;
        
    }


}
