package com.kurabiye.kutd.model.MapTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.kurabiye.kutd.model.Managers.*;
import com.kurabiye.kutd.model.Tile.*;
import com.kurabiye.kutd.model.Player.*;
import com.kurabiye.kutd.model.Map.*;


public class GameManagerTest {

    private GameManager gameManager;
    

    @BeforeEach
    public void setup() {
        UserPreference.resetInstance();
        UserPreference.applySettings(
            new UserPreference.Builder().setStartingGold(1000)
        );

        GameMap gameMap = GameMap.getPrebuiltMap(); 
        gameManager = new GameManager(gameMap);
        gameManager.startGame(); 

        // Assume (2, 3) is a valid buildable tile
        int tileCode = 15;
        TileFactory tileFactory = new TileFactory();
        Tile tile = tileFactory.create(tileCode);
        gameManager.getGameMap().setTile(2, 3, tile);

    }

    @Test
    public void testBuildTowerValid() {
        boolean result = gameManager.buildTower(2, 3, 0);
        assertTrue(result, "Tower should be built successfully");
        assertTrue(gameManager.getGameMap().getTile(2, 3).getTileCode() >= 20, "Tile should be updated");
        assertEquals(1, gameManager.getTowers().size(), "One tower should be added");
    }

    

    @Test
    public void testBuildTowerOnNonBuildableTile() {
        int tileCode = 0;
        gameManager.getGameMap().setTile(2, 3, new Tile(tileCode));
        boolean result = gameManager.buildTower(2, 3, 0);
        assertFalse(result, "Should fail due to non-buildable tile");
    }

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

    @Test
    public void testBuildTowerWithInvalidTowerType() {
        boolean result = gameManager.buildTower(2, 3, 5);
        assertFalse(result, "Should fail due to invalid tower type");
    }
}



