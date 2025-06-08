package com.kurabiye.kutd.controller;

import com.kurabiye.kutd.model.Coordinates.TilePoint2D;
import com.kurabiye.kutd.model.Map.*;
import com.kurabiye.kutd.model.Tile.*;
import java.util.List;

public class MapEditorController {
    private final GameMapRepository mapRepository;
    private GameMap currentMap;
    private final TileFactory tileFactory;

    public MapEditorController() {
        this.mapRepository = GameMapRepository.getInstance();
        this.tileFactory = new TileFactory();
        initializeNewMap();
    }

    private void initializeNewMap() {
        Tile[][] tiles = new Tile[GameMap.MAP_HEIGHT][GameMap.MAP_WIDTH];
        for (int i = 0; i < GameMap.MAP_HEIGHT; i++) {
            for (int j = 0; j < GameMap.MAP_WIDTH; j++) {
                tiles[i][j] = tileFactory.create(5); // Default grass tile
            }
        }
        currentMap = new GameMap();
        currentMap.setTiles(tiles);
    }

    public ValidationResult validateMap() {
        try {
            TilePoint2D startPoint = findSpecialTile(0); // Start tile
            TilePoint2D endPoint = findSpecialTile(14); // End tile
            
            if (startPoint == null || endPoint == null) {
                return new ValidationResult(false, "Map must have start (0) and end (14) tiles!");
            }
            
            GameMapValidator.isValidGameMap(currentMap.getTiles(), startPoint, endPoint);
            return new ValidationResult(true, "Map is valid!");
        } catch (IllegalArgumentException e) {
            return new ValidationResult(false, e.getMessage());
        }
    }

    public MapOperationResult saveMap(String mapName) {
        ValidationResult validationResult = validateMap();
        if (!validationResult.isValid()) {
            return new MapOperationResult(false, validationResult.getMessage());
        }
        
        try {
            currentMap.setName(mapName);
            mapRepository.addGameMap(currentMap);
            return new MapOperationResult(true, "Map saved successfully!");
        } catch (Exception e) {
            return new MapOperationResult(false, "Failed to save map: " + e.getMessage());
        }
    }

    public GameMap loadMap(String mapName) {
        return mapRepository.getGameMap(mapName);
    }

    public List<String> getAvailableMapNames() {
        return mapRepository.getAvailableMapNames();
    }

    public boolean placeTile(int x, int y, int tileCode) {
        try {
            Tile newTile = tileFactory.create(tileCode);
            currentMap.setTile(x, y, newTile);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private TilePoint2D findSpecialTile(int tileCode) {
        for (int i = 0; i < GameMap.MAP_HEIGHT; i++) {
            for (int j = 0; j < GameMap.MAP_WIDTH; j++) {
                if (currentMap.getTile(j, i).getTileCode() == tileCode) {
                    return new TilePoint2D(j, i);
                }
            }
        }
        return null;
    }

    public GameMap getCurrentMap() {
        return currentMap;
    }
}

// Result classes to encapsulate operation results
class ValidationResult {
    private final boolean valid;
    private final String message;

    public ValidationResult(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    public boolean isValid() { return valid; }
    public String getMessage() { return message; }
}

