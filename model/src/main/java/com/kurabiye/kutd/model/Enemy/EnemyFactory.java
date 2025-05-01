package com.kurabiye.kutd.model.Enemy;

import java.util.ArrayList;
import java.util.stream.IntStream;

import com.kurabiye.kutd.model.Enemy.Enemy.EnemyType;
import com.kurabiye.kutd.model.Enemy.MoveStrategy.GoblinMoveStrategy;
import com.kurabiye.kutd.model.Enemy.MoveStrategy.KnightMoveStrategy;
import com.kurabiye.kutd.model.Player.UserPreference;

import javafx.geometry.Point2D;

/* EnemyFactory.java
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

    private static UserPreference userPreferences = UserPreference.getInstance(); // User preferences object
    
    // Pre-defined damage values for different projectile types against different enemy types
    // Format: [ProjectileType][EnemyType]
    private static final float[][] DEFAULT_DAMAGE_VALUES = transposeArray(userPreferences.getDamageDealt());

    private ArrayList<Point2D> enemyPath; // Path for enemies to follow
    
    // Private constructor to prevent instantiation from outside
    private EnemyFactory() {
        // Private constructor for singleton pattern
    }
    
    // Get the singleton instance of EnemyFactory
    public static synchronized EnemyFactory getInstance() {
        if (instance == null) {
            instance = new EnemyFactory();
        }
        return instance;
    }
    

    // Create an enemy of a specific type with default values
    public Enemy createEnemy(EnemyType enemyType) {
        
        Enemy my_enemy = new Enemy(enemyType,
        userPreferences.getEnemyHealth()[enemyType.getValue()],
        userPreferences.getEnemyMovementSpeed()[enemyType.getValue()],
        userPreferences.getGoldPerEnemy()[enemyType.getValue()], 
        DEFAULT_DAMAGE_VALUES[enemyType.getValue()]);

        switch (enemyType) {
            case EnemyType.GOBLIN:

            my_enemy.setMovePathWithStrategy(enemyPath, new GoblinMoveStrategy());       
                break;

            case EnemyType.KNIGHT:

            my_enemy.setMovePathWithStrategy(enemyPath, new KnightMoveStrategy()); 
                break;
            default:
                throw new IllegalArgumentException("Invalid enemy type: " + enemyType);

        }

        return my_enemy;
    }

    public Enemy createEnemy(int enemyType) {
        return createEnemy(EnemyType.values()[enemyType]);
    }


    /**
 * Transposes a 2D array using Java streams
 */
    private static float[][] transposeArray(float[][] original) {
    int rows = original.length;
    int cols = original[0].length;
    
        return IntStream.range(0, cols)
            .mapToObj(col -> IntStream.range(0, rows)
                    .mapToDouble(row -> original[row][col])
                    .toArray())
            .toArray(float[][]::new);
    }

    // set the enemy path
    public void setEnemyPath(ArrayList<Point2D> enemyPath) {
        this.enemyPath = enemyPath; // Set the path for enemies to follow
    }
    
    
}
