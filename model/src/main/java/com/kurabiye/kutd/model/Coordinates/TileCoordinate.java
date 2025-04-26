package com.kurabiye.kutd.model.Coordinates;


/*  This is the specific class that represents a tile coordinate in the game.
 *  It adjust the tile sizes and the coordinates according coordinates of the map.
 *  One can use this class to define the coordinates of the map tiles.
 * 
 * 
 * @author: Atlas Berk Polat
 * @version: 1.0
 * @since: 2025-04-23
 * 
 */

public class TileCoordinate extends Coordinate {

    public static final int NUMBER_OF_TILES_X = 16; // The number of tiles in the x direction
    public static final int NUMBER_OF_TILES_Y = 9; // The number of tiles in the y direction


    public static final int TILE_WIDTH = Coordinate.MAP_WIDTH / NUMBER_OF_TILES_X; // The width of each tile in pixels
    public static final int TILE_HEIGHT = Coordinate.MAP_HEIGHT / NUMBER_OF_TILES_Y; // The height of each tile in pixels
 
    public TileCoordinate(int x, int y) {
        super(x * TILE_WIDTH, y * TILE_HEIGHT); // Convert the tile coordinates to pixel coordinates
        // Check if the tile coordinates are within the bounds of the map
        
        if (x < 0 || x >= NUMBER_OF_TILES_X) {
            throw new IllegalArgumentException("Tile x coordinate is out of bounds: " + x);
        }
        if (y < 0 || y >= NUMBER_OF_TILES_Y) {
            throw new IllegalArgumentException("Tile y coordinate is out of bounds: " + y);
        }
    }

    public int getTileX() {
        return getX() / TILE_WIDTH; // Convert the pixel coordinates to tile coordinates
    }

    public int getTileY() {
        return getY() / TILE_HEIGHT; // Convert the pixel coordinates to tile coordinates
    }

    public void setTileX(int x) {
        // Check if the tile coordinates are within the bounds of the map
        if (x < 0 || x >= NUMBER_OF_TILES_X) {
            throw new IllegalArgumentException("Tile x coordinate is out of bounds: " + x);
        }
        setX(x * TILE_WIDTH); // Convert the tile coordinates to pixel coordinates
    }
    public void setTileY(int y) {
        // Check if the tile coordinates are within the bounds of the map
        if (y < 0 || y >= NUMBER_OF_TILES_Y) {
            throw new IllegalArgumentException("Tile y coordinate is out of bounds: " + y);
        }
        setY(y * TILE_HEIGHT); // Convert the tile coordinates to pixel coordinates
    }

}
