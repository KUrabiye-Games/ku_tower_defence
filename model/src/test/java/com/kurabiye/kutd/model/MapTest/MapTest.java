package com.kurabiye.kutd.model.MapTest;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;



import com.kurabiye.kutd.model.Map.GameMap;
import com.kurabiye.kutd.model.Tile.Tile;
import com.kurabiye.kutd.model.Tile.TileFactory;
import com.kurabiye.kutd.model.Coordinates.TilePoint2D;

public class MapTest {

    
    
    private static TileFactory tileFactory;
    
    @BeforeAll
    public static void setUp() {
        tileFactory = new TileFactory();
    }
    
    @Test
    public void testMapInitialization() {
        GameMap map = new GameMap(); // Create a map of size 10x10
        assertNotNull(map, "Map should be initialized");
    }


    @Test
    public void testValidGameMap_StartTileNotOnEdge() {
        // Test when starting tile is not on the edge
        Tile[][] tiles = createBasicValidMap();
        TilePoint2D start =  tiles[4][5].getCoordinate();
        TilePoint2D end =  tiles[4][15].getCoordinate();
        
        GameMap map = new GameMap(tiles, start, end);
        assertFalse(map.isValidGameMap(), "Map with start tile not on edge should be invalid");
    }
 
    @Test
    public void testValidGameMap_EndTileNotOnEdge() {
        // Test when ending tile is not on the edge
        Tile[][] tiles = createBasicValidMap();
        TilePoint2D start =  tiles[4][0].getCoordinate();
        TilePoint2D end =  tiles[4][10].getCoordinate();
        
        GameMap map = new GameMap(tiles, start, end);
        assertFalse(map.isValidGameMap(), "Map with end tile not on edge should be invalid");
    }
  
