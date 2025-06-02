package com.kurabiye.kutd.model.MapTest;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;


import com.kurabiye.kutd.model.Map.GameMap;
import com.kurabiye.kutd.model.Tile.Tile;
import com.kurabiye.kutd.model.Tile.TileFactory;
import com.kurabiye.kutd.model.Coordinates.TilePoint2D;


/**
 * MapTest.java
 * 
 * This class contains unit tests for the GameMap class isValidMap method.
 * It tests functionalities such as map initialization, path validation,
 * and edge cases for start and end tiles.
 * 
 * @author Atlas Berk Polat
 * @version 1.0
 * @since 2025-06-01
 */

public class MapTest {

    
    
    private static TileFactory tileFactory;
    
    /*
     * Sets up the TileFactory before all tests are run.
     * This method is executed once before any test methods in this class.
     * It initializes the tileFactory instance which is used to create Tile objects
     * This ensures that all tests have access to a properly initialized TileFactory
     * 
     */
    @BeforeAll
    public static void setUp() {
        tileFactory = new TileFactory();
    }

    /**
     * Test to ensure that the GameMap can be initialized correctly.
     * This test checks if a GameMap object can be created without any exceptions.
     */
    
    @Test
    public void testMapInitialization() {
        GameMap map = new GameMap();
        assertNotNull(map, "Map should be initialized");
    }


    /**
     * Test with valid game map
     * This test checks if the map is valid when the starting and ending tiles are on the edges.
     * 
     */
    @Test
    public void testValidGameMap_StartTileNotOnEdge() {
        // Test when starting tile is not on the edge
        Tile[][] tiles = createBasicValidMap();
        TilePoint2D start =  tiles[4][5].getCoordinate();
        TilePoint2D end =  tiles[4][15].getCoordinate();
        


        assertEquals(GameMap.isValidGameMap(tiles, start, end), "Starting tile is not on the edge of the map", "Starting tile is not on the edge of the map");
    }
    /**
     * Test with valid game map
     * This test checks if the map is valid when the ending tile is not on the edge.
     * 
     */
    @Test
    public void testValidGameMap_EndTileNotOnEdge() {
        // Test when ending tile is not on the edge
        Tile[][] tiles = createBasicValidMap();
        TilePoint2D start =  tiles[4][0].getCoordinate();
        TilePoint2D end =  tiles[4][10].getCoordinate();
        
        assertEquals(GameMap.isValidGameMap(tiles, start, end), "Ending tile is not on the edge of the map", "Ending tile is not on the edge of the map");
    }
   /**
    * Test with valid game map
    * This test checks if the map is valid when the starting and ending tiles are the same.
    */
    @Test
    public void testValidGameMap_StartAndEndSameTile() {
        // Test when start and end tiles are the same
        Tile[][] tiles = createBasicValidMap();
        TilePoint2D start =  tiles[4][0].getCoordinate();
        TilePoint2D end =  tiles[4][0].getCoordinate();
        
        assertEquals(GameMap.isValidGameMap(tiles, start, end), "Starting and ending tiles are the same", "Starting and ending tiles are the same");
    }
    /**
     * Test with valid game map
     * This test checks if the map is valid when the starting tile is not a path tile.
     * 
     */
    @Test
    public void testValidGameMap_StartTileNotPath() {
        // Test when starting tile is not a path tile
        Tile[][] tiles = createBasicValidMap();
        tiles[8][6] = tileFactory.create(5); // Ground tile instead of path
        TilePoint2D start = new TilePoint2D(6, 8); // Left edge
        TilePoint2D end = new TilePoint2D(12, 0); // Right edge
        
        assertEquals(GameMap.isValidGameMap(tiles, start, end), "Starting tile is not a path tile", "Starting tile is not a path tile");

    }
 
