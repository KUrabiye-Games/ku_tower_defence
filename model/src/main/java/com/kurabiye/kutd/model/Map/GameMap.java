package com.kurabiye.kutd.model.Map;

import java.util.ArrayList;
import java.util.List;

import com.kurabiye.kutd.model.Coordinates.Point2D;
import com.kurabiye.kutd.model.Coordinates.TilePoint2D;
import com.kurabiye.kutd.model.Tile.Tile;
import com.kurabiye.kutd.util.ObserverPattern.Observable;
import com.kurabiye.kutd.util.ObserverPattern.Observer;

public class GameMap implements Observable{

    private static final Tile ERROR_TILE = new Tile(1);

    public static final int MAP_WIDTH = 16; // Width of the map
    public static final int MAP_HEIGHT = 9; // Height of the map

    /* [0] 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 x-axis
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

    private TilePoint2D startTileCoordinates; // Starting tile of the map
    private TilePoint2D endTileCoordinates; // Ending tile of the map

    private List<Point2D> pointPath; // List of path tiles on the map

    private List<Tile> tilePath;


    public GameMap() {
        tiles = new Tile[MAP_HEIGHT][MAP_WIDTH]; // Initialize the tiles array
    }

    public GameMap(Tile[][] tiles, TilePoint2D startTileCoordinates, TilePoint2D endTileCoordinates) {
        if (tiles.length != MAP_HEIGHT || tiles[0].length != MAP_WIDTH) {
            throw new IllegalArgumentException("Invalid tile array dimensions");
        }
        this.tiles = tiles; // Initialize the tiles array with the provided tiles

        this.startTileCoordinates = startTileCoordinates; // Set the starting tile
        this.endTileCoordinates = endTileCoordinates; // Set the ending tile
    }


    /*
     * The following two methods are synchronized to ensure thread safety.
     * 
     * 
     */

    public synchronized Tile getTile(int x, int y) {
        if (x < 0 || x >= MAP_WIDTH || y < 0 || y >= MAP_HEIGHT) {
            throw new IllegalArgumentException("Coordinates out of bounds");
        }
        return tiles[y][x]; // Return the tile at the specified coordinates
    }

    public synchronized void setTile(int x, int y, Tile tile) {
        if (x < 0 || x >= MAP_WIDTH || y < 0 || y >= MAP_HEIGHT) {
            throw new IllegalArgumentException("Coordinates out of bounds");
        }
        notifyObservers(tile);
        tiles[y][x] = tile; // Set the tile at the specified coordinates
    }

    public TilePoint2D getstartTileCoordinates() {
        return startTileCoordinates; // Return the starting tile
    }

    public void setstartTileCoordinates(TilePoint2D startTileCoordinates) {
        this.startTileCoordinates = startTileCoordinates; // Set the starting tile
    }

    public TilePoint2D getendTileCoordinates() {
        return endTileCoordinates; // Return the ending tile
    }

    public void setendTileCoordinates(TilePoint2D endTileCoordinates) {
        this.endTileCoordinates = endTileCoordinates; // Set the ending tile
    }

    public List<Point2D> getPath() {
        return pointPath; // Return the list of path tiles
    }





    public boolean isValidGameMap() {

        // Check if the tiles array is null or has invalid dimensions
        if (tiles == null || tiles.length != MAP_HEIGHT || tiles[0].length != MAP_WIDTH ) {
            return false; // Invalid tiles array
        }

        // check if the tiles have any null values

        for (int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                if (tiles[i][j] == null) {
                    return false; // Invalid tile
                }
            }
        }

        // Check if the starting and ending tiles are null
        if (startTileCoordinates == null || endTileCoordinates == null) {
            return false; // Invalid tiles
        }


        // Check if the starting and ending tiles are within the bounds of the map
        if (startTileCoordinates.getTileX() < 0 || startTileCoordinates.getTileX() >= MAP_WIDTH ||
            startTileCoordinates.getTileY() < 0 || startTileCoordinates.getTileY() >= MAP_HEIGHT ||
            endTileCoordinates.getTileX() < 0 || endTileCoordinates.getTileX() >= MAP_WIDTH ||
            endTileCoordinates.getTileY() < 0 || endTileCoordinates.getTileY() >= MAP_HEIGHT) {
            return false; // Invalid coordinates
        }

        // check if the starting and ending tiles are not the same

