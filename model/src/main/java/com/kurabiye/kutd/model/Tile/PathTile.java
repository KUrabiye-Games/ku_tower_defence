package com.kurabiye.kutd.model.Tile;

/* PathTile.java
 * This class represents a path tile in the game.
 * It extends the Tile class and provides functionality specific to path tiles.
 * 
 * 
 * 
 * 
 * @author Atlas Berk Polat
 * @version 1.0
 * @since 2025-04-25
 */

public class PathTile extends Tile {
    // PathTile class extends the Tile class
    // This class represents a path tile in the game

    private boolean isStartingTile = false; // Indicates if this tile is the starting tile of a path
    private boolean isEndingTile = false; // Indicates if this tile is the ending tile of a path

    public PathTile() {

        super(TileType.PATH); // Call the constructor of the parent Tile class with PATH type
        this.isWalkable = true; // Set the walkable status to true
    }
    public PathTile(boolean isStartingTile, boolean isEndingTile) {
        super(TileType.PATH); // Call the constructor of the parent Tile class with PATH type
        this.isWalkable = true; // Set the walkable status to true
        this.isStartingTile = isStartingTile; // Set the starting tile status
        this.isEndingTile = isEndingTile; // Set the ending tile status
    }
    public boolean isStartingTile() {
        return isStartingTile; // Get the starting tile status
    }

    public boolean isEndingTile() {
        return isEndingTile; // Get the ending tile status
    }

    public PathTile getNextTileOnPath() {
        // This method returns the next tile on the path
        // Looks for the next tile in the neighbors array that is a path tile

        // If the tile is the ending tile, return null
        if (isEndingTile) {
            return null; // Return null if this tile is the ending tile
        }

        for (Tile neighbor : neighbors) {
            if (neighbor.getTileType() == TileType.PATH) {
                // Check if the neighbor tile is not the previous tile
                PathTile checkTile = (PathTile) neighbor;
                if(checkTile.getNextTileOnPath() != this) {
                    return checkTile; // Recursively call the next tile on path method
                }

                
            }
        }

        // If no path tile is found, return error
        System.out.println("Error: No path tile found in the neighbors array.");
        return null; // Return null if no path tile is found
        
    }

}
