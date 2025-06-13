package com.kurabiye.kutd.model.Tile;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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

 @JsonIgnoreProperties(ignoreUnknown = true)
public class Tile implements Serializable {
        private static final long serialVersionUID = 1L;


        /* Tile code is very important for the view layer.
         * It is used to determine which tile to draw on the screen.
         * We will decide the specific tile code for each tile type later.
         * The Map class will use this code to draw the tile on the screen.
         * 
         */
        private int tileCode; // Code for the tile

        /* The tileDirections array is used to store the directions of the tile.
         *         1
         *    0 - Tile - 2
         *         3
         */
        private int tileDirections[];
        

        private TilePoint2D coordinate; // Coordinate of the tile on the map

        /* The neighbors array is used to store the neighboring tiles of this tile.
         * There is a maximum of 8 neighbors for each tile.
         * And there is a minimum of 3 neighbors for each tile.
         * Here is the coding for the neighbors array:
         *      0 1 2
         *      7 X 3
         *      6 5 4 
         */

        @JsonCreator
        public Tile( @JsonProperty("tileCode") int tileCode) {
            this.tileCode = tileCode; // Set the tile code


            
        }

        private boolean isPathTile = false; // Walkable status of the tile
        private boolean isBuildableTile = false; // Buildable status of the tile
        private boolean isTowerTile = false; // Tower status of the tile
        private boolean isGroundTile = false; // Ground status of the tile
        private boolean isDecorationTile = false; // Decoration status of the tile

        @JsonProperty("tileCode")
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


        public void setTileCode(int tileCode) {
            this.tileCode = tileCode; // Set the code of the tile
        }
        public void setPathTile(boolean isPathTile) {
            this.isPathTile = isPathTile; // Set the tile as a path tile
        }
        public void setBuildableTile(boolean isBuildableTile) {
            this.isBuildableTile = isBuildableTile; // Set the tile as buildable
        }
        public void setGroundTile(boolean isGroundTile) {
            this.isGroundTile = isGroundTile; // Set the tile as a ground tile
        }
        public void setDecorationTile(boolean isDecorationTile) {
            this.isDecorationTile = isDecorationTile; // Set the tile as a decoration tile
        }
        public void setTowerTile(boolean isTowerTile) {
            this.isTowerTile = isTowerTile; // Set the tile as a tower tile
        }

        public void setCoordinate(TilePoint2D coordinate) {
            this.coordinate = coordinate; // Set the coordinate of the tile
        }




        public TilePoint2D getCoordinate() {
            return coordinate; // Get the coordinate of the tile
        }


        public int[] getTileDirections() {
            return tileDirections; // Get the directions of the tile
        }
        public void setTileDirections(int[] tileDirections) {
            this.tileDirections = tileDirections; // Set the directions of the tile
        }



        

}
