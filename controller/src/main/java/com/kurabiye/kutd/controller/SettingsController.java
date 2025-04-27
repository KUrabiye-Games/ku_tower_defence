package com.kurabiye.kutd.controller;

import com.kurabiye.kutd.view.SettingsView;
import com.kurabiye.kutd.persistence.SettingsManager;
import com.kurabiye.kutd.model.Settings;

public class SettingsController extends Controller{
    private SettingsView settingsView;
    private SettingsManager settingsManager;
    private Settings settings;

    public SettingsController(SettingsView settingsView, SettingsManager settingsManager) {
        this.settingsView = settingsView;
        this.settingsManager = settingsManager;
        this.settings = settingsManager.loadSettings();
        initialize();
    }

    @Override
    protected void initialize() {
        // Initialize view with loaded settings
        settingsView.displaySettings(settings);

        // Hook up UI events to saving logic
        settingsView.setOnSaveAction(newSettings -> onSaveSettings(newSettings));
        settingsView.setOnResetDefaultsAction(() -> onResetDefaults());
    }

    private void onSaveSettings(Settings newSettings) {
        System.out.println("Saving new settings...");
        this.settings = newSettings;
        settingsManager.saveSettings(settings);
    }

    private void onResetDefaults() {
        System.out.println("Resetting settings to defaults...");
        this.settings = Settings.getDefaultSettings();
        settingsView.displaySettings(settings);
        settingsManager.saveSettings(settings);
    }
}