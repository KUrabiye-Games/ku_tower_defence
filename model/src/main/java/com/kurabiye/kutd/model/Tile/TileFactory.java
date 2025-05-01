package com.kurabiye.kutd.model.Tile;

import com.kurabiye.kutd.util.FactoryPattern.CodeFactory;

/* TileFactory.java
 * This class is responsible for creating tile objects.
 * It is a factory class that can be used to create different types of tiles.
 * 
 */

public class TileFactory implements CodeFactory<Tile> {
    // This method creates a tile object based on the given code.

    /*
     *  if(isIn(tileCode, new int[]{0, 1, 2, 3, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14})) {
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
     * 
     * 
     */
    @Override
    public Tile create(int code) {

        // check if the code is within the range

        if(code < 0 || code > 31) {
            throw new IllegalArgumentException("Invalid tile code: " + code);
        }

        Tile tile = new Tile(code);

        if(isIn(code, new int[]{0, 1, 2, 3, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14})) {
            tile.setPathTile(true);
        }

        if(code == 15) {
            tile.setBuildableTile(true);
        }

        if(code == 5) {
            tile.setGroundTile(true);
        }

        if(isIn(code, new int[]{20, 21, 26})){
            tile.setTowerTile(true);
        }

        if(isIn(code, new int[]{16, 17, 18, 19, 22, 23, 24, 25, 27, 28, 29, 30, 31})) {
            tile.setDecorationTile(true);
        }


        return tile;
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
