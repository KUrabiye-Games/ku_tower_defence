package com.kurabiye.kutd.model.Map;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GameMapRepository implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String REPOSITORY_FILE = System.getProperty("user.home") + "/.kutd/maps/repository.dat";
    private static GameMapRepository instance;
    private Map<String, GameMap> gameMaps;

    private GameMapRepository() {
        this.gameMaps = new HashMap<>();
        loadRepository();
    }

    public static synchronized GameMapRepository getInstance() {
        if (instance == null) {
            instance = new GameMapRepository();
        }
        return instance;
    }

    public MapOperationResult addGameMap(GameMap map) {
        if (map.getName() == null || map.getName().isEmpty()) {
            return new MapOperationResult(false, "Map name cannot be empty");
        }

        gameMaps.put(map.getName(), map);
        boolean saved = saveRepository();
        return new MapOperationResult(saved, 
            saved ? "Map saved successfully" : "Failed to save map");
    }

    public GameMap getGameMap(String name) {
        return gameMaps.get(name);
    }

    public List<String> getAvailableMapNames() {
        return new ArrayList<>(gameMaps.keySet());
    }

    private boolean saveRepository() {
        try {
            File directory = new File(REPOSITORY_FILE).getParentFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(REPOSITORY_FILE))) {
                oos.writeObject(this);
            }
            return true;
        } catch (IOException e) {
            System.err.println("Failed to save repository: " + e.getMessage());
            return false;
        }
    }

    private void loadRepository() {
        File file = new File(REPOSITORY_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(file))) {
                GameMapRepository loaded = (GameMapRepository) ois.readObject();
                this.gameMaps = loaded.gameMaps;
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Failed to load repository: " + e.getMessage());
                this.gameMaps = new HashMap<>();
            }
        }
    }

    private Object readResolve() {
        instance = this;
        return this;
    }
}