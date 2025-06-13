package com.kurabiye.kutd.model.Map;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public final class GameMapRepository {
    private static final String MAPS_DIR = System.getProperty("user.home") + "/.kutd/maps/";
    private static final ObjectMapper mapper = new ObjectMapper()
        .enable(SerializationFeature.INDENT_OUTPUT)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(GameMapRepository.class);
    private static volatile GameMapRepository instance;
    private final Map<String, GameMap> gameMaps;

    private GameMapRepository() {
        this.gameMaps = new HashMap<>();
        loadRepository();
    }

    public static GameMapRepository getInstance() {
        if (instance == null) {
            synchronized (GameMapRepository.class) {
                if (instance == null) {
                    instance = new GameMapRepository();
                }
            }
        }
        return instance;
    }

    public MapOperationResult addGameMap(GameMap map) {
        if (map.getName() == null || map.getName().isEmpty()) {
            return new MapOperationResult(false, "Map name cannot be empty");
        }

        try {
            // Save to temporary file first
            String tempFileName = MAPS_DIR + map.getName() + ".json.tmp";
            String finalFileName = MAPS_DIR + map.getName() + ".json";
            
            // Ensure directory exists
            Files.createDirectories(Paths.get(MAPS_DIR));
            
            // Write to temp file
            mapper.writeValue(new File(tempFileName), map);
            
            // Atomic move
            Files.move(Paths.get(tempFileName), Paths.get(finalFileName), 
                      StandardCopyOption.ATOMIC_MOVE, 
                      StandardCopyOption.REPLACE_EXISTING);
            
            // Update memory cache
            gameMaps.put(map.getName(), map);
            return new MapOperationResult(true, "Map saved successfully");
            
        } catch (IOException e) {
            logger.error("Failed to save map: " + map.getName(), e);
            return new MapOperationResult(false, "Failed to save map: " + e.getMessage());
        }
    }

    private void loadRepository() {
        try {
            Files.createDirectories(Paths.get(MAPS_DIR));
            
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(MAPS_DIR), "*.json")) {
                for (Path mapFile : stream) {
                    try {
                        GameMap map = mapper.readValue(mapFile.toFile(), GameMap.class);
                        if (map.getName() != null) {
                            gameMaps.put(map.getName(), map);
                        }
                    } catch (IOException e) {
                        logger.error("Failed to load map: " + mapFile.getFileName(), e);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Failed to load map repository", e);
            gameMaps.clear();
        }
    }

    public GameMap getGameMap(String name) {
        return gameMaps.get(name);
    }

    public List<String> getAvailableMapNames() {
        return new ArrayList<>(gameMaps.keySet());
    }

    public boolean deleteMap(String mapName) {
        try {
            Path mapPath = Paths.get(MAPS_DIR + mapName + ".json");
            Files.deleteIfExists(mapPath);
            gameMaps.remove(mapName);
            return true;
        } catch (IOException e) {
            logger.error("Failed to delete map: " + mapName, e);
            return false;
        }
    }
}