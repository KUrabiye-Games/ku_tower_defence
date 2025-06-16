package com.kurabiye.kutd.controller;

import com.kurabiye.kutd.model.Map.GameMap;
import com.kurabiye.kutd.model.Map.GameMapRepository;
import java.util.List;

public class MapSelectionController {
    private final GameMapRepository mapRepository;

    public MapSelectionController() {
        this.mapRepository = GameMapRepository.getInstance();
    }

    public List<String> getAvailableMapNames() {
        return mapRepository.getAvailableMapNames();
    }

    public GameMap getMap(String mapName) {
        return mapRepository.getGameMap(mapName);
    }
}