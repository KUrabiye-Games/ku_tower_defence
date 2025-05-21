
package com.kurabiye.kutd.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kurabiye.kutd.model.Player.UserPreference;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SettingsRepository {
    private static final String SETTINGS_DIR = System.getProperty("user.home") + "/.kutd/";
    private static final String SETTINGS_FILE = "settings.json";
    private static final ObjectMapper mapper = new ObjectMapper();
    
    private static SettingsRepository instance;
    
    // Private constructor to prevent instantiation
    private SettingsRepository() {}
    
    public static synchronized SettingsRepository getInstance() {
        if (instance == null) {
            instance = new SettingsRepository();
        }
        return instance;
    }
    
    public void saveSettings(UserPreference preferences) {
        try {
            Files.createDirectories(Paths.get(SETTINGS_DIR));
            File settingsFile = new File(SETTINGS_DIR + SETTINGS_FILE);
            mapper.writeValue(settingsFile, preferences);
        } catch (IOException e) {
            System.err.println("Failed to save settings: " + e.getMessage());
        }
    }
    
    public UserPreference loadSettings() {
        File settingsFile = new File(SETTINGS_DIR + SETTINGS_FILE);
        if (!settingsFile.exists()) {
            return null;
        }
        
        try {
            return mapper.readValue(settingsFile, UserPreference.class);
        } catch (IOException e) {
            System.err.println("Failed to load settings: " + e.getMessage());
            return null;
        }
    }
}