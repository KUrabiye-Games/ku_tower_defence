package com.kurabiye.kutd.model.Map;

import java.util.ArrayList;
import java.util.List;

import com.kurabiye.kutd.model.Coordinates.Point2D;
import com.kurabiye.kutd.model.Coordinates.TilePoint2D;
import com.kurabiye.kutd.model.Tile.Tile;

public final class GameMapPathFinder {

    public static final int MAP_WIDTH = GameMap.MAP_WIDTH; // Width of the map
    public static final int MAP_HEIGHT = GameMap.MAP_HEIGHT; // Height of the map




    /**
     * This method requires the GameMap to be a valid game map.
     * I do not want to repeat the code in the isValidGameMap method.
     * First check if the game map is valid.
     * Note that build path would add the ERROR_PATH to the end of the arraylist if there is no path
     * 
     * @return ArrayList<Tile> - List of path tiles from the starting tile to the ending tile, if there is a path.
     * * If there is no path, it returns an empty ArrayList.
     * 
     * 
     */

    public static List<Tile> buildTilePath(Tile[][] tiles, TilePoint2D startTileCoordinates, TilePoint2D endTileCoordinates) {
        
        List<Tile> my_path = new ArrayList<>(); // List to store the path tiles

        Tile addTile = tiles[startTileCoordinates.getTileY()][startTileCoordinates.getTileX()]; // Get the starting tile
        
        my_path.add(addTile); // Add the starting tile to the path

        int startTileDirection = getTileDirection(tiles[startTileCoordinates.getTileY()][startTileCoordinates.getTileX()], startTileCoordinates); // Get the starting tile direction
        

        int currentToDirection = findOtherEndTile(addTile, startTileDirection); // Set the current direction to the starting tile direction

        do{
            

            // Check if only single one of the neighbours is a path tile and not already in the path

            /*
             * Tile directions
             *      1
             * 0 - Tile - 2
             *      3
             */

             Tile targetTile = null; // Initialize the target tile

           if(currentToDirection == 1){
                targetTile = tiles[addTile.getCoordinate().getTileY() - 1][addTile.getCoordinate().getTileX()]; // Get the tile above

           }else if(currentToDirection == 2){
                targetTile = tiles[addTile.getCoordinate().getTileY()][addTile.getCoordinate().getTileX() + 1]; // Get the tile to the right
           }else if(currentToDirection == 3){
                targetTile = tiles[addTile.getCoordinate().getTileY() + 1][addTile.getCoordinate().getTileX()]; // Get the tile below
           }else if(currentToDirection == 0){
                targetTile = tiles[addTile.getCoordinate().getTileY()][addTile.getCoordinate().getTileX() - 1]; // Get the tile to the left
           }else{

                // add the error tile to the path
                my_path.add(GameMap.ERROR_TILE); // Add the error tile to the path
                break; // Exit the loop if the direction is invalid
            }

            if (targetTile != null && targetTile.isPathTile() && !my_path.contains(targetTile)) {
                // Check if the target tile has a connection in the current direction

                if(convertDirection(currentToDirection) == targetTile.getTileDirections()[0] || 
                    convertDirection(currentToDirection) == targetTile.getTileDirections()[1]) {
                    // If the target tile is a path tile and not already in the path, add it to the path

                    
                    my_path.add(targetTile); // Add the target tile to the path

                    addTile = targetTile; // Update the current tile to the target tile

                    currentToDirection = findOtherEndTile(addTile, convertDirection(currentToDirection)); // Update the current direction to the target tile direction
                   }else{

                    // We've hit a dead end or loop in the path
                    my_path.add(GameMap.ERROR_TILE);
                    break;

                   }

                
                
            } else {
                // We've hit a dead end or loop in the path
                my_path.add(GameMap.ERROR_TILE);
                break;
            }

            // Check if we've reached the end tile
            if (addTile.getCoordinate().getTileX() == endTileCoordinates.getTileX() && 
                addTile.getCoordinate().getTileY() == endTileCoordinates.getTileY()) {
            }

        }while (addTile != tiles[endTileCoordinates.getTileY()][endTileCoordinates.getTileX()]); // Loop until the end tile is reached

        return my_path; // Return the list of path tiles
    }


    /**
     * This method finds the other end tile of the path based on the current tile and direction.
     * It checks the tile's directions and returns the other end tile in the opposite direction.
     * 
     * @param tile - The current tile
     * @param direction - The current direction
     * 
     * @return int - The other end tile of the path, or -1 if not found
     */
    private static int findOtherEndTile(Tile tile, int direction) {
        // Find the other end tile of the path
        int otherEndTile = -1;
        if(tile.getTileDirections()[0] == direction) {
            otherEndTile = tile.getTileDirections()[1]; // Get the other end tile in the opposite direction
        } else if (tile.getTileDirections()[1] == direction) {
            otherEndTile = tile.getTileDirections()[0]; // Get the other end tile in the opposite direction
        }
        return otherEndTile;
    }

