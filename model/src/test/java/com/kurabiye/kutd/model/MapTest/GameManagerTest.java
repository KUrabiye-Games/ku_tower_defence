package com.kurabiye.kutd.model.MapTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.kurabiye.kutd.model.Managers.*;
import com.kurabiye.kutd.model.Tile.*;
import com.kurabiye.kutd.model.Player.*;
import com.kurabiye.kutd.model.Map.*;

/**
 * Test class for the GameManager class.
 * 
 * This class tests the tower-building logic in GameManager using JUnit 5.
 * The tests ensure correct behavior for building towers on valid tiles,
 * preventing tower construction on invalid or non-buildable tiles,
 * handling edge cases such as insufficient gold, and using invalid tower types.
 */
public class GameManagerTest {

    private GameManager gameManager;
    
    /**
     * Sets up the test environment before each test.
     * - Resets user preferences and sets starting gold.
     * - Initializes the GameMap using a predefined layout.
     * - Starts the game.
     * - Sets a buildable tile at position (2, 3) for use in tower tests.
     */
    @BeforeEach
    public void setup() {
        // Reset preferences and set a high starting gold
        UserPreference.resetInstance();
        UserPreference.applySettings(
            new UserPreference.Builder().setStartingGold(1000)
        );
        // Use the prebuilt map and initialize the game manager
        GameMap gameMap = GameMap.getPrebuiltMap(); 
        gameManager = new GameManager(gameMap);
        gameManager.startGame(); 

        // Make sure tile (2, 3) is buildable by setting a buildable tile code
        int tileCode = 15;
        TileFactory tileFactory = new TileFactory();
        Tile tile = tileFactory.create(tileCode);
        gameManager.getGameMap().setTile(2, 3, tile);

    }
    
    /**
     * Test that verifies a tower can be built successfully on a valid tile.
     * - Asserts that the tower is built
     * - Asserts that the tile is updated (likely to reflect tower presence)
     * - Asserts that the tower list in GameManager is updated
     */
    @Test
    public void testBuildTowerValid() {
        boolean result = gameManager.buildTower(2, 3, 0);
        assertTrue(result, "Tower should be built successfully");
        assertTrue(gameManager.getGameMap().getTile(2, 3).getTileCode() >= 20, "Tile should be updated");
        assertEquals(1, gameManager.getTowers().size(), "One tower should be added");
    }

    
    /**
     * Test that verifies tower building fails on a non-buildable tile.
     */
    @Test
    public void testBuildTowerOnNonBuildableTile() {
        int tileCode = 0;
        gameManager.getGameMap().setTile(2, 3, new Tile(tileCode));
        boolean result = gameManager.buildTower(2, 3, 0);
        assertFalse(result, "Should fail due to non-buildable tile");
    }

    /**
     * Test that verifies tower building fails when the player has insufficient gold.
     */
    @Test
    public void testBuildTowerWithInsufficientGold() {
        
        UserPreference.resetInstance();
        UserPreference.applySettings(
            new UserPreference.Builder()
                .setStartingGold(0)
        );

        GameMap gameMap = GameMap.getPrebuiltMap();
        gameManager = new GameManager(gameMap);
        gameManager.startGame();

        boolean result = gameManager.buildTower(2, 3, 0);
        assertFalse(result, "Should fail due to insufficient gold");
    }

    /**
     * Test that verifies tower building fails when an invalid tower type is specified.
     */
    @Test
    public void testBuildTowerWithInvalidTowerType() {
        boolean result = gameManager.buildTower(2, 3, 5);
        assertFalse(result, "Should fail due to invalid tower type");
    }
}



