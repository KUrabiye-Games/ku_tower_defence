package com.kurabiye.kutd.model.Tile;
import com.kurabiye.kutd.model.Coordinates.TileCoordinate;

public interface ITile {
 
    
    TileCoordinate getTileCoordinate(); // Getter for tile coordinate
    
    boolean isWalkable(); // Check if the tile is walkable
    boolean isBuildable(); // Check if the tile is buildable
    boolean isAttackable(); // Check if the tile is attackable
    boolean isVisible(); // Check if the tile is visible
    boolean isOccupied(); // Check if the tile is occupied
    

}
