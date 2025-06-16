package com.kurabiye.kutd.model.Map;

import com.kurabiye.kutd.model.Coordinates.TilePoint2D;
import com.kurabiye.kutd.model.Tile.Tile;
import com.kurabiye.kutd.model.Tile.TileFactory;

/**
 * StaticMap.java
 * * This class provides a static map for the game.
 * 
 * * It contains a predefined 2D array of tile codes that represent the map layout.
 * * * The map is used to create a GameMap object with tiles, starting and ending coordinates.
 * 
 * 
 * * @author Atlas Berk Polat
 * * @version 1.0
 * * @since 2025-06-01
 */

public final class StaticMap {

    public static final int MAP_WIDTH = GameMap.MAP_WIDTH; // Width of the map
    public static final int MAP_HEIGHT = GameMap.MAP_HEIGHT; // Height of the map

    private static final int[][] MAP = {
        { 5, 5, 5, 5, 16, 5, 17, 5, 5, 5, 24, 25, 7, 5, 5, 19 },
        { 0, 1, 2, 5, 0, 1, 2, 5, 5, 18, 28, 29, 6, 23, 16, 5 },
        { 4, 15, 7, 15, 7, 22, 8, 13, 13, 9, 1, 9, 10, 5, 5, 5 },
        { 8, 2, 8, 9, 10, 5, 5, 5, 5, 5, 5, 5, 5, 17, 27, 5 },
        { 5, 7, 19, 18, 5, 5, 5, 0, 1, 2, 15, 5, 5, 31, 5, 5 },
        { 5, 7, 5, 5, 15, 0, 13, 10, 15, 8, 13, 2, 5, 5, 5, 5 },
        { 5, 4, 5, 0, 13, 10, 0, 1, 2, 5, 30, 6, 5, 5, 5, 5 },
        { 23, 7, 15, 7, 5, 5, 4, 18, 8, 13, 13, 10, 16, 5, 18, 5 },
        { 5, 8, 13, 10, 5, 5, 7, 5, 5, 5, 5, 5, 5, 5, 5, 5 }
    };

    

    /**
     * This method creates or retrieves the prebuilt static map for the game.
     * It uses lazy initialization to ensure the map is created only once.
     * 
     * @return GameMap - Returns a GameMap object with the prebuilt static map.
     */
    public static GameMap getPrebuiltMap() {

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

            // Set the starting and ending tiles
            TilePoint2D startTileCoordinates = new TilePoint2D(6, 8);
            TilePoint2D endTileCoordinates = new TilePoint2D(12, 0);

            GameMap preMadeMap = new GameMap(tiles, startTileCoordinates, endTileCoordinates); // Create the static map
        
        return preMadeMap; // Return the singleton instance
    }
}