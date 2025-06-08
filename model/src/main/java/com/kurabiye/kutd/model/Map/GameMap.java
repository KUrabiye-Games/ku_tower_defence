package com.kurabiye.kutd.model.Map;


import java.util.ArrayList;
import java.util.List;

import com.kurabiye.kutd.model.Coordinates.Point2D;
import com.kurabiye.kutd.model.Coordinates.TilePoint2D;
import com.kurabiye.kutd.model.Tile.Tile;
import com.kurabiye.kutd.util.ObserverPattern.Observable;
import com.kurabiye.kutd.util.ObserverPattern.Observer;

/**
 * This class represents the game map.
 * It contains a 2D array of tiles, each tile has a specific code that determines its type.
 * The map is divided into a grid of tiles, and each tile has a width and height.
 * The map is used to build and sell towers, and to determine the path of the enemies.
 * The map is also used to determine the starting and ending tiles of the path.
 * 
 * Version 2.0
 * This version includes the following changes:
 * Made most functionalities static so that the map can be used without creating an instance of the GameMap class.
 * isValidGameMap method is refactored to check the validity of the game map.
 * 
 * @author Atlas Berk Polat
 * @version 2.0
 * @since 2025-06-01
 */

public class GameMap implements Observable{

    public static final Tile ERROR_TILE = new Tile(1);


    public static final int MAP_WIDTH = 16; // Width of the map
    public static final int MAP_HEIGHT = 9; // Height of the map

    /**
     * [0] 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 x-axis
     * [1] 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15
     *   .
     *   .
     * [8] 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15
     * y-axis
     * 
     *  The map is represented as a 2D array of tiles.
     *  Each tile has a specific code that determines its type.
     * 
     *  The map is divided into a grid of tiles, and each tile has a width and height.
     * tiles[y-axis][x-axis]
     */ 
    private Tile[][] tiles; // 2D array representing the tiles on the map it will

    private List<Point2D> pointPath; // List of path tiles on the map

    private List<Tile> tilePath;

    private String name; // Name of the game map


    public GameMap() {
        tiles = new Tile[MAP_HEIGHT][MAP_WIDTH]; // Initialize the tiles array
    }

    /**
     * Constructor for the GameMap class.
     * 
     * @param tiles - 2D array of tiles representing the map
     * @param startTileCoordinates - Coordinates of the starting tile
     * @param endTileCoordinates - Coordinates of the ending tile
     * 
     * @throws IllegalArgumentException - If the game map is invalid
     *
     *
     * @param tiles
     * @param startTileCoordinates
     * @param endTileCoordinates
     */

    public GameMap(Tile[][] tiles, TilePoint2D startTileCoordinates, TilePoint2D endTileCoordinates) {


        try {
            GameMapValidator.isValidGameMap(tiles, startTileCoordinates, endTileCoordinates); // Validate the game map
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("GameMap validation failed: " + e.getMessage(), e); // Wrap and rethrow with additional context
        }

        this.tiles = tiles; // Initialize the tiles array with the provided tiles


       
        inititializePaths(startTileCoordinates, endTileCoordinates); // Initialize the paths from the starting tile to the ending tile

    }


    /**
     * This method initializes the paths from the starting tile to the ending tile.
     * It builds the tile path and the point path using the GameMapPathFinder class.
     * 
     * @param startTileCoordinates - Coordinates of the starting tile
     * @param endTileCoordinates - Coordinates of the ending tile
     */
    private void inititializePaths(TilePoint2D startTileCoordinates, TilePoint2D endTileCoordinates) {
        this.tilePath = GameMapPathFinder.buildTilePath(tiles, startTileCoordinates, endTileCoordinates); // Build the tile path from the starting tile to the ending tile
        this.pointPath = GameMapPathFinder.buildPointPath(tilePath, startTileCoordinates, endTileCoordinates); // Build the point path from the starting tile to the ending tile
    }


    /**
     * The following two methods are synchronized to ensure thread safety.
     * * They allow getting and setting tiles at specific coordinates on the map.
     * Usually used to build and sell the towers on the map.
     * * @param x - x-coordinate of the tile
     * * @param y - y-coordinate of the tile
     * 
     * 
     * * @return Tile - The tile at the specified coordinates
     * 
     * * @throws IllegalArgumentException - If the coordinates are out of bounds
     * 
     */

