package com.kurabiye.kutd.model.Tile;

import com.kurabiye.kutd.util.FactoryPattern.CodeFactory;

/** TileFactory.java
 * This class is responsible for creating tile objects.
 * It is a factory class that can be used to create different types of tiles.
 * 
 * 
 * @author Atlas Berk Polat
 * @version 2.0
 * @since 2025-06-03
 */

public class TileFactory implements CodeFactory<Tile> {

    /**
     * Creates a Tile object based on the provided code.
     * @requires code >= 0 && code <= 31
     * @param code The code representing the type of tile to create.
     * @return A Tile object corresponding to the provided code.
     */
    @Override
    public Tile create(int code) {

        // check if the code is within the range

        if(code < 0 || code > 34) {
            throw new IllegalArgumentException("Invalid tile code: " + code);
        }

        Tile tile = new Tile(code);

        if(isIn(code, new int[]{0, 1, 2, 3, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14})) {
            tile.setPathTile(true);

            /*
             * Set up the tile directions
             *         1
             *    0 - Tile - 2
             *         3
             */

             int dir1 = -1;
             int dir2 = -1;
            
            // 0
            if (isIn(code, new int[]{1, 2, 9, 10, 13, 14})) {
                dir1 = 0; // Set direction 1 to 0
                
            }
            // 1
            if (isIn(code, new int[]{4, 6, 7, 8, 10, 11})) {
                if (dir1 == -1) {
                    dir1 = 1; // Set direction 1 to 1            
                }else {
                    dir2 = 1; // Set direction 2 to 1
                }
            }

            // 2

            if (isIn(code, new int[]{0, 1, 8, 9, 12, 13})) {
                if (dir1 == -1) {
                    dir1 = 2; // Set direction 1 to 2
                }else {
                    dir2 = 2; // Set direction 2 to 2
                }
            }

            // 3

            if (isIn(code, new int[]{0, 2, 3, 4, 6, 7})) {
                if (dir1 == -1) {
                    dir1 = 3; // Set direction 1 to 3
                }else {
                    dir2 = 3; // Set direction 2 to 3
                }
            }


            int[] tileDirections = new int[]{dir1, dir2};

            tile.setTileDirections(tileDirections);
        }

        if(code == 15) {
            tile.setBuildableTile(true);
        }

        if(code == 5) {
            tile.setGroundTile(true);
        }

        if(isIn(code, new int[]{20, 21, 26, 32, 33, 34})){ //32,33 and 34 are upgraded
            tile.setTowerTile(true);
        }

        if(isIn(code, new int[]{16, 17, 18, 19, 22, 23, 24, 25, 27, 28, 29, 30, 31})) {
            tile.setDecorationTile(true);
        }


        return tile;
    }


    
    /**
     * Checks if a number is present in an array.
     * @param num
     * @param arr
     * @return
     */
    private static boolean isIn(int num, int[] arr){

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == num) {
                return true; // Return true if the number is found in the array
            }
        }
        return false; // Return false if the number is not found in the array
    }

    



}
