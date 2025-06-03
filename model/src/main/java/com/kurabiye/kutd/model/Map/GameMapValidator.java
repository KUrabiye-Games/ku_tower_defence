package com.kurabiye.kutd.model.Map;

import java.util.List;


import com.kurabiye.kutd.model.Coordinates.TilePoint2D;
import com.kurabiye.kutd.model.Tile.Tile;

public final class GameMapValidator {


    public static final int MAP_WIDTH = GameMap.MAP_WIDTH; // Width of the map
    public static final int MAP_HEIGHT = GameMap.MAP_HEIGHT; // Height of the map

    /**
     * This method checks if the game map is valid.
     * It checks the following conditions:
     * 1. The tiles array is not null and has valid dimensions.
     * 2. The starting and ending tiles are not null.
     * 3. The starting and ending tiles are on the edges of the map.
     * 4. The tiles do not contain any null values.
     * 5. The starting and ending tiles are within the bounds of the map.
     * 6. The starting and ending tiles are not the same.
     * 7. The starting tile is a path tile.
     * 8. The ending tile is a path tile.
     * 9. There are at least four buildable tiles on the map.
     * 10. The path from the starting tile to the ending tile is valid.
     * 11. The last tile in the path is the ending tile.
     * 12. The castle tiles (24, 25, 28, 29) are in the correct configuration.
     * 
     * 
     * It is static so that it can be used without creating an instance of the GameMap class.
     *      *
     * @param tiles - 2D array of tiles representing the map
     * @param startTileCoordinates - Coordinates of the starting tile
     * @param endTileCoordinates - Coordinates of the ending tile
     *
     * @return boolean true - "true" if the game map is valid, otherwise an error message
     */
    public static boolean isValidGameMap(Tile[][] tiles, TilePoint2D startTileCoordinates, TilePoint2D endTileCoordinates) 
    throws IllegalArgumentException {

        // Check if the tiles array is null or has invalid dimensions
        if (tiles == null) {
            throw new IllegalArgumentException("Tiles cannot be empty"); // Invalid tiles array
        }


        if (tiles.length != MAP_HEIGHT || tiles[0].length != MAP_WIDTH) {
            throw new IllegalArgumentException("Invalid map dimensions"); // Invalid map dimensions
        }

        // Check if the starting and ending tiles are null
        if (startTileCoordinates == null) {
            throw new IllegalArgumentException("Start tile is Null"); // Invalid tiles
        }

        if (endTileCoordinates == null) {
            throw new IllegalArgumentException("End tile is Null"); // Invalid tiles
        }

        // Check if the starting and ending tiles are on the edges of the map

        if ((startTileCoordinates.getTileX() != 0 && startTileCoordinates.getTileX() != MAP_WIDTH - 1) &&
            (startTileCoordinates.getTileY() != 0 && startTileCoordinates.getTileY() != MAP_HEIGHT - 1)) {
            throw new IllegalArgumentException("Starting tile is not on the edge of the map"); // Starting tile is not on the edge of the map
        }

        if ((endTileCoordinates.getTileX() != 0 && endTileCoordinates.getTileX() != MAP_WIDTH - 1) &&
            (endTileCoordinates.getTileY() != 0 && endTileCoordinates.getTileY() != MAP_HEIGHT - 1)) {
            throw new IllegalArgumentException("Ending tile is not on the edge of the map"); // Ending tile is not on the edge of the map
        }

        // check if the tiles have any null values

        for (int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                if (tiles[i][j] == null) {
                    throw new IllegalArgumentException("Some tile pieces are Null"); // Invalid tile
                }
            }
        }

        


        // Check if the starting and ending tiles are within the bounds of the map
        if (startTileCoordinates.getTileX() < 0 || startTileCoordinates.getTileX() >= MAP_WIDTH ||
            startTileCoordinates.getTileY() < 0 || startTileCoordinates.getTileY() >= MAP_HEIGHT
           ) {
            throw new IllegalArgumentException("Start tile is out of the map bounds"); // Invalid coordinates
        }

        if (endTileCoordinates.getTileX() < 0 || endTileCoordinates.getTileX() >= MAP_WIDTH ||
            endTileCoordinates.getTileY() < 0 || endTileCoordinates.getTileY() >= MAP_HEIGHT) {
            throw new IllegalArgumentException("End tile is out of the map bounds"); // Invalid coordinates
        }

        // check if the starting and ending tiles are not the same

        if (startTileCoordinates.getTileX() == endTileCoordinates.getTileX() && startTileCoordinates.getTileY() == endTileCoordinates.getTileY()) {
            throw new IllegalArgumentException("Starting and ending tiles are the same"); 
        }


        // Check if the starting tile is a path tile

        if (!tiles[startTileCoordinates.getTileY()][startTileCoordinates.getTileX()].isPathTile()) {
            throw new IllegalArgumentException("Starting tile is not a path tile"); // Starting tile is not a path tile
        }
        // Check if the ending tile is a path tile
        if (!tiles[endTileCoordinates.getTileY()][endTileCoordinates.getTileX()].isPathTile()) {
            throw new IllegalArgumentException("Ending tile is not a path tile"); // Ending tile is not a path tile
        }

       
         // All the iterative checks are done here together
        // Check if the castle tiles (24, 25, 28, 29) are in the correct configuration
        // 24 25
        // 28 29
        // This is done by checking if the tile code 24 is present and if the adjacent tiles are 25, 28, and 29
        // Also check is there is at least four buildable tiles on the map

        int buildableCount = 0; // Counter for buildable tiles
         
        
         for(int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                if (tiles[i][j].getTileCode() == 24){

                    //check if the tile is not on the right or bottom edge of the map

                    if(i == MAP_HEIGHT - 1 || j == MAP_WIDTH - 1) {
                        throw new IllegalArgumentException("Castle tiles should be together"); // Invalid tile
                    }

                    if(tiles[i][j+1].getTileCode() != 25 || tiles[i+1][j].getTileCode() != 28 || tiles[i+1][j+1].getTileCode() != 29) {
                        throw new IllegalArgumentException("Castle tiles should be together"); // Invalid tile
                    }
                }

                if (tiles[i][j].isBuildableTile()) {
                    buildableCount++; // Increment the counter if the tile is buildable
                }

            }

        }

        if (buildableCount < 4) {
            throw new IllegalArgumentException("Insufficient buildable tiles"); // Insufficient buildable tiles
        }

        

        List<Tile> tilePath = GameMapPathFinder.buildTilePath(tiles, startTileCoordinates, endTileCoordinates); // Build the tile path from the starting tile to the ending tile

        if (tilePath.isEmpty()) {
            throw new IllegalArgumentException("No path found from start to end tile"); // No path found
        }

        if (tilePath.contains(GameMap.ERROR_TILE)) {
            throw new IllegalArgumentException("Tile path is disconnected or has an error");
        }

        // Check if the last element in the tilePath is the ending tile

        if (tilePath.get(tilePath.size() - 1).getCoordinate().getTileX() != endTileCoordinates.getTileX() || 
            tilePath.get(tilePath.size() - 1).getCoordinate().getTileY() != endTileCoordinates.getTileY()) {
            throw new IllegalArgumentException("Last tile in the path is not the ending tile"); // Last tile in the path is not the ending tile
        }


       

        return true; // Valid game map
    }

}