    public synchronized Tile getTile(int x, int y) throws IllegalArgumentException {
        if (x < 0 || x >= MAP_WIDTH || y < 0 || y >= MAP_HEIGHT) {
            throw new IllegalArgumentException("Coordinates out of bounds");
        }
        return tiles[y][x]; // Return the tile at the specified coordinates
    }
    /**
     * The following two methods are synchronized to ensure thread safety.
     * * They allow getting and setting tiles at specific coordinates on the map.
     * Usually used to build and sell the towers on the map.
     * * @param x - x-coordinate of the tile
     * * @param y - y-coordinate of the tile
     * 
     * 
     * * @return Tile - The tile at the specified coordinates
     * 
     * * @throws IllegalArgumentException - If the coordinates are out of bounds
     * 
     */

    public synchronized void setTile(int x, int y, Tile tile) throws IllegalArgumentException {
        if (x < 0 || x >= MAP_WIDTH || y < 0 || y >= MAP_HEIGHT) {
            throw new IllegalArgumentException("Coordinates out of bounds");
        }
        changed = true; // Set the changed flag to true to indicate that the map has changed

        tiles[y][x] = tile;
        notifyObservers(tile);
         // Set the tile at the specified coordinates
    }

    /**
     * This method returns the point path of the game map.
     * It is used to get the path points from the starting tile to the ending tile.
     * 
     * @return List<Point2D> - List of path points from the starting tile to the ending tile
     */
    public List<Point2D> getPointPath() {
      
        return pointPath; // Return the list of path points
    }



    private int[][] cachedIntArrayMap; // Cached result for lazy evaluation

    private boolean changed = true; // Flag to indicate if the map has changed

    /** Convert the Game Map to a 2D array of integers using the tile codes.
     * This method uses lazy evaluation to compute the array only once.
     * 
     * @return int[][] - 2D array of integers representing the map
     */
    public int[][] toIntArray() {
        // If the cached result is null, compute it
        if (cachedIntArrayMap == null  || changed) {
            cachedIntArrayMap = new int[MAP_HEIGHT][MAP_WIDTH]; // Initialize the integer array

            for (int i = 0; i < MAP_HEIGHT; i++) {
                for (int j = 0; j < MAP_WIDTH; j++) {
                    cachedIntArrayMap[i][j] = tiles[i][j].getTileCode(); // Get the tile code and set it in the array
                }
            }

            changed = false; // Reset the changed flag after computation
        }
        return cachedIntArrayMap; // Return the cached result
    }


    /*
     * 
     * Boilerplate code for the Observable interface
     */

    private List<Observer> observers = new ArrayList<>(); // List of observers

    @Override
    public void addObserver(Observer observer) {
        if (observer == null) {
            throw new NullPointerException("Null Observer");
        }
        if (!observers.contains(observer)) {
            observers.add(observer); // Add the observer to the list
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        if (observer == null) {
            throw new NullPointerException("Null Observer");
        }
        if (observers.contains(observer)) {
            observers.remove(observer); // Remove the observer from the list
        }
    }

    @Override
    public void notifyObservers(Object arg) {
        for (Observer observer : observers) {
            observer.update(arg); // Notify each observer with the argument
        }
    }

    /** 
     * Override the equals method to remove the maps with identical tile arrays
     * 
     * @return true if the two GameMap objects are equal, false otherwise
     * */ 
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Check if the same instance
        if (!(obj instanceof GameMap)) return false; // Check if the object is a GameMap

        GameMap other = (GameMap) obj; // Cast the object to GameMap

        // Compare the tile codes of each tile
        for (int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                if (this.tiles[i][j].getTileCode() != other.tiles[i][j].getTileCode()) {
                    return false;
                }
            }
        }

        return true;
    }

    public String getName() {
        return name; // Return the name of the game map
    }
    public void setName(String name) {
        this.name = name; // Set the name of the game map
    }

    public Tile[][] getTiles() {
        return tiles; // Return the 2D array of tiles representing the map
    }
    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles; // Set the 2D array of tiles representing the map
        this.changed = true; // Set the changed flag to true to indicate that the map has changed
        inititializePaths(new TilePoint2D(0, 0), new TilePoint2D(MAP_WIDTH - 1, MAP_HEIGHT - 1)); // Reinitialize the paths with default start and end points
    }
    

 }
