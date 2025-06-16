package com.kurabiye.kutd.controller;

import com.kurabiye.kutd.model.Coordinates.TilePoint2D;
import com.kurabiye.kutd.model.Map.*;
import com.kurabiye.kutd.model.Tile.*;
import java.util.List;

public class MapEditorController {
    private String currentMapName;
    private final GameMapRepository mapRepository;
    private final TileFactory tileFactory;
    private int[][] tileCodeMatrix;
    private TilePoint2D startPoint;
    private TilePoint2D endPoint;
    private static final int DEFAULT_TILE = 5; // grass tile

    public MapEditorController() {
        this.mapRepository = GameMapRepository.getInstance();
        this.tileFactory = new TileFactory();
        this.currentMapName = null;
        initializeTileMatrix();
    }

    public void initializeTileMatrix() {
        tileCodeMatrix = new int[GameMap.MAP_HEIGHT][GameMap.MAP_WIDTH];
        for (int i = 0; i < GameMap.MAP_HEIGHT; i++) {
            for (int j = 0; j < GameMap.MAP_WIDTH; j++) {
                tileCodeMatrix[i][j] = DEFAULT_TILE; // Initialize with grass tiles
            }
        }
        startPoint = null;
        endPoint = null;
    }

    public void setStartPoint(int x, int y) {
        if (isValidPosition(x, y)) {
            startPoint = new TilePoint2D(x, y);
        }
    }

    public void setEndPoint(int x, int y) {
        if (isValidPosition(x, y)) {
            endPoint = new TilePoint2D(x, y);
        }
    }

    public MapOperationResult saveMap(String mapName) {
        if (!isValidMapName(mapName)) {
            return new MapOperationResult(false, "Invalid map name");
        }

        if (startPoint == null || endPoint == null) {
            return new MapOperationResult(false, "Start and end points must be set");
        }

        try {
            GameMap gameMap = createGameMapFromMatrix(mapName);
            
            // Check if we're editing an existing map
            if (currentMapName != null && currentMapName.equals(mapName)) {
                // Update existing map
                return mapRepository.addGameMap(gameMap);
            } else {
                // Check if a map with this name already exists
                if (mapRepository.getGameMap(mapName) != null) {
                    return new MapOperationResult(false, 
                        "A map with this name already exists. Choose a different name.");
                }
                // Save as new map
                currentMapName = mapName;
                return mapRepository.addGameMap(gameMap);
            }
        } catch (IllegalArgumentException e) {
            return new MapOperationResult(false, e.getMessage());
        }
    }

    private GameMap createGameMapFromMatrix(String mapName) {
        Tile[][] tiles = new Tile[GameMap.MAP_HEIGHT][GameMap.MAP_WIDTH];
        for (int i = 0; i < GameMap.MAP_HEIGHT; i++) {
            for (int j = 0; j < GameMap.MAP_WIDTH; j++) {
                tiles[i][j] = tileFactory.create(tileCodeMatrix[i][j]);
                tiles[i][j].setCoordinate(new TilePoint2D(j, i));
            }
        }

        try {
            GameMap gameMap = new GameMap(tiles, startPoint, endPoint, mapName);
            return gameMap;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid map configuration: " + e.getMessage());
        }
    }

    public GameMap loadMap(String mapName) {
        GameMap loadedMap = mapRepository.getGameMap(mapName);
        if (loadedMap != null) {
            loadMapIntoMatrix(loadedMap);
            currentMapName = mapName; // Set current map name for editing
            return loadedMap;
        }
        return null;
    }

    private void loadMapIntoMatrix(GameMap map) {
        Tile[][] mapTiles = map.getTiles();
        startPoint = map.getStartTileCoordinates();
        endPoint = map.getEndTileCoordinates();
        
        // Convert tiles back to tile codes
        for (int i = 0; i < GameMap.MAP_HEIGHT; i++) {
            for (int j = 0; j < GameMap.MAP_WIDTH; j++) {
                tileCodeMatrix[i][j] = mapTiles[i][j].getTileCode();
            }
        }
    }

    public int[][] getTileCodeMatrix() {
        return tileCodeMatrix;
    }

    public boolean placeTile(int x, int y, int tileCode) {
        if (!isValidPosition(x, y)) {
            return false;
        }
        tileCodeMatrix[y][x] = tileCode;
        return true;
    }

    public List<String> getAvailableMapNames() {
        return mapRepository.getAvailableMapNames();
    }

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < GameMap.MAP_WIDTH && y >= 0 && y < GameMap.MAP_HEIGHT;
    }

    private boolean isValidMapName(String mapName) {
        return mapName != null && !mapName.trim().isEmpty();
    }

    public void clearMap() {
        initializeTileMatrix();
        startPoint = null;
        endPoint = null;
        currentMapName = null;
    }
    public boolean deleteMap(String mapName) {
        return mapRepository.deleteMap(mapName);
    }

    public TilePoint2D getStartPoint() {
        return startPoint;
    }

    public TilePoint2D getEndPoint() {
        return endPoint;
    }

    public String getCurrentMapName() {
        return currentMapName;
    }
}