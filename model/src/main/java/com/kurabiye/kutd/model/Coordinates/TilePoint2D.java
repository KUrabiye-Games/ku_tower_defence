package com.kurabiye.kutd.model.Coordinates;

/**
 * This is the specific class that represents a tile coordinate in the game.
 * It extends our custom Point2D and adds tile-specific functionality.
 * It adjusts the tile sizes and the coordinates according to the map dimensions.
 * One can use this class to define the coordinates of the map tiles.
 * 
 * Each tile has 4 corners, and the coordinates are defined in pixels.
 * The map is divided into a grid of tiles, and each tile has a width and height. 
 * 
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

    private int tileX; // The x coordinate of the tile in the grid
    private int tileY; // The y coordinate of the tile in the grid

    private Point2D rightTopCorner;
    private Point2D rightBottomCorner;
    private Point2D leftTopCorner; // the super value store this coordinate
    private Point2D leftBottomCorner;

    private Point2D center; // the super value store this coordinate
 
    public TilePoint2D(int tileX, int tileY) {
        super(tileX * TILE_WIDTH, tileY * TILE_HEIGHT); // Convert the tile coordinates to pixel coordinates

        this.rightTopCorner = new Point2D((tileX + 1) * TILE_WIDTH, tileY * TILE_HEIGHT); // Calculate the right top corner
        this.rightBottomCorner = new Point2D((tileX + 1) * TILE_WIDTH, (tileY + 1) * TILE_HEIGHT); // Calculate the right bottom corner
        this.leftTopCorner = new Point2D(tileX * TILE_WIDTH, tileY * TILE_HEIGHT); // Calculate the left top corner
        this.leftBottomCorner = new Point2D(tileX * TILE_WIDTH, (tileY + 1) * TILE_HEIGHT); // Calculate the left bottom corner

        this.center = new Point2D((tileX + 0.5) * TILE_WIDTH, (tileY + 0.5) * TILE_HEIGHT); // Calculate the center of the tile

        this.tileX = tileX; // Set the tile x coordinate
        this.tileY = tileY; // Set the tile y coordinate
        
        // Check if the tile coordinates are within the bounds of the map
        if (tileX < 0 || tileX >= NUMBER_OF_TILES_X) {
            throw new IllegalArgumentException("Tile x coordinate is out of bounds: " + tileX);
        }
        if (tileY < 0 || tileY >= NUMBER_OF_TILES_Y) {
            throw new IllegalArgumentException("Tile y coordinate is out of bounds: " + tileY);
        }
    }

    public int getTileX() {
        return tileX; // Convert the pixel coordinates to tile coordinates
    }

    public int getTileY() {
        return tileY; // Convert the pixel coordinates to tile coordinates
    }

    public Point2D getRightTopCorner() {
        return rightTopCorner; // Get the right top corner of the tile
    }

    public Point2D getRightBottomCorner() {
        return rightBottomCorner; // Get the right bottom corner of the tile
    }

    public Point2D getLeftTopCorner() {
        return leftTopCorner; // Get the left top corner of the tile
    }

    public Point2D getLeftBottomCorner() {
        return leftBottomCorner; // Get the left bottom corner of the tile
    }

    public Point2D getCenter() {
        return center; // Get the center of the tile
    }

    public static int getTileWidth() {
        return TILE_WIDTH; // Get the width of the tile in pixels
    }

    public static int getTileHeight() {
        return TILE_HEIGHT; // Get the height of the tile in pixels
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Check if the object is the same instance
        if (!(obj instanceof TilePoint2D)) return false; // Check if the object is of the same type

        TilePoint2D other = (TilePoint2D) obj; // Cast the object to TilePoint2D
        return tileX == other.getTileX() && tileY == other.getTileY(); // Check if the tile coordinates are equal
    }

    
    
   
}