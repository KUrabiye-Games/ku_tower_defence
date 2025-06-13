package com.kurabiye.kutd.model.Enemy;

import java.util.ArrayList;

import com.kurabiye.kutd.model.Coordinates.Point2D;
import com.kurabiye.kutd.model.Enemy.MoveStrategy.GoblinMoveStrategy;
import com.kurabiye.kutd.model.Enemy.MoveStrategy.KnightMoveStrategy;

/** EnemyFactory.java
 * This class is responsible for creating different enemy objects.
 * It uses the Factory design pattern to create instances of different enemy types.
 * userPreferences.java will help to get the necessary data from the user with regard to enemy health, speed, and kill reward.
 * 
 * 
 * 
 * 
 * @author: Atlas Berk Polat
 * @version: 1.0
 * @since: 2025-04-29
 */

public class EnemyFactory {

    // Static instance for Singleton pattern
    private static EnemyFactory instance;

    private ArrayList<Point2D> enemyPath; // Path for enemies to follow

    
    // Get the singleton instance of EnemyFactory
    public static synchronized EnemyFactory getInstance() {
        if (instance == null) {
            instance = new EnemyFactory();
        }
        return instance;
    }
    

     /**
     * @requires enemyType != null && enemyPath != null && enemyPath.size() > 0
     * @modifies none
     * @effects 
     *    - returns a new Enemy object initialized with values from userPreferences
     *    - assigns appropriate MoveStrategy based on enemyType
     *    - sets initial position of enemy to the first point in enemyPath
     *    - throws IllegalArgumentException if enemyType is invalid
     */

    public Enemy createEnemy(EnemyType enemyType) {
        

        Enemy newEnemy = new Enemy(enemyType);


        switch (enemyType) {
            case EnemyType.GOBLIN:

            newEnemy.setMovePathWithStrategy(enemyPath, new GoblinMoveStrategy());       
                break;

            case EnemyType.KNIGHT:

            newEnemy.setMovePathWithStrategy(enemyPath, new KnightMoveStrategy());

                break;
            default:
                throw new IllegalArgumentException("Invalid enemy type: " + enemyType);

        }

        newEnemy.locate(enemyPath.get(0)); // Set the initial position of the enemy to the first point in the path

        return newEnemy;
    }

    public Enemy createEnemy(int enemyType) {
        return createEnemy(EnemyType.values()[enemyType]);
    }


    // set the enemy path
    public void setEnemyPath(ArrayList<Point2D> enemyPath) {
        this.enemyPath = enemyPath; // Set the path for enemies to follow
    }
    
    
}
