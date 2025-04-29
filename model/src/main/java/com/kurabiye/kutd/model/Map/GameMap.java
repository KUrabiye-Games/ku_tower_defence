package com.kurabiye.kutd.model.Map;

import java.util.List;

import com.kurabiye.kutd.model.Coordinates.TilePoint2D;
import com.kurabiye.kutd.model.Tile.Tile;

public class GameMap {

    public static final int MAP_WIDTH = 16; // Width of the map
    public static final int MAP_HEIGHT = 9; // Height of the map

    private Tile[][] tiles; // 2D array representing the tiles on the map

    private TilePoint2D startTile; // Starting tile of the map
    private TilePoint2D endTile; // Ending tile of the map

    private List<TilePoint2D> path; // List of path tiles on the map


    public GameMap() {
        tiles = new Tile[MAP_WIDTH][MAP_HEIGHT]; // Initialize the tiles array
    }

    public GameMap(Tile[][] tiles) {
        if (tiles.length != MAP_WIDTH || tiles[0].length != MAP_HEIGHT) {
            throw new IllegalArgumentException("Invalid tile array dimensions");
        }
        this.tiles = tiles; // Initialize the tiles array with the provided tiles
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || x >= MAP_WIDTH || y < 0 || y >= MAP_HEIGHT) {
            throw new IllegalArgumentException("Coordinates out of bounds");
        }
        return tiles[x][y]; // Return the tile at the specified coordinates
    }

    public void setTile(int x, int y, Tile tile) {
        if (x < 0 || x >= MAP_WIDTH || y < 0 || y >= MAP_HEIGHT) {
            throw new IllegalArgumentException("Coordinates out of bounds");
        }
        tiles[x][y] = tile; // Set the tile at the specified coordinates
    }

    public TilePoint2D getStartTile() {
        return startTile; // Return the starting tile
    }

    public void setStartTile(TilePoint2D startTile) {
        this.startTile = startTile; // Set the starting tile
    }

    public TilePoint2D getEndTile() {
        return endTile; // Return the ending tile
    }

    public void setEndTile(TilePoint2D endTile) {
        this.endTile = endTile; // Set the ending tile
    }

    public List<TilePoint2D> getPath() {
        return path; // Return the list of path tiles
    }

    public void setPath(List<TilePoint2D> path) {
        this.path = path; // Set the list of path tiles
    }

}
