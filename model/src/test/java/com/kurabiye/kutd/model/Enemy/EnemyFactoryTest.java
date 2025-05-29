package com.kurabiye.kutd.model.Enemy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.kurabiye.kutd.model.Coordinates.Point2D;
import com.kurabiye.kutd.model.Player.UserPreference;

public class EnemyFactoryTest {

    private EnemyFactory factory;
    private ArrayList<Point2D> path;

    @BeforeEach
    void setUp() {
        path = new ArrayList<>();
        path.add(new Point2D(0, 0));
        factory = EnemyFactory.getInstance();
        factory.setEnemyPath(path);
    }

    @Test
    void testCreateGoblinEnemy() {
        Enemy enemy = factory.createEnemy(EnemyType.GOBLIN);
        assertNotNull(enemy, "Goblin enemy should not be null");
        assertEquals(EnemyType.GOBLIN, enemy.getEnemyType()); 
        assertEquals(path.get(0), enemy.getCoordinate());   
    }

    @Test
    void testCreateKnightEnemy() {
        Enemy enemy = factory.createEnemy(EnemyType.KNIGHT);
        assertNotNull(enemy, "Knight enemy should not be null");
        assertEquals(EnemyType.KNIGHT, enemy.getEnemyType()); 
        assertEquals(path.get(0), enemy.getCoordinate());   
    }

    @Test
    void testEnemyInitialHealthCorrect() {
        Enemy enemy = factory.createEnemy(EnemyType.GOBLIN);
        float expectedHealth = UserPreference.getInstance().getEnemyHealth()[EnemyType.GOBLIN.getValue()];
        assertEquals(expectedHealth, enemy.getHealth(), 0.01f);
    }

    @Test
    void testCreateEnemyInitialPositionCorrect() {
        Point2D start = new Point2D(3, 4);
        ArrayList<Point2D> newPath = new ArrayList<>();
        newPath.add(start);
        factory.setEnemyPath(newPath);

        Enemy enemy = factory.createEnemy(EnemyType.KNIGHT);
        assertEquals(start, enemy.getCoordinate());
    }

    @Test
    void testCreateEnemyWithNullTypeThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createEnemy(null);
        });
    }



}
