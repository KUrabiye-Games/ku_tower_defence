package com.kurabiye.kutd.model.Tile;

import com.kurabiye.kutd.model.Tower.Tower;

/* BuildTile.java
 * This class represents a buildable tile in the game.
 * It is a subclass of the Tile class and can be used to define specific types of buildable tiles.
 * 
 * @author Atlas Berk Polat
 * @version 1.0
 * @since 2025-04-25
 * 
 */

public class BuildTile extends Tile {

    private boolean hasTower = false; // Indicates if the tile has a tower built on it

    private Tower builtTower;

    public BuildTile() {
        super(TileType.BUILD_POINT); // Call the constructor of the parent class (Tile)
        this.isBuildable = true; // Set the buildable status to true
    }

    public boolean hasTower() {
        return hasTower; // Get the status of the tower on the tile
    }
    public void setTower(Tower tower) {
        this.builtTower = tower; // Set the tower on the tile
        this.hasTower = true; // Update the status of the tower on the tile
    }
    public Tower getTower() {
        return builtTower; // Get the tower on the tile
    }
    public void removeTower() {
        this.builtTower = null; // Remove the tower from the tile
        this.hasTower = false; // Update the status of the tower on the tile
    }

}
