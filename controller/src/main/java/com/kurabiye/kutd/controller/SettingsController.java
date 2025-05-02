package com.kurabiye.kutd.controller;

import com.kurabiye.kutd.model.Player.UserPreference;

public class SettingsController {

    /**
     * Returns the current user preferences instance (default if not modified).
     */
    public UserPreference getCurrentPreferences() {
        return UserPreference.getInstance();
    }

    /**
     * Applies new user settings using a Builder.
     * This updates the singleton instance with the provided configuration.
     */
    public void applyPreferences(UserPreference.Builder builder) {
        UserPreference.applySettings(builder);
    }

    /**
     * Resets the preferences to their default values.
     */
    public void resetPreferencesToDefault() {
        UserPreference.resetInstance(); // Discard current instance
        // No need to manually set defaults — the singleton does it on creation
        UserPreference.getInstance(); // Reinitialize with default values
    }
}

