package com.kurabiye.kutd.model.Coordinates;

import javafx.geometry.Point2D;

/**
 * This is the specific class that represents a tile coordinate in the game.
 * It extends JavaFX's Point2D and adds tile-specific functionality.
 * It adjusts the tile sizes and the coordinates according to the map dimensions.
 * One can use this class to define the coordinates of the map tiles.
 * 
 * @author: Atlas Berk Polat
 * @version: 1.0
 * @since: 2025-04-23
 */
public class TilePoint2D extends Point2D {

    public static final int NUMBER_OF_TILES_X = 16; // The number of tiles in the x direction
    public static final int NUMBER_OF_TILES_Y = 9; // The number of tiles in the y direction

    public static final int MAP_WIDTH = 1920; // The width of the map in pixels
    public static final int MAP_HEIGHT = 1080; // The height of the map in pixels

    public static final int TILE_WIDTH = MAP_WIDTH / NUMBER_OF_TILES_X; // The width of each tile in pixels
    public static final int TILE_HEIGHT = MAP_HEIGHT / NUMBER_OF_TILES_Y; // The height of each tile in pixels
 
    public TilePoint2D(int tileX, int tileY) {
        super(tileX * TILE_WIDTH, tileY * TILE_HEIGHT); // Convert the tile coordinates to pixel coordinates
        
        // Check if the tile coordinates are within the bounds of the map
        if (tileX < 0 || tileX >= NUMBER_OF_TILES_X) {
            throw new IllegalArgumentException("Tile x coordinate is out of bounds: " + tileX);
        }
        if (tileY < 0 || tileY >= NUMBER_OF_TILES_Y) {
            throw new IllegalArgumentException("Tile y coordinate is out of bounds: " + tileY);
        }
    }

    public int getTileX() {
        return (int)(getX() / TILE_WIDTH); // Convert the pixel coordinates to tile coordinates
    }

    public int getTileY() {
        return (int)(getY() / TILE_HEIGHT); // Convert the pixel coordinates to tile coordinates
    }
    
    /**
     * Creates a new TilePoint2D with the specified tile X coordinate
     * @param tileX The tile X coordinate
     * @return A new TilePoint2D with updated tile X coordinate
     */
    public TilePoint2D withTileX(int tileX) {
        // Check if the tile coordinates are within the bounds of the map
        if (tileX < 0 || tileX >= NUMBER_OF_TILES_X) {
            throw new IllegalArgumentException("Tile x coordinate is out of bounds: " + tileX);
        }
        return new TilePoint2D(tileX, getTileY());
    }
    
    /**
     * Creates a new TilePoint2D with the specified tile Y coordinate
     * @param tileY The tile Y coordinate
     * @return A new TilePoint2D with updated tile Y coordinate
     */
    public TilePoint2D withTileY(int tileY) {
        // Check if the tile coordinates are within the bounds of the map
        if (tileY < 0 || tileY >= NUMBER_OF_TILES_Y) {
            throw new IllegalArgumentException("Tile y coordinate is out of bounds: " + tileY);
        }
        return new TilePoint2D(getTileX(), tileY);
    }
}