        if (startTileCoordinates.getTileX() == endTileCoordinates.getTileX() && startTileCoordinates.getTileY() == endTileCoordinates.getTileY()) {
            return false; // Starting and ending tiles are the same
        }

        // check if the stating and ending tiles are on the edges of the map
        // they are supposed to be on the edges of the map

        if ((startTileCoordinates.getTileX() != 0 && startTileCoordinates.getTileX() != MAP_WIDTH - 1 ) ||
            (startTileCoordinates.getTileY() != 0 && startTileCoordinates.getTileY() != MAP_HEIGHT - 1)) {
            return false; // Starting tile is not on the edge of the map
        }

        if ((endTileCoordinates.getTileX() != 0 && endTileCoordinates.getTileX() != MAP_WIDTH - 1 ) ||
            (endTileCoordinates.getTileY() != 0 && endTileCoordinates.getTileY() != MAP_HEIGHT - 1)) {
            return false; // Ending tile is not on the edge of the map
        }

        // check if the staring tile is a path tile

        if (!tiles[startTileCoordinates.getTileY()][startTileCoordinates.getTileX()].isPathTile()) {
            return false; // Starting tile is not a path tile
        }
        // check if the ending tile is a path tile
        if (!tiles[endTileCoordinates.getTileY()][endTileCoordinates.getTileX()].isPathTile()) {
            return false; // Ending tile is not a path tile
        }

    
        /*check if any of the tile codes 24 25 28 29 are in the path
         *    also check if they are in the correct configuration of
         *     24 25
         *     28 29
         */
        
