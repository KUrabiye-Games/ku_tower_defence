package com.kurabiye.kutd.model.Coordinates;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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


@JsonIgnoreProperties(ignoreUnknown = true)
public class TilePoint2D extends Point2D {
    private static final long serialVersionUID = 1L;
    

    /** The number of tiles in the x direction (horizontal). */

    public static final int NUMBER_OF_TILES_X = 16; // The number of tiles in the x direction
    
    /** The number of tiles in the y direction (vertical). */
    public static final int NUMBER_OF_TILES_Y = 9; // The number of tiles in the y direction

    /** The width of the map in pixels. */
    public static final int MAP_WIDTH = 1920; // The width of the map in pixels
    
    /** The height of the map in pixels. */
    public static final int MAP_HEIGHT = 1080; // The height of the map in pixels

    /** The width of each tile in pixels. */
    public static final int TILE_WIDTH = MAP_WIDTH / NUMBER_OF_TILES_X; // The width of each tile in pixels
    
    /** The height of each tile in pixels. */
    public static final int TILE_HEIGHT = MAP_HEIGHT / NUMBER_OF_TILES_Y; // The height of each tile in pixels

    /** The x coordinate of the tile in the grid. */
    private int tileX; // The x coordinate of the tile in the grid
    
    /** The y coordinate of the tile in the grid. */
    private int tileY; // The y coordinate of the tile in the grid

    /** The right top corner of the tile in pixel coordinates. */
    private Point2D rightTopCorner;
    
    /** The right bottom corner of the tile in pixel coordinates. */
    private Point2D rightBottomCorner;
    
    /** The left top corner of the tile in pixel coordinates (same as super class coordinate). */
    private Point2D leftTopCorner; // the super value store this coordinate
    
    /** The left bottom corner of the tile in pixel coordinates. */
    private Point2D leftBottomCorner;

    /** The center point of the tile in pixel coordinates. */
    private Point2D center; // the super value store this coordinate

 
    /**
     * Constructor for the TilePoint2D class.
     * Creates a new TilePoint2D with the specified tile coordinates.
     * Automatically calculates all corner points and center point in pixel coordinates.
     * 
     * @param tileX The x coordinate of the tile in the grid (0-based index)
     * @param tileY The y coordinate of the tile in the grid (0-based index)
     * @throws IllegalArgumentException if tile coordinates are out of bounds
     */
    @JsonCreator
    public TilePoint2D(@JsonProperty("x") int tileX, @JsonProperty("y") int tileY) {

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


    /**
     * Gets the x coordinate of the tile in the grid.
     * 
     * @return The x coordinate of the tile (0-based index)
     */
  @JsonProperty("x")
    public int getTileX() {
        return tileX; // Convert the pixel coordinates to tile coordinates
    }


    /**
     * Gets the y coordinate of the tile in the grid.
     * 
     * @return The y coordinate of the tile (0-based index)
     */
   @JsonProperty("y")
    public int getTileY() {
        return tileY; // Convert the pixel coordinates to tile coordinates
    }


    /**
     * Gets the right top corner of the tile.
     * 
     * @return The right top corner point in pixel coordinates
     */
    @JsonProperty("rightTopCorner")
    public Point2D getRightTopCorner() {
        return rightTopCorner; // Get the right top corner of the tile
    }

    /**
     * Gets the right bottom corner of the tile.
     * 
     * @return The right bottom corner point in pixel coordinates
     */
    @JsonProperty("rightBottomCorner")
    public Point2D getRightBottomCorner() {
        return rightBottomCorner; // Get the right bottom corner of the tile
    }


    /**
     * Gets the left top corner of the tile.
     * 
     * @return The left top corner point in pixel coordinates
     */
    @JsonProperty("leftTopCorner")
    public Point2D getLeftTopCorner() {
        return leftTopCorner; // Get the left top corner of the tile
    }


    /**
     * Gets the left bottom corner of the tile.
     * 
     * @return The left bottom corner point in pixel coordinates
     */
    @JsonProperty("leftBottomCorner")
    public Point2D getLeftBottomCorner() {
        return leftBottomCorner; // Get the left bottom corner of the tile
    }


    /**
     * Gets the center point of the tile.
     * 
     * @return The center point in pixel coordinates
     */
    @JsonProperty("center")
    public Point2D getCenter() {
        return center; // Get the center of the tile
    }

    /**
     * Gets the width of each tile in pixels.
     * 
     * @return The tile width in pixels
     */
    public static int getTileWidth() {
        return TILE_WIDTH; // Get the width of the tile in pixels
    }

    /**
     * Gets the height of each tile in pixels.
     * 
     * @return The tile height in pixels
     */
    public static int getTileHeight() {
        return TILE_HEIGHT; // Get the height of the tile in pixels
    }


    /**
     * Checks if this TilePoint2D is equal to another object.
     * Two TilePoint2D objects are considered equal if they have the same tile coordinates.
     * 
     * @param obj The object to compare with this TilePoint2D
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Check if the object is the same instance
        if (!(obj instanceof TilePoint2D)) return false; // Check if the object is of the same type

        TilePoint2D other = (TilePoint2D) obj; // Cast the object to TilePoint2D
        return tileX == other.getTileX() && tileY == other.getTileY(); // Check if the tile coordinates are equal
    }

    
    
   
}