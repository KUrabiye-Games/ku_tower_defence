package com.kurabiye.kutd.model.Map;

import java.io.Serializable;
import java.util.List;

/* * GameMapRepository.java
 * This class is responsible for managing the game map data, including loading and saving map data,
 * and providing access to the map data for other components of the game.
 * 
 * 
 * @author: Atlas Berk Polat
 * @version: 1.0
 * @since: 2025-04-28
 */

public class GameMapRepository implements Serializable{

        private static final long serialVersionUID = 1L; // Unique ID for serialization

        private static GameMapRepository instance; // Singleton instance of GameMapRepository

        private List<GameMap> gameMaps; // List of game maps

        private GameMapRepository() {
            // Private constructor to prevent instantiation
        }


        public static GameMapRepository getInstance() {
            if (instance == null) {
                instance = new GameMapRepository(); // Create a new instance if it doesn't exist
            }
            return instance; // Return the singleton instance
        }


        public List<GameMap> getGameMaps() {
            return gameMaps; // Return the list of game maps
        }

        public void addGameMap(GameMap gameMap) {
            gameMaps.add(gameMap); // Add a new game map to the list
        }

        public void removeGameMap(GameMap gameMap) {
            gameMaps.remove(gameMap); // Remove a game map from the list
        }

        public void removeGameMap(int index) {
            if (index >= 0 && index < gameMaps.size()) {
                gameMaps.remove(index);
            } else {
                throw new IndexOutOfBoundsException("Invalid map index: " + index);
            }
        }

        private Object readResolve() {
            // Ensure that the singleton instance is returned during deserialization
            return getInstance();
        }

}
