package com.kurabiye.kutd.model.Tile;

import com.kurabiye.kutd.model.Coordinates.TileCoordinate;

/* Tile.java
 * This class represents a tile in the game.
 * It is an abstract class that can be extended by other classes to define specific types of tiles.
 * 
 * 
 * @author Atlas Berk Polat
 * @version 1.0
 * @since 2025-04-23
 */

public class Tile {
        public enum TileType { // Enum for different tile types
        GROUND, // Grass tile type
        PATH,
        BUILD_POINT,
        DECORATION,
        SPACE // Space tile type this tile is not used in the game it is just a placeholder for the edges and corners of the map
    }

        public Tile(TileType tileType) {
            this.tileType = tileType; // Set the type of the tile
        }

        protected TileType tileType; // Type of the tile

        /* Tile code is very important for the view layer.
         * It is used to determine which tile to draw on the screen.
         * We will decide the specific tile code for each tile type later.
         * The Map class will use this code to draw the tile on the screen.
         * 
         */
        protected int tileCode; // Code for the tile
        

        protected TileCoordinate coordinate; // Coordinate of the tile on the map

        /* The neighbors array is used to store the neighboring tiles of this tile.
         * There is a maximum of 8 neighbors for each tile.
         * And there is a minimum of 3 neighbors for each tile.
         * Here is the coding for the neighbors array:
         *      0 1 2
         *      7 X 3
         *      6 5 4 
         */
        protected Tile[] neighbors = new Tile[8]; // Array of neighboring tiles

        protected boolean isWalkable = false; // Walkable status of the tile
        protected boolean isBuildable = false; // Buildable status of the tile
        protected boolean isDecoratable = false; // Decoratable status of the tile

        public TileType getTileType() {
            return tileType; // Get the type of the tile
        }
        public int getTileCode() {
            return tileCode; // Get the code of the tile
        }
        
        public Tile[] getNeighbors() {
            return neighbors; // Get the neighboring tiles
        }

}
