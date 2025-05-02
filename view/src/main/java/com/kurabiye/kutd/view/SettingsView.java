package com.kurabiye.kutd.view;

import com.kurabiye.kutd.model.Player.UserPreference;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SettingsView extends Application {

    private final UserPreference userPreference = UserPreference.getInstance();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Game Settings");

        // User name
        TextField userNameField = new TextField(userPreference.getUserName());

        // Music volume
        Slider musicSlider = new Slider(0, 1, userPreference.getMusicVolume());
        musicSlider.setShowTickLabels(true);
        musicSlider.setShowTickMarks(true);

        // Sound volume
        Slider soundSlider = new Slider(0, 1, userPreference.getSoundVolume());
        soundSlider.setShowTickLabels(true);
        soundSlider.setShowTickMarks(true);

        // Delay between waves
        Spinner<Integer> waveDelaySpinner = new Spinner<>(1000, 10000, userPreference.getDelayBetweenWaves(), 500);

        // Delay between groups
        Spinner<Integer> groupDelaySpinner = new Spinner<>(1000, 10000, userPreference.getDelayBetweenGroups(), 500);

        // Starting gold
        Spinner<Integer> goldSpinner = new Spinner<>(0, 10000, userPreference.getStartingGold(), 50);

        // Starting health
        Spinner<Integer> healthSpinner = new Spinner<>(0, 1000, userPreference.getStartingHealth(), 10);

        // Save button
        Button saveButton = new Button("Save Settings");
        saveButton.setOnAction(e -> saveSettings(
            userNameField.getText(),
            (float) musicSlider.getValue(),
            (float) soundSlider.getValue(),
            waveDelaySpinner.getValue(),
            groupDelaySpinner.getValue(),
            goldSpinner.getValue(),
            healthSpinner.getValue()
        ));

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("User Name:"), 0, 0);
        grid.add(userNameField, 1, 0);

        grid.add(new Label("Music Volume:"), 0, 1);
        grid.add(musicSlider, 1, 1);

        grid.add(new Label("Sound Volume:"), 0, 2);
        grid.add(soundSlider, 1, 2);

        grid.add(new Label("Delay Between Waves (ms):"), 0, 3);
        grid.add(waveDelaySpinner, 1, 3);

        grid.add(new Label("Delay Between Groups (ms):"), 0, 4);
        grid.add(groupDelaySpinner, 1, 4);

        grid.add(new Label("Starting Gold:"), 0, 5);
        grid.add(goldSpinner, 1, 5);

        grid.add(new Label("Starting Health:"), 0, 6);
        grid.add(healthSpinner, 1, 6);

        grid.add(saveButton, 1, 7);

        Scene scene = new Scene(grid, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void saveSettings(String userName, float musicVol, float soundVol,
                               int waveDelay, int groupDelay, int gold, int health) {
        // Use controller to apply new settings
        UserPreference.Builder builder = new UserPreference.Builder(UserPreference.getInstance())
                .setUserName(userName)
                .setMusicVolume(musicVol)
                .setSoundVolume(soundVol)
                .setDelayBetweenWaves(waveDelay)
                .setDelayBetweenGroups(groupDelay)
                .setStartingGold(gold)
                .setStartingHealth(health);

        UserPreference.applySettings(builder);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Settings Saved");
        alert.setHeaderText(null);
        alert.setContentText("Your settings have been saved successfully.");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