    @Test
    public void testValidGameMap_StartAndEndSameTile() {
        // Test when start and end tiles are the same
        Tile[][] tiles = createBasicValidMap();
         TilePoint2D start =  tiles[4][0].getCoordinate();
        TilePoint2D end =  tiles[4][0].getCoordinate();
        
        GameMap map = new GameMap(tiles, start, end);
        assertFalse(map.isValidGameMap(), "Map with same start and end tiles should be invalid");
    }
              /*  
    @Test
    public void testValidGameMap_StartTileNotPath() {
        // Test when starting tile is not a path tile
        Tile[][] tiles = createBasicValidMap();
        tiles[4][0] = tileFactory.create(5); // Ground tile instead of path
        TilePoint2D start = new TilePoint2D(0, 4); // Left edge
        TilePoint2D end = new TilePoint2D(15, 4); // Right edge
        
        GameMap map = new GameMap(tiles, start, end);
        assertFalse(map.isValidGameMap(), "Map with non-path start tile should be invalid");
    }
 
    @Test
    public void testValidGameMap_EndTileNotPath() {
        // Test when ending tile is not a path tile
        Tile[][] tiles = createBasicValidMap();
        tiles[4][15] = tileFactory.create(5); // Ground tile instead of path
        TilePoint2D start = new TilePoint2D(0, 4); // Left edge
        TilePoint2D end = new TilePoint2D(15, 4); // Right edge
        
        GameMap map = new GameMap(tiles, start, end);
        assertFalse(map.isValidGameMap(), "Map with non-path end tile should be invalid");
    }
    
    @Test
    public void testValidGameMap_InsufficientBuildableTiles() {
        // Test when there are less than 4 buildable tiles
        Tile[][] tiles = createBasicValidMap();
        // Replace some buildable tiles with ground tiles
        for (int i = 0; i < GameMap.MAP_HEIGHT; i++) {
            for (int j = 0; j < GameMap.MAP_WIDTH; j++) {
                if (tiles[i][j].isBuildableTile()) {
                    tiles[i][j] = tileFactory.create(5); // Ground tile
                }
            }
        }
        // Add only 3 buildable tiles
        tiles[1][1] = tileFactory.create(15);
        tiles[1][2] = tileFactory.create(15);
        tiles[1][3] = tileFactory.create(15);
        
        TilePoint2D start = new TilePoint2D(0, 4); // Left edge
        TilePoint2D end = new TilePoint2D(15, 4); // Right edge
        
        GameMap map = new GameMap(tiles, start, end);
        assertFalse(map.isValidGameMap(), "Map with less than 4 buildable tiles should be invalid");
    }
    
    @Test
    public void testValidGameMap_ExactlyFourBuildableTiles() {
        // Test when there are exactly 4 buildable tiles
        Tile[][] tiles = createBasicValidMap();
        // Replace all buildable tiles with ground tiles
        for (int i = 0; i < GameMap.MAP_HEIGHT; i++) {
            for (int j = 0; j < GameMap.MAP_WIDTH; j++) {
                if (tiles[i][j].isBuildableTile()) {
                    tiles[i][j] = tileFactory.create(5); // Ground tile
                }
            }
        }
        // Add exactly 4 buildable tiles
        tiles[1][1] = tileFactory.create(15);
        tiles[1][2] = tileFactory.create(15);
        tiles[1][3] = tileFactory.create(15);
        tiles[1][4] = tileFactory.create(15);
        
        TilePoint2D start = new TilePoint2D(0, 4); // Left edge
        TilePoint2D end = new TilePoint2D(15, 4); // Right edge
        
        GameMap map = new GameMap(tiles, start, end);
        assertTrue(map.isValidGameMap(), "Map with exactly 4 buildable tiles should be valid");
    }
    
    @Test
    public void testValidGameMap_DisconnectedPath() {
        // Test when path is disconnected
        Tile[][] tiles = createBasicValidMap();
        // Break the path by replacing a middle tile with ground
        tiles[4][8] = tileFactory.create(5); // Ground tile breaks path
        
        TilePoint2D start = new TilePoint2D(0, 4); // Left edge
        TilePoint2D end = new TilePoint2D(15, 4); // Right edge
        
        GameMap map = new GameMap(tiles, start, end);
        assertFalse(map.isValidGameMap(), "Map with disconnected path should be invalid");
    }
    
    @Test
    public void testValidGameMap_NullTiles() {
        // Test when tiles array is null
        GameMap map = new GameMap(null, new TilePoint2D(0, 4), new TilePoint2D(15, 4));
        assertFalse(map.isValidGameMap(), "Map with null tiles should be invalid");
    }
    
    @Test
    public void testValidGameMap_NullStartCoordinates() {
        // Test when start coordinates are null
        Tile[][] tiles = createBasicValidMap();
        GameMap map = new GameMap(tiles, null, new TilePoint2D(15, 4));
        assertFalse(map.isValidGameMap(), "Map with null start coordinates should be invalid");
    }
    
    @Test
    public void testValidGameMap_NullEndCoordinates() {
        // Test when end coordinates are null
        Tile[][] tiles = createBasicValidMap();
        GameMap map = new GameMap(tiles, new TilePoint2D(0, 4), null);
        assertFalse(map.isValidGameMap(), "Map with null end coordinates should be invalid");
    }
    
    @Test
    public void testValidGameMap_TopEdgeStart() {
        // Test with start tile on top edge
        Tile[][] tiles = createVerticalPathMap();
        TilePoint2D start = new TilePoint2D(8, 0); // Top edge
        TilePoint2D end = new TilePoint2D(8, 8); // Bottom edge
        
        GameMap map = new GameMap(tiles, start, end);
        assertTrue(map.isValidGameMap(), "Map with start on top edge should be valid");
    }
    
    @Test
    public void testValidGameMap_BottomEdgeEnd() {
        // Test with end tile on bottom edge
        Tile[][] tiles = createVerticalPathMap();
        TilePoint2D start = new TilePoint2D(8, 0); // Top edge
        TilePoint2D end = new TilePoint2D(8, 8); // Bottom edge
        
        GameMap map = new GameMap(tiles, start, end);
        assertTrue(map.isValidGameMap(), "Map with end on bottom edge should be valid");
    }
    
    @Test
    public void testValidGameMap_InvalidWideStructure() {
        // Test with invalid wide structure (24-25-28-29 configuration)
        Tile[][] tiles = createBasicValidMap();
        // Place tile 24 at edge where it can't have proper configuration
        tiles[8][15] = tileFactory.create(24); // Can't have proper configuration at edge
        
        TilePoint2D start = new TilePoint2D(0, 4); // Left edge
        TilePoint2D end = new TilePoint2D(15, 4); // Right edge
        
        GameMap map = new GameMap(tiles, start, end);
        assertFalse(map.isValidGameMap(), "Map with invalid wide structure should be invalid");
    }
    
    @Test
    public void testValidGameMap_ValidWideStructure() {
        // Test with valid wide structure (24-25-28-29 configuration)
        Tile[][] tiles = createBasicValidMap();
        // Place proper 2x2 configuration
        tiles[1][1] = tileFactory.create(24);
        tiles[1][2] = tileFactory.create(25);
        tiles[2][1] = tileFactory.create(28);
        tiles[2][2] = tileFactory.create(29);
        
        TilePoint2D start = new TilePoint2D(0, 4); // Left edge
        TilePoint2D end = new TilePoint2D(15, 4); // Right edge
        
        GameMap map = new GameMap(tiles, start, end);
        assertTrue(map.isValidGameMap(), "Map with valid wide structure should be valid");
    }
    
    @Test
    public void testValidGameMap_OutOfBoundsCoordinates() {
        // Test with coordinates out of bounds
        //Tile[][] tiles = createBasicValidMap();
        
        assertThrows(IllegalArgumentException.class, () -> {
            new TilePoint2D(-1, 4); // Negative coordinates should throw exception
        }, "Negative coordinates should throw exception");
        
        assertThrows(IllegalArgumentException.class, () -> {
            new TilePoint2D(16, 4); // X coordinate out of bounds
        }, "X coordinate out of bounds should throw exception");
        
        assertThrows(IllegalArgumentException.class, () -> {
            new TilePoint2D(4, 9); // Y coordinate out of bounds
        }, "Y coordinate out of bounds should throw exception");
    }
    
    @Test
    public void testPrebuiltMapIsValid() {
        // Test that the prebuilt map is valid
        GameMap prebuiltMap = GameMap.getPrebuiltMap();
        assertNotNull(prebuiltMap, "Prebuilt map should not be null");
        assertTrue(prebuiltMap.isValidGameMap(), "Prebuilt map should be valid");
    }
    
    @Test
    public void testValidGameMap_PathConnectivity() {
        // Test path connectivity with a more complex path
        Tile[][] tiles = createComplexValidMap();
        TilePoint2D start = new TilePoint2D(0, 1); // Left edge
        TilePoint2D end = new TilePoint2D(15, 7); // Right edge
        
        GameMap map = new GameMap(tiles, start, end);
        assertTrue(map.isValidGameMap(), "Complex valid map should return true");
    }
    
    @Test
    public void testValidGameMap_CornerStartEnd() {
        // Test with start and end at corners
        Tile[][] tiles = createCornerPathMap();
        TilePoint2D start = new TilePoint2D(0, 0); // Top-left corner
        TilePoint2D end = new TilePoint2D(15, 8); // Bottom-right corner
        
        GameMap map = new GameMap(tiles, start, end);
        assertTrue(map.isValidGameMap(), "Map with corner start/end should be valid");
    }
    
    @Test
    public void testMapEditorRequirements() {
        // This test verifies all the map editor requirements mentioned in COMP 302 assignment
        
        // 1. The starting tile of the path is at the edge of the screen and marked as starting point
        Tile[][] tiles = createBasicValidMap();
        TilePoint2D edgeStart = new TilePoint2D(0, 4); // Left edge
        TilePoint2D centerStart = new TilePoint2D(5, 4); // Not on edge
        TilePoint2D validEnd = new TilePoint2D(15, 4); // Right edge
        
        GameMap validMap = new GameMap(tiles, edgeStart, validEnd);
        assertTrue(validMap.isValidGameMap(), "Map with edge start should be valid");
        
        GameMap invalidMap = new GameMap(tiles, centerStart, validEnd);
        assertFalse(invalidMap.isValidGameMap(), "Map with center start should be invalid");
        
        // 2. The ending tile of the path is at the edge of the screen and marked as ending point
        TilePoint2D edgeEnd = new TilePoint2D(15, 4); // Right edge
        TilePoint2D centerEnd = new TilePoint2D(10, 4); // Not on edge
        
        validMap = new GameMap(tiles, edgeStart, edgeEnd);
        assertTrue(validMap.isValidGameMap(), "Map with edge end should be valid");
        
        invalidMap = new GameMap(tiles, edgeStart, centerEnd);
        assertFalse(invalidMap.isValidGameMap(), "Map with center end should be invalid");
        
        // 3. The path is fully connected and does not contain discontinuities
        Tile[][] disconnectedTiles = createBasicValidMap();
        disconnectedTiles[4][8] = tileFactory.create(5); // Break the path
        
        GameMap disconnectedMap = new GameMap(disconnectedTiles, edgeStart, edgeEnd);
        assertFalse(disconnectedMap.isValidGameMap(), "Map with disconnected path should be invalid");
        
        // 4. There are at least four tiles with empty lots on which towers can be built during play
        Tile[][] insufficientBuildableTiles = createBasicValidMap();
        // Remove all buildable tiles
        for (int i = 0; i < GameMap.MAP_HEIGHT; i++) {
            for (int j = 0; j < GameMap.MAP_WIDTH; j++) {
                if (insufficientBuildableTiles[i][j].isBuildableTile()) {
                    insufficientBuildableTiles[i][j] = tileFactory.create(5); // Ground tile
                }
            }
        }
        // Add only 3 buildable tiles (less than required 4)
        insufficientBuildableTiles[1][1] = tileFactory.create(15);
        insufficientBuildableTiles[1][2] = tileFactory.create(15);
        insufficientBuildableTiles[1][3] = tileFactory.create(15);
        
        GameMap insufficientMap = new GameMap(insufficientBuildableTiles, edgeStart, edgeEnd);
        assertFalse(insufficientMap.isValidGameMap(), "Map with less than 4 buildable tiles should be invalid");
        
        // Valid map should have all requirements met
        assertTrue(validMap.isValidGameMap(), "Map meeting all requirements should be valid");
    }
    
    @Test
    public void testValidGameMap_AllEdgesStartEnd() {
        // Test all possible edge positions for start and end tiles
        
        // Top edge start to bottom edge end
        Tile[][] verticalTiles = createVerticalPathMap();
        GameMap topToBottom = new GameMap(verticalTiles, new TilePoint2D(8, 0), new TilePoint2D(8, 8));
        assertTrue(topToBottom.isValidGameMap(), "Top to bottom path should be valid");
        
        // Left edge start to right edge end (already tested in basic test)
        Tile[][] horizontalTiles = createBasicValidMap();
        GameMap leftToRight = new GameMap(horizontalTiles, new TilePoint2D(0, 4), new TilePoint2D(15, 4));
        assertTrue(leftToRight.isValidGameMap(), "Left to right path should be valid");
        
        // Corner combinations
        Tile[][] cornerTiles = createCornerPathMap();
        GameMap cornerPath = new GameMap(cornerTiles, new TilePoint2D(0, 0), new TilePoint2D(15, 8));
        assertTrue(cornerPath.isValidGameMap(), "Corner to corner path should be valid");
    }
    
    @Test
    public void testValidGameMap_EdgeValidation() {
        // Test edge validation logic more thoroughly
        Tile[][] tiles = createBasicValidMap();
        
        // Test all combinations of invalid positions (not on edges)
        TilePoint2D[] invalidPositions = {
            new TilePoint2D(1, 1), // Center
            new TilePoint2D(5, 3), // Center
            new TilePoint2D(8, 4), // Center
            new TilePoint2D(1, 7), // Not on any edge
        };
        
        TilePoint2D validEdgePosition = new TilePoint2D(0, 4); // Left edge
        
        for (TilePoint2D invalidPos : invalidPositions) {
            // Test invalid start position
            GameMap mapInvalidStart = new GameMap(tiles, invalidPos, validEdgePosition);
            assertFalse(mapInvalidStart.isValidGameMap(), 
                "Map with start at (" + invalidPos.getTileX() + "," + invalidPos.getTileY() + ") should be invalid");
            
            // Test invalid end position
            GameMap mapInvalidEnd = new GameMap(tiles, validEdgePosition, invalidPos);
            assertFalse(mapInvalidEnd.isValidGameMap(), 
                "Map with end at (" + invalidPos.getTileX() + "," + invalidPos.getTileY() + ") should be invalid");
        }
    }
    
    @Test
    public void testValidGameMap_BuildableTilesCounting() {
        // Test the exact counting of buildable tiles
        Tile[][] tiles = createBasicValidMap();
        
        // Count buildable tiles in our test map
        int buildableCount = 0;
        for (int i = 0; i < GameMap.MAP_HEIGHT; i++) {
            for (int j = 0; j < GameMap.MAP_WIDTH; j++) {
                if (tiles[i][j].isBuildableTile()) {
                    buildableCount++;
                }
            }
        }
        
        assertTrue(buildableCount >= 4, "Test map should have at least 4 buildable tiles");
        
        TilePoint2D start = new TilePoint2D(0, 4);
        TilePoint2D end = new TilePoint2D(15, 4);
        GameMap map = new GameMap(tiles, start, end);
        assertTrue(map.isValidGameMap(), "Map with " + buildableCount + " buildable tiles should be valid");
        
        // Test edge case: exactly 4 buildable tiles
        Tile[][] exactFourTiles = new Tile[GameMap.MAP_HEIGHT][GameMap.MAP_WIDTH];
        
        // Fill with ground tiles
        for (int i = 0; i < GameMap.MAP_HEIGHT; i++) {
            for (int j = 0; j < GameMap.MAP_WIDTH; j++) {
                exactFourTiles[i][j] = tileFactory.create(5); // Ground tile
            }
        }
        
        // Create path
        for (int j = 0; j < GameMap.MAP_WIDTH; j++) {
            exactFourTiles[4][j] = tileFactory.create(12); // Horizontal path
        }
        exactFourTiles[4][0] = tileFactory.create(2); // Start tile
        exactFourTiles[4][15] = tileFactory.create(1); // End tile
        
        // Add exactly 4 buildable tiles
        exactFourTiles[1][1] = tileFactory.create(15);
        exactFourTiles[1][2] = tileFactory.create(15);
        exactFourTiles[6][1] = tileFactory.create(15);
        exactFourTiles[6][2] = tileFactory.create(15);
        
        GameMap exactFourMap = new GameMap(exactFourTiles, start, end);
        assertTrue(exactFourMap.isValidGameMap(), "Map with exactly 4 buildable tiles should be valid");
    }

*/  
    private static final int[][] map = {
        { 5, 5, 5, 5, 16, 5, 17, 5, 5, 5, 24, 25, 7, 5, 5, 19 },
        { 0, 1, 2, 5, 0, 1, 2, 5, 5, 18, 28, 29, 6, 23, 16, 5 },
        { 4, 15, 7, 15, 7, 22, 8, 13, 13, 9, 1, 9, 10, 5, 5, 5 },
        { 8, 2, 8, 9, 10, 5, 5, 5, 5, 5, 5, 5, 5, 17, 27, 5 },
        { 5, 7, 19, 18, 5, 5, 5, 0, 1, 2, 15, 5, 5, 31, 5, 5},
        { 5, 7, 5, 5, 15, 0, 13, 10, 15, 8, 13, 2, 5, 5, 5, 5 },
        { 5, 4, 5, 0, 13, 10, 0, 1, 2, 5, 30, 6, 5, 5, 5, 5 },
        { 23, 7, 15, 7, 5, 5, 4, 18, 8, 13, 13, 10, 16, 5, 18, 5 },
        { 5, 8, 13, 10, 5, 5, 7, 5, 5, 5, 5, 5, 5, 5, 5, 5 }
    };
 
 

