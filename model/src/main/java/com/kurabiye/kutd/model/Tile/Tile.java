package com.kurabiye.kutd.model.Tile;

import com.kurabiye.kutd.model.Coordinates.TilePoint2D;

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
        


        /* Tile code is very important for the view layer.
         * It is used to determine which tile to draw on the screen.
         * We will decide the specific tile code for each tile type later.
         * The Map class will use this code to draw the tile on the screen.
         * 
         */
        private int tileCode; // Code for the tile
        

        private TilePoint2D coordinate; // Coordinate of the tile on the map

        /* The neighbors array is used to store the neighboring tiles of this tile.
         * There is a maximum of 8 neighbors for each tile.
         * And there is a minimum of 3 neighbors for each tile.
         * Here is the coding for the neighbors array:
         *      0 1 2
         *      7 X 3
         *      6 5 4 
         */

        public Tile(int tileCode, TilePoint2D coordinate) {
            this.tileCode = tileCode; // Set the tile code
            this.coordinate = coordinate; // Set the coordinate of the tile


            if(isIn(tileCode, new int[]{0, 1, 2, 3, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14})) {
                isPathTile = true; // Set the tile as a path tile
            }

            if(tileCode == 15) {
                isBuildableTile = true; // Set the tile as buildable
            }

            if(tileCode == 5) {
                isGroundTile = true; // Set the tile as a starting tile
            }

            if(isIn(tileCode, new int[]{20, 21, 26})){
                isTowerTile = true; // Set the tile as a tower tile
            }

            if(isIn(tileCode, new int[]{16, 17, 18, 19, 22, 23, 24, 25, 27, 28, 29, 30, 31})) {
                isDecorationTile = true; // Set the tile as a starting tile
            }

            
        }

        private boolean isPathTile = false; // Walkable status of the tile
        private boolean isBuildableTile = false; // Buildable status of the tile
        private boolean isTowerTile = false; // Tower status of the tile
        private boolean isGroundTile = false; // Ground status of the tile
        private boolean isDecorationTile = false; // Decoration status of the tile


        public int getTileCode() {
            return tileCode; // Get the code of the tile
        }

        public boolean isPathTile() {
            return isPathTile; // Check if the tile is a path tile
        }
        public boolean isBuildableTile() {
            return isBuildableTile; // Check if the tile is buildable
        }
        public boolean isGroundTile() {
            return isGroundTile; // Check if the tile is a ground tile
        }
        public boolean isDecorationTile() {
            return isDecorationTile; // Check if the tile is a decoration tile
        }

        public boolean isTowerTile() {
            return isTowerTile; // Check if the tile is a tower tile
        }

        public TilePoint2D getCoordinate() {
            return coordinate; // Get the coordinate of the tile
        }



        // Helper method

        private static boolean isIn(int num, int[] arr){

            for (int i = 0; i < arr.length; i++) {
                if (arr[i] == num) {
                    return true; // Return true if the number is found in the array
                }
            }
            return false; // Return false if the number is not found in the array
        }

}
