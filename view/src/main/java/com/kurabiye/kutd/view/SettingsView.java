package com.kurabiye.kutd.view;

import com.kurabiye.kutd.controller.SettingsController;
import com.kurabiye.kutd.model.Player.UserPreference;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SettingsView {

    public void show(Stage stage, SettingsController controller) {
        // Create a new stage for settings
        Stage settingsStage = new Stage();
        
        // Current preference values
        UserPreference current = controller.getCurrentPreferences();

        // UI Controls
        TextField usernameField = new TextField(current.getUserName());
        Slider musicVolumeSlider = new Slider(0, 1, current.getMusicVolume());
        musicVolumeSlider.setShowTickMarks(true);
        musicVolumeSlider.setShowTickLabels(true);
        
        Slider soundVolumeSlider = new Slider(0, 1, current.getSoundVolume());
        soundVolumeSlider.setShowTickMarks(true);
        soundVolumeSlider.setShowTickLabels(true);

        Button applyButton = new Button("Apply");
        Button resetButton = new Button("Reset to Defaults");
        Button returnButton = new Button("Return to Menu");
        
        // Style the return button to match main menu style
        returnButton.setPrefWidth(200);
        returnButton.setPrefHeight(40);
        returnButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #4CAF50, #2E7D32);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-background-radius: 10;"
        );
        
        returnButton.setOnMouseEntered(e -> returnButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #66BB6A, #388E3C);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-background-radius: 10;"
        ));
        
        returnButton.setOnMouseExited(e -> returnButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #4CAF50, #2E7D32);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-background-radius: 10;"
        ));

        // Actions
        applyButton.setOnAction(e -> {
            UserPreference.Builder builder = new UserPreference.Builder()
                    .setUserName(usernameField.getText())
                    .setMusicVolume((float)musicVolumeSlider.getValue())
                    .setSoundVolume((float)soundVolumeSlider.getValue());

            controller.applyPreferences(builder);
            showAlert("Preferences applied!");
        });

        resetButton.setOnAction(e -> {
            controller.resetPreferencesToDefault();

            // Refresh UI with new default values
            UserPreference defaults = controller.getCurrentPreferences();
            usernameField.setText(defaults.getUserName());
            musicVolumeSlider.setValue(defaults.getMusicVolume());
            soundVolumeSlider.setValue(defaults.getSoundVolume());

            showAlert("Preferences reset to defaults.");
        });
        
        returnButton.setOnAction(e -> {
            settingsStage.close();
            stage.show();
        });

        // Layout
        VBox layout = new VBox(10,
                new Label("Username:"), usernameField,
                new Label("Music Volume:"), musicVolumeSlider,
                new Label("Sound Volume:"), soundVolumeSlider,
                new HBox(10, applyButton, resetButton),
                new HBox(10, returnButton)
        );
        layout.setPadding(new Insets(20));

        settingsStage.setScene(new Scene(layout));
        settingsStage.setTitle("Settings");
        settingsStage.show();
        
        // Hide main stage while settings are open
        stage.hide();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}