         for(int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                if (tiles[i][j].getTileCode() == 24){

                    //check if the tile is not on the right or bottom edge of the map

                    if(i == MAP_HEIGHT - 1 || j == MAP_WIDTH - 1) {
                        return false; // Invalid tile
                    }

                    if(tiles[i][j+1].getTileCode() != 25 || tiles[i+1][j].getTileCode() != 28 || tiles[i+1][j+1].getTileCode() != 29) {
                        return false; // Invalid tile
                    }
                }
            }

        }
        // TODO: Check if there are at least for buildable tiles



        // check if there is a single path from the starting tile to the ending tile


        buildTilePath(); // Build the path from the starting tile to the ending tile
        
        // check if the last tile is the error tile

        if (tilePath.get(tilePath.size() - 1) == ERROR_TILE) {
            return false; // Invalid path
        }
        // check if the last tile is the ending tile

        if (tilePath.get(tilePath.size() - 1) != tiles[endTileCoordinates.getTileY()][endTileCoordinates.getTileX()]) {
            return false; // Invalid path
        }



        

        // More conditions will be added later

        // For example, check if the path is one piece

        return true; // Valid game map
    }


    /*
     * This method requires the GameMap to be a valid game map.
     * I do not want to repeat the code in the isValidGameMap method.
     * First check if the game map is valid.
     * Note that build path would add the ERROR_PATH to the end of the arraylist if there is no path
     * 
     */

    private void buildTilePath() {
        

        ArrayList<Tile> my_path = new ArrayList<>(); // List to store the path tiles

        Tile addTile = tiles[startTileCoordinates.getTileY()][startTileCoordinates.getTileX()]; // Get the starting tile

        
        my_path.add(addTile); // Add the starting tile to the path
        

        do{
            

            ArrayList<Tile> neighbours = neighbours(addTile); // Get the neighbouring tiles

            // Check if only single one of the neighbours is a path tile and not already in the path

            int pathCount = 0; // Counter for path tiles

            for (Tile neighbour : neighbours) {
                if (neighbour.isPathTile() && !my_path.contains(neighbour)) {
                    pathCount++; // Increment the path tile counter
                    addTile = neighbour; // Set the next tile to the neighbour
                }
            }

            if (pathCount > 1) {
                my_path.add(ERROR_TILE); // Add the error tile to the path if there are multiple path tiles
                break; // Break the loop if there are multiple path tiles
            }
            if (pathCount == 0) {
                my_path.add(ERROR_TILE); // Add the error tile to the path if there are no path tiles
                break; // Break the loop if there are no path tiles
            }
            if (pathCount == 1) {
                my_path.add(addTile); // Add the next tile to the path

            }



            

        }while (addTile != tiles[endTileCoordinates.getTileY()][endTileCoordinates.getTileX()]); // Loop until the end tile is reached




        tilePath = my_path; // Return the list of path tiles
    }

    private ArrayList<Tile> neighbours(Tile tile) {
        
        ArrayList<Tile> neighbours = new ArrayList<>(); // List to store the neighbouring tiles

        // Check if tile or its coordinate is null
        if (tile == null || tile.getCoordinate() == null) {
            return neighbours; // Return empty list if the tile or its coordinate is null
        }

        int x = tile.getCoordinate().getTileX(); // Get the x coordinate of the tile
        int y = tile.getCoordinate().getTileY(); // Get the y coordinate of the tile

        // Check the neighbouring tiles in all 4 directions

        if (x > 0) { // Check left
            neighbours.add(tiles[y][x - 1]);
        }
        if (x < MAP_WIDTH - 1) { // Check right
            neighbours.add(tiles[y][x + 1]);
        }
        if (y > 0) { // Check up
            neighbours.add(tiles[y - 1][x]);
        }
        if (y < MAP_HEIGHT - 1) { // Check down
            neighbours.add(tiles[y + 1][x]);
        }

        return neighbours; // Return the list of neighbouring tiles
        
    }


    public void buildPointPath(){
        // Check if the tile path is already built
        if(pointPath != null) {
            return; // Return if the point path is already built
        }
        buildTilePath(); // Build the tile path

        ArrayList<Point2D> pathPoints = new ArrayList<>(); // List to store the path points

        for (Tile tile : tilePath) {
            // Skip tiles with null coordinates or ERROR_TILE
            if (tile != ERROR_TILE && tile.getCoordinate() != null) {
                pathPoints.add(tile.getCoordinate().getCenter()); // Add the tile coordinates to the path points
            }
        }

        pointPath = pathPoints; // Return the list of path points
    }

    public List<Point2D> getPointPath() {
        if(pointPath == null) {
            buildPointPath(); // Build the point path if it is not already built
        }
        return pointPath; // Return the list of path points
    }
    public List<Tile> getTilePath() {
        if(tilePath == null) {
            buildTilePath(); // Build the tile path if it is not already built
        }
        return tilePath; // Return the list of path tiles
    }


    /* Static map for the game
     * 
     * 
     */
    private static final int[][] map = {
        { 5, 5, 5, 5, 16, 5, 17, 5, 5, 5, 24, 25, 7, 5, 5, 19 },
        { 0, 1, 2, 5, 0, 1, 2, 5, 5, 18, 28, 29, 6, 23, 16, 5 },
        { 4, 15, 7, 15, 7, 22, 8, 13, 13, 9, 1, 9, 10, 5, 5, 5 },
        { 8, 2, 8, 9, 10, 5, 5, 5, 5, 5, 5, 5, 5, 17, 27, 5 },
        { 5, 7, 19, 18, 5, 5, 5, 0, 1, 2, 21, 5, 5, 31, 5, 5},
        { 5, 7, 5, 5, 20, 0, 13, 10, 15, 8, 13, 2, 5, 5, 5, 5 },
        { 5, 4, 5, 0, 13, 10, 0, 1, 2, 5, 30, 6, 5, 5, 5, 5 },
        { 23, 7, 15, 7, 5, 5, 4, 18, 8, 13, 13, 10, 16, 5, 18, 5 },
        { 5, 8, 13, 10, 5, 5, 7, 5, 5, 5, 5, 5, 5, 5, 5, 5 }
    };
 

    public static GameMap getPrebuiltMap() {
        Tile[][] tiles = new Tile[MAP_HEIGHT][MAP_WIDTH]; // Initialize the tiles array

        // Create the tiles and set their properties
        for (int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                Tile tile = new Tile(map[i][j]); // Create a new tile with code
                tile.setCoordinate(new TilePoint2D(j, i)); // Set coordinates for the tile
                tiles[i][j] = tile;
            }
        }

        // Set the starting and ending tiles
        TilePoint2D startTileCoordinates = new TilePoint2D(6, 8);
        TilePoint2D endTileCoordinates = new TilePoint2D(12,0);

        return new GameMap(tiles, startTileCoordinates, endTileCoordinates); // Return the static map
    }


    /* Convert the Game Map to an 2D array of integers
     * using the tile codes
     * 
     */

    public static int[][] toIntArray(GameMap gameMap) {
        int[][] intArray = new int[MAP_HEIGHT][MAP_WIDTH]; // Initialize the integer array

        for (int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                intArray[i][j] = gameMap.getTile(j, i).getTileCode(); // Get the tile code and set it in the array
            }
        }

        return intArray; // Return the integer array
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

    

 }
