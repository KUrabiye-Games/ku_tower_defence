package com.kurabiye.kutd.model.Tile;

/* TileFactory.java
 * This class is responsible for creating tile objects.
 * It is a factory class that can be used to create different types of tiles.
 * 
 */

public class TileFactory {

    public static Tile createTile(Tile.TileType tileType) {
        switch (tileType) {
            case GROUND:
                return new Tile(tileType); // Create a ground tile
            case PATH:
                return new PathTile(); // Create a path tile
            case BUILD_POINT:
                return new BuildTile(); // Create a buildable tile
            case DECORATION:
                return new Tile(tileType); // Create a decoration tile
            case SPACE:
                return new Tile(tileType); // Create a space tile
            default:
                throw new IllegalArgumentException("Invalid tile type: " + tileType); // Throw an exception for invalid tile type
        }
    }



}