    /**
     * This method converts the direction to the opposite direction.
     * It is used to convert the direction of the tile to the opposite direction.
     * 
     * @param direction - The current direction
     * 
     * @return int - The opposite direction (0, 1, 2, or 3)
     */

    private static int convertDirection(int direction) {
        // Convert the direction to the opposite direction
        if (direction == 0) {
            return 2; // Convert left to right
        } else if (direction == 1) {
            return 3; // Convert up to down
        } else if (direction == 2) {
            return 0; // Convert right to left
        } else if (direction == 3) {
            return 1; // Convert down to up
        }
        return -1; // Invalid direction
    }

    /**
     * This method builds the point path from the tile path.
     * It converts the tile coordinates to point coordinates and adds them to the path.
     * 
     * @param tilePath - List of tiles representing the path
     * @param startTileCoordinates - Coordinates of the starting tile
     * @param endTileCoordinates - Coordinates of the ending tile
     * 
     * @return List<Point2D> - List of path points from the starting tile to the ending tile
     */
    public static List<Point2D> buildPointPath(List<Tile> tilePath, TilePoint2D startTileCoordinates, TilePoint2D endTileCoordinates) {


        List<Point2D> pathPoints = new ArrayList<>(); // List to store the path points

        for (Tile tile : tilePath) {
            // Skip tiles with null coordinates or GameMap.ERROR_TILE
            if (tile != GameMap.ERROR_TILE && tile.getCoordinate() != null) {
                pathPoints.add(tile.getCoordinate().getCenter()); // Add the tile coordinates to the path points
            }
        }

        // Add on point to the beginning and end of the path
        // So that enemy spawn does not happen on the visible path

        Point2D startPoint = pathPoints.get(0); // Get the starting point

        int startTileDirection = getTileDirection(tilePath.get(0), startTileCoordinates); // Get the starting tile direction


        if(startTileDirection == 0) {
            pathPoints.add(0, startPoint.add(new Point2D(-3 * MAP_WIDTH, 0)));
        }
        else if(startTileDirection == 1) {
            pathPoints.add(0, startPoint.add(new Point2D(0, -3 * MAP_HEIGHT)));
        }
        else if(startTileDirection == 2) {
            pathPoints.add(0, startPoint.add(new Point2D(3 * MAP_WIDTH, 0)));
        }
        else if(startTileDirection == 3) {
            pathPoints.add(0, startPoint.add(new Point2D(0, 3 * MAP_HEIGHT)));
        }


        Point2D endPoint = pathPoints.get(pathPoints.size() - 1); // Get the ending point

        int endTileDirection = getTileDirection(tilePath.get(tilePath.size() - 1), endTileCoordinates); // Get the ending tile direction

        if(endTileDirection == 0) {
            pathPoints.add(endPoint.add(new Point2D(-4 * MAP_WIDTH, 0)));
        }
        else if(endTileDirection == 1) {
            pathPoints.add(endPoint.add(new Point2D(0, -4 * MAP_HEIGHT)));
        }
        else if(endTileDirection == 2) {
            pathPoints.add(endPoint.add(new Point2D(4 * MAP_WIDTH, 0)));
        }
        else if(endTileDirection == 3) {
            pathPoints.add(endPoint.add(new Point2D(0, 4 * MAP_HEIGHT)));
        }


        return pathPoints; // Return the list of path points
    }

    

    /**
     * This method gets the direction of the tile based on its coordinates.
     * It checks the tile's position on the map and returns the direction accordingly.
     * 
     * @param tile - The tile to get the direction for
     * @param startTileCoordinates - The coordinates of the starting tile
     * 
     * @return int - The direction of the tile (0, 1, 2, or 3)
     */

    private static int getTileDirection(Tile tile, TilePoint2D startTileCoordinates) {

        int startTileDirection = -1; // Initialize the starting tile direction
 
        int[] startTileDirections = tile.getTileDirections();

        // Set up the starting and ending tile directions
        // If the starting tile in on the bottom edge and the tile has a direction 3 then set the staring tile direction to 3
        

        if (startTileCoordinates.getTileY() == MAP_HEIGHT - 1 && (startTileDirections[0] == 3 || startTileDirections[1] == 3)) {
            startTileDirection = 3;
        }
        // If the starting the starting tile is on the left edge and the tile has a direction 0 then set the staring tile direction to 0

        if (startTileCoordinates.getTileX() == 0 && (startTileDirections[0] == 0 || startTileDirections[1] == 0)) {
            startTileDirection = 0;
        }

        // If the starting the starting tile is on the right edge and the tile has a direction 2 then set the staring tile direction to 2
        if (startTileCoordinates.getTileX() == MAP_WIDTH - 1 && (startTileDirections[0] == 2 || startTileDirections[1] == 2)) {
            startTileDirection = 2;
        }

        // If the starting the starting tile is on the top edge and the tile has a direction 1 then set the staring tile direction to 1
        if (startTileCoordinates.getTileY() == 0 && (startTileDirections[0] == 1 || startTileDirections[1] == 1)) {
            startTileDirection = 1;
        }

        return startTileDirection; // Return the starting tile direction
    }

}