    /**
     * Test with valid game map
     * This test checks if the map is valid when the ending tile is not a path tile.
     * 
     */
    @Test
    public void testValidGameMap_EndTileNotPath() {
        // Test when ending tile is not a path tile
        Tile[][] tiles = createBasicValidMap();
        tiles[0][12] = tileFactory.create(5); // Ground tile instead of path
        TilePoint2D start = new TilePoint2D(6, 8); // Left edge
        TilePoint2D end = new TilePoint2D(12, 0); // Right edge
        
        assertEquals(GameMap.isValidGameMap(tiles, start, end), "Ending tile is not a path tile", "Ending tile is not a pathXTGStile");

    }

    /**
     * Test with valid game map
     * This test checks if the map is valid when there are not enough buildable tiles.
     * 
     */
    
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
        
        TilePoint2D start = new TilePoint2D(6, 8); // Left edge
        TilePoint2D end = new TilePoint2D(12, 0); // Right edge
        

        assertEquals(GameMap.isValidGameMap(tiles, start, end), "Insufficient buildable tiles", "Insufficient buildable tiles");
    }
    /**
     * Test with valid game map
     * This test checks if the map is valid when there are exactly 4 buildable tiles.
     * 
     */
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
        
        TilePoint2D start = new TilePoint2D(6, 8); // Left edge
        TilePoint2D end = new TilePoint2D(12, 0); // Right edge
        

        assertNotEquals(GameMap.isValidGameMap(tiles, start, end), "Insufficient buildable tiles", "Insufficient buildable tiles");
    }

    /**
     * Test with valid game map
     * This test checks if the map is not valid when the path is disconnected.
     * 
     */

    
    @Test
    public void testValidGameMap_DisconnectedPath() {
        // Test when path is disconnected
        Tile[][] tiles = createBasicValidMap();
        // Break the path by replacing a middle tile with ground
        tiles[6][7] = tileFactory.create(5); // Ground tile breaks path
        
        TilePoint2D start = new TilePoint2D(6, 8); // Left edge
        TilePoint2D end = new TilePoint2D(12, 0); // Right edge
        
        assertEquals(GameMap.isValidGameMap(tiles, start, end), "Tile path is disconnected or has an error", "\"Tile path is disconnected or has an error");

    }

    /**
     * Test with null tiles
     * This test checks if the map is valid when some tiles are null.
     * 
     */
    
    @Test
    public void testValidGameMap_NullTiles() {
        // Test when path is disconnected
        Tile[][] tiles = createBasicValidMap();
        // Break the path by replacing a middle tile with ground
        tiles[6][7] = null; // Ground tile breaks path
        
        TilePoint2D start = new TilePoint2D(6, 8); // Left edge
        TilePoint2D end = new TilePoint2D(12, 0); // Right edge
        
        assertEquals(GameMap.isValidGameMap(tiles, start, end), "Some tile pieces are Null", "Some tile pieces are Null");
    }
    /**
     * Test with null start coordinates
     * This test checks if the map is valid when the start coordinates are null.
     * 
     */
    @Test
    public void testValidGameMap_NullStartCoordinates() {
        // Test when start coordinates are null
        Tile[][] tiles = createBasicValidMap();
       
        TilePoint2D start = null; // Left edge
        TilePoint2D end = new TilePoint2D(12, 0); // Right edge
        
        assertEquals(GameMap.isValidGameMap(tiles, start, end), "Start tile is Null", "Start tile is Null");
    }

     /**
      * Test with null end coordinates
      * This test checks if the map is valid when the end coordinates are null.
      */
    @Test
    public void testValidGameMap_NullEndCoordinates() {
        // Test when end coordinates are null
        Tile[][] tiles = createBasicValidMap();

        TilePoint2D start = new TilePoint2D(6, 8); // Left edge
        TilePoint2D end = null; // Right edge
        
        assertEquals(GameMap.isValidGameMap(tiles, start, end), "End tile is Null", "End tile is Null");
    }

    /**
     * Test with start tile on left edge
     * This test checks if the map is valid when the start tile is on the left edge.
     * 
     */
   
    @Test
    public void testValidGameMap_TopEdgeStart() {
        // Test with start tile on top edge
        Tile[][] tiles = createVerticalPathMap();
        TilePoint2D start = new TilePoint2D(0, 8); // Top edge
        TilePoint2D end = new TilePoint2D(15, 8); // Bottom edge
        
        
        assertEquals(GameMap.isValidGameMap(tiles, start, end),"valid" , "Map with start on top edge should be valid");
    }
     
    /**
     * Test with end tile on bottom edge
     * This test checks if the map is valid when the end tile is on the bottom edge.
     * 
     */
    @Test
    public void testValidGameMap_BottomEdgeEnd() {
        // Test with end tile on bottom edge
        // Test with start tile on top edge
        Tile[][] tiles = createVerticalPathMap();
        TilePoint2D start = new TilePoint2D(0, 8); // Top edge
        TilePoint2D end = new TilePoint2D(15, 8); // Bottom edge
        
        
        assertEquals(GameMap.isValidGameMap(tiles, start, end),"valid" , "Map with start on top edge should be valid");
    }
    
    /**
     * Test with invalid wide structure (24-25-28-29 configuration)
     * This test checks if the map is valid when the castle tiles are not together.
     * 
     */
    @Test
    public void testValidGameMap_InvalidWideStructure() {
        // Test with invalid wide structure (24-25-28-29 configuration)
        Tile[][] tiles = createBasicValidMap();
        // Place tile 24 at edge where it can't have proper configuration
        tiles[8][15] = tileFactory.create(24); // Can't have proper configuration at edge
        

        TilePoint2D start = new TilePoint2D(6, 8); 
        TilePoint2D end = new TilePoint2D(12, 0); 
        
        assertEquals(GameMap.isValidGameMap(tiles, start, end), "Castle tiles should be together", "Castle tiles should be together");
    }
    

    /**
     * Test with invalid wide structure (24-25-28-29 configuration)
     * This test checks if the map is valid when the castle tiles are not together.
     * 
     */
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


  
    private static final int[][] MAP = {
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
   /**
     * Creates a basic valid map with predefined tile configurations.
     * This method initializes a 16x9 map with specific tile codes.
     * 
     * @return A 2D array of Tile objects representing the map.
     */

    private Tile[][] createBasicValidMap() {

        int MAP_WIDTH = 16; // Width of the map
        int MAP_HEIGHT = 9; // Height of the map

        Tile[][] tiles = new Tile[MAP_HEIGHT][MAP_WIDTH]; // Initialize the tiles array
        TileFactory tileFactory = new TileFactory();

        // Create the tiles and set their properties
        for (int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                Tile tile = tileFactory.create(MAP[i][j]); // Create a new tile with code using factory
                tile.setCoordinate(new TilePoint2D(j, i)); // Set coordinates for the tile
                tiles[i][j] = tile;
            }
        }

        return tiles; // Return the static map
    }


    private static final int[][] VERTICAL_MAP = {
    { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 },
    { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 },
    { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 },
    { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 },
    { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 },
    { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 },
    { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 },
    { 5, 5, 5, 5, 15, 5, 15, 5, 15, 5, 15, 5, 5, 5, 5, 5 },
    {13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13}
};

    
/**
 * Creates a vertical path map with predefined tile configurations.
 * This method initializes a 16x9 map with specific tile codes for a vertical path.
 *  * @return A 2D array of Tile objects representing the vertical path map.
 *  
 */
   
    private Tile[][] createVerticalPathMap() {
        int MAP_WIDTH = 16; // Width of the map
        int MAP_HEIGHT = 9; // Height of the map

        Tile[][] tiles = new Tile[MAP_HEIGHT][MAP_WIDTH]; // Initialize the tiles array
        TileFactory tileFactory = new TileFactory();

        // Create the tiles and set their properties
        for (int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                Tile tile = tileFactory.create(VERTICAL_MAP[i][j]); // Create a new tile with code using factory
                tile.setCoordinate(new TilePoint2D(j, i)); // Set coordinates for the tile
                tiles[i][j] = tile;
            }
        }

        return tiles; // Return the static map
    }

}