    private Tile[][] createBasicValidMap() {

        int MAP_WIDTH = 16; // Width of the map
        int MAP_HEIGHT = 9; // Height of the map

        Tile[][] tiles = new Tile[MAP_HEIGHT][MAP_WIDTH]; // Initialize the tiles array
        TileFactory tileFactory = new TileFactory();

        // Create the tiles and set their properties
        for (int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                Tile tile = tileFactory.create(map[i][j]); // Create a new tile with code using factory
                tile.setCoordinate(new TilePoint2D(j, i)); // Set coordinates for the tile
                tiles[i][j] = tile;
            }
        }

        return tiles; // Return the static map
    }


    // Helper method to create a map with vertical path
    private Tile[][] createVerticalPathMap() {
        Tile[][] tiles = new Tile[GameMap.MAP_HEIGHT][GameMap.MAP_WIDTH];
        
        // Fill map with ground tiles
        for (int i = 0; i < GameMap.MAP_HEIGHT; i++) {
            for (int j = 0; j < GameMap.MAP_WIDTH; j++) {
                tiles[i][j] = tileFactory.create(5); // Ground tile
            }
        }
        
        // Create vertical path in the middle column (x=8)
        for (int i = 0; i < GameMap.MAP_HEIGHT; i++) {
            if (i == 0) {
                tiles[i][8] = tileFactory.create(3); // Path tile going down (1->3)
            } else if (i == GameMap.MAP_HEIGHT - 1) {
                tiles[i][8] = tileFactory.create(4); // Path tile going from up (1->3)
            } else {
                tiles[i][8] = tileFactory.create(11); // Vertical path tile (1->3)
            }
        }
        
        // Add buildable tiles (at least 4)
        tiles[1][1] = tileFactory.create(15);
        tiles[1][2] = tileFactory.create(15);
        tiles[2][1] = tileFactory.create(15);
        tiles[2][2] = tileFactory.create(15);
        tiles[6][6] = tileFactory.create(15);
        tiles[6][7] = tileFactory.create(15);
        
        return tiles;
    }
    
    // Helper method to create a complex valid map with curved path
    private Tile[][] createComplexValidMap() {
        Tile[][] tiles = new Tile[GameMap.MAP_HEIGHT][GameMap.MAP_WIDTH];
        
        // Fill map with ground tiles
        for (int i = 0; i < GameMap.MAP_HEIGHT; i++) {
            for (int j = 0; j < GameMap.MAP_WIDTH; j++) {
                tiles[i][j] = tileFactory.create(5); // Ground tile
            }
        }
        
        // Create L-shaped path: start at (0,1), go right to (5,1), then down to (5,7), then right to (15,7)
        // Start tile
        tiles[1][0] = tileFactory.create(2); // Going right
        
        // Horizontal segment
        for (int j = 1; j < 5; j++) {
            tiles[1][j] = tileFactory.create(12); // Horizontal path
        }
        
        // Corner tile
        tiles[1][5] = tileFactory.create(9); // Right to down
        
        // Vertical segment  
        for (int i = 2; i < 7; i++) {
            tiles[i][5] = tileFactory.create(11); // Vertical path
        }
        
        // Corner tile
        tiles[7][5] = tileFactory.create(1); // Down to right
        
        // Final horizontal segment
        for (int j = 6; j < 15; j++) {
            tiles[7][j] = tileFactory.create(12); // Horizontal path
        }
        
        // End tile
        tiles[7][15] = tileFactory.create(1); // From left
        
        // Add buildable tiles
        for (int i = 3; i < 6; i++) {
            for (int j = 7; j < 10; j++) {
                tiles[i][j] = tileFactory.create(15);
            }
        }
        
        return tiles;
    }
    
    // Helper method to create a map with corner path
    private Tile[][] createCornerPathMap() {
        Tile[][] tiles = new Tile[GameMap.MAP_HEIGHT][GameMap.MAP_WIDTH];
        
        // Fill map with ground tiles
        for (int i = 0; i < GameMap.MAP_HEIGHT; i++) {
            for (int j = 0; j < GameMap.MAP_WIDTH; j++) {
                tiles[i][j] = tileFactory.create(5); // Ground tile
            }
        }
        
        // Create diagonal-like path from top-left to bottom-right
        // Start at top-left corner
        tiles[0][0] = tileFactory.create(9); // Down and right
        
        // Go right along top edge
        for (int j = 1; j < 8; j++) {
            tiles[0][j] = tileFactory.create(12); // Horizontal path
        }
        
        // Turn down
        tiles[0][8] = tileFactory.create(9); // Right to down
        
        // Go down
        for (int i = 1; i < 8; i++) {
            tiles[i][8] = tileFactory.create(11); // Vertical path
        }
        
        // Turn right
        tiles[8][8] = tileFactory.create(1); // Down to right
        
        // Go right to end
        for (int j = 9; j < 15; j++) {
            tiles[8][j] = tileFactory.create(12); // Horizontal path
        }
        
        // End at bottom-right
        tiles[8][15] = tileFactory.create(1); // From left
        
        // Add buildable tiles
        tiles[2][2] = tileFactory.create(15);
        tiles[2][3] = tileFactory.create(15);
        tiles[3][2] = tileFactory.create(15);
        tiles[3][3] = tileFactory.create(15);
        tiles[5][5] = tileFactory.create(15);
        tiles[6][6] = tileFactory.create(15);
        
        return tiles;
    }

}
