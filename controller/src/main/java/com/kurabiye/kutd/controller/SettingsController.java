package com.kurabiye.kutd.controller;

import com.kurabiye.kutd.model.Player.UserPreference;
import com.kurabiye.kutd.persistence.SettingsRepository;

public class SettingsController {
    private final SettingsRepository settingsRepository;

    public SettingsController() {
        this.settingsRepository = SettingsRepository.getInstance();
        loadSavedSettings();
    }

    private void loadSavedSettings() {
        UserPreference savedSettings = settingsRepository.loadSettings();
        if (savedSettings != null) {
            UserPreference.applySettings(new UserPreference.Builder(savedSettings));
        }
    }

    public UserPreference getCurrentPreferences() {
        return UserPreference.getInstance();
    }

    public void applyPreferences(UserPreference.Builder builder) {
        UserPreference.applySettings(builder);
        settingsRepository.saveSettings(UserPreference.getInstance());
    }

    public void resetPreferencesToDefault() {
        UserPreference.resetInstance();
        UserPreference newInstance = UserPreference.getInstance();
        settingsRepository.saveSettings(newInstance);
    }
}