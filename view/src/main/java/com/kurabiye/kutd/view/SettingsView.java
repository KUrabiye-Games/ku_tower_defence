package com.kurabiye.kutd.view;

import com.kurabiye.kutd.controller.SettingsController;
import com.kurabiye.kutd.model.Player.UserPreference;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Random;

public class SettingsView {

    public void show(Stage stage, SettingsController controller) {
        // Create a new stage for settings
        Stage settingsStage = new Stage();
        
        // Current preference values
        UserPreference current = controller.getCurrentPreferences();

        // Create main layout
        BorderPane root = new BorderPane();
        
   
        try {
            Image backgroundImage = new Image(getClass().getClassLoader().getResource("assets/castle_loadingbg.png").toExternalForm());
            ImageView backgroundView = new ImageView(backgroundImage);
            backgroundView.setFitWidth(800);
            backgroundView.setFitHeight(600);
            backgroundView.setPreserveRatio(false); 
            
            // Add a semi-transparent overlay
            Region overlay = new Region();
            overlay.setPrefSize(800, 600);
            overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.75);");

    
            
            // Stack background and overlay
            StackPane backgroundStack = new StackPane(backgroundView, overlay);
            root.setCenter(backgroundStack);
        } catch (Exception e) {
            // Fallback if image can't be loaded
            root.setStyle("-fx-background-color: #212121;"); 
        }
        
        // Create title
        Text titleText = new Text("GAME SETTINGS");
        titleText.setFont(Font.font("System", FontWeight.BOLD, 36));
        titleText.setFill(Color.GOLD);
        
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.BLACK);
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        titleText.setEffect(dropShadow);
        
        // Create settings panels
        VBox settingsContainer = new VBox(20);
        settingsContainer.setAlignment(Pos.TOP_CENTER);
        settingsContainer.setPadding(new Insets(30, 40, 30, 40));
        settingsContainer.getChildren().add(titleText);
        
        // Player settings
        VBox playerSettings = createSettingsPanel("PLAYER SETTINGS");
        
        TextField usernameField = new TextField(current.getUserName());
        styleTextField(usernameField);
        
        addLabelAndControl(playerSettings, "Commander Name:", usernameField);
        
        // Audio settings
        VBox audioSettings = createSettingsPanel("AUDIO SETTINGS");
        
        Slider musicVolumeSlider = new Slider(0, 1, current.getMusicVolume());
        styleSlider(musicVolumeSlider);
        
        Slider soundVolumeSlider = new Slider(0, 1, current.getSoundVolume());
        styleSlider(soundVolumeSlider);
        
        // Add volume percentage labels
        Label musicValueLabel = new Label(Math.round(current.getMusicVolume() * 100) + "%");
        styleValueLabel(musicValueLabel);
        
        Label soundValueLabel = new Label(Math.round(current.getSoundVolume() * 100) + "%");
        styleValueLabel(soundValueLabel);
        
        // Update labels when sliders change
        musicVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> 
            musicValueLabel.setText(Math.round(newVal.doubleValue() * 100) + "%"));
            
        soundVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> 
            soundValueLabel.setText(Math.round(newVal.doubleValue() * 100) + "%"));
        
        HBox musicBox = new HBox(10, musicVolumeSlider, musicValueLabel);
        musicBox.setAlignment(Pos.CENTER_LEFT);
        
        HBox soundBox = new HBox(10, soundVolumeSlider, soundValueLabel);
        soundBox.setAlignment(Pos.CENTER_LEFT);
        
        addLabelAndControl(audioSettings, "Music Volume:", musicBox);
        addLabelAndControl(audioSettings, "Sound Effects:", soundBox);
        
        // Game settings
        VBox gameSettings = createSettingsPanel("GAME SETTINGS");
        
        TextField delayBetweenWavesField = new TextField(String.valueOf(current.getDelayBetweenWaves()));
        styleTextField(delayBetweenWavesField);
        
        TextField delayBetweenGroupsField = new TextField(String.valueOf(current.getDelayBetweenGroups()));
        styleTextField(delayBetweenGroupsField);
        
        TextField startingGoldField = new TextField(String.valueOf(current.getStartingGold()));
        styleTextField(startingGoldField);
        
        TextField startingHealthField = new TextField(String.valueOf(current.getStartingHealth()));
        styleTextField(startingHealthField);
        
        TextField artilleryRangeField = new TextField(String.valueOf(current.getArtilleryRange()));
        styleTextField(artilleryRangeField);
        
        addLabelAndControl(gameSettings, "Delay Between Waves (ms):", delayBetweenWavesField);
        addLabelAndControl(gameSettings, "Delay Between Groups (ms):", delayBetweenGroupsField);
        addLabelAndControl(gameSettings, "Starting Gold:", startingGoldField);
        addLabelAndControl(gameSettings, "Starting Health:", startingHealthField);
        addLabelAndControl(gameSettings, "Artillery Range:", artilleryRangeField);
        
        // Add all settings panels to container
        settingsContainer.getChildren().addAll(playerSettings, audioSettings, gameSettings);
        
        // Create buttons with the same style as the loading screen
        Button applyButton = new Button("APPLY CHANGES");
        styleButton(applyButton);
        
        Button resetButton = new Button("RESET TO DEFAULTS");
        styleButton(resetButton);
        
        Button returnButton = new Button("RETURN TO MENU");
        styleButton(returnButton);
        returnButton.setPrefWidth(200);
        
        // Create button container
        HBox buttonBox = new HBox(20, applyButton, resetButton);
        buttonBox.setAlignment(Pos.CENTER);
        
        VBox returnBox = new VBox(20, returnButton);
        returnBox.setAlignment(Pos.CENTER);
        returnBox.setPadding(new Insets(10, 0, 20, 0));
        
        settingsContainer.getChildren().addAll(buttonBox, returnBox);
        
        
        // Stack all content
        StackPane contentStack = new StackPane();

        
        // Add a scroll pane for the settings
        ScrollPane scrollPane = new ScrollPane(settingsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        contentStack.getChildren().add(scrollPane);
        
        // Add content to the root
        ((StackPane)root.getCenter()).getChildren().add(contentStack);

        // Actions
        applyButton.setOnAction(e -> {
            try {
                UserPreference.Builder builder = new UserPreference.Builder()
                    .setUserName(usernameField.getText())
                    .setMusicVolume((float) musicVolumeSlider.getValue())
                    .setSoundVolume((float) soundVolumeSlider.getValue())
                    .setDelayBetweenWaves(Integer.parseInt(delayBetweenWavesField.getText()))
                    .setDelayBetweenGroups(Integer.parseInt(delayBetweenGroupsField.getText()))
                    .setStartingGold(Integer.parseInt(startingGoldField.getText()))
                    .setStartingHealth(Integer.parseInt(startingHealthField.getText()))
                    .setArtilleryRange(Float.parseFloat(artilleryRangeField.getText()));
        
                controller.applyPreferences(builder);
                showAlert("Preferences applied!");
            } catch (NumberFormatException ex) {
                showAlert("Invalid input: please enter numeric values where appropriate.");
            }
        });

        resetButton.setOnAction(e -> {
            controller.resetPreferencesToDefault();

            // Refresh UI with new default values
            UserPreference defaults = controller.getCurrentPreferences();
            usernameField.setText(defaults.getUserName());
            musicVolumeSlider.setValue(defaults.getMusicVolume());
            soundVolumeSlider.setValue(defaults.getSoundVolume());
            delayBetweenWavesField.setText(String.valueOf(defaults.getDelayBetweenWaves()));
            delayBetweenGroupsField.setText(String.valueOf(defaults.getDelayBetweenGroups()));
            startingGoldField.setText(String.valueOf(defaults.getStartingGold()));
            startingHealthField.setText(String.valueOf(defaults.getStartingHealth()));
            artilleryRangeField.setText(String.valueOf(defaults.getArtilleryRange()));

            // Update percentage labels
            musicValueLabel.setText(Math.round(defaults.getMusicVolume() * 100) + "%");
            soundValueLabel.setText(Math.round(defaults.getSoundVolume() * 100) + "%");

            showAlert("Preferences reset to defaults.");
        });
        
        returnButton.setOnAction(e -> {
            settingsStage.close();
            stage.show();
        });


        Scene scene = new Scene(root, 800, 600);
        settingsStage.setTitle("Tower Defense - Settings");
        settingsStage.setScene(scene);
        settingsStage.show();
        
        stage.hide();
    }
    
    private VBox createSettingsPanel(String title) {
        // Create panel title
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.rgb(0, 218, 142)); 
        titleLabel.setPadding(new Insets(0, 0, 10, 0));
        
        // Create panel
        VBox panel = new VBox(12);
        panel.setStyle("-fx-background-color: rgba(20, 20, 20, 0.7); -fx-background-radius: 10;");
        panel.setPadding(new Insets(15, 20, 15, 20));
        panel.getChildren().add(titleLabel);
        
        return panel;
    }
    
    private void addLabelAndControl(VBox panel, String labelText, Control control) {
        Label label = new Label(labelText);
        label.setFont(Font.font("System", FontWeight.NORMAL, 14));
        label.setTextFill(Color.WHITE);
        
        panel.getChildren().addAll(label, control);
    }
    
    private void addLabelAndControl(VBox panel, String labelText, Pane controlContainer) {
        Label label = new Label(labelText);
        label.setFont(Font.font("System", FontWeight.NORMAL, 14));
        label.setTextFill(Color.WHITE);
        
        panel.getChildren().addAll(label, controlContainer);
    }
    
    private void styleTextField(TextField field) {
        field.setStyle(
            "-fx-background-color: rgba(30, 30, 30, 0.8);" +
            "-fx-text-fill: white;" +
            "-fx-border-color: rgb(0, 218, 142);" +
            "-fx-border-width: 1px;" +
            "-fx-border-radius: 5px;" +
            "-fx-background-radius: 5px;" +
            "-fx-padding: 8px;"
        );
        field.setPrefHeight(35);
    }
    
    private void styleSlider(Slider slider) {
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(0.25);
        slider.setMinorTickCount(5);
        slider.setBlockIncrement(0.1);
        slider.setPrefWidth(300);
        slider.setStyle(
            "-fx-control-inner-background: #3c3b3f;" +
            "-fx-accent: rgb(0, 218, 142);" 
        );
    }
    
    private void styleValueLabel(Label label) {
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font("System", FontWeight.BOLD, 14));
        label.setMinWidth(50);
    }
    
    private void styleButton(Button button) {
        button.setPrefHeight(40);
        button.setPrefWidth(180);
        button.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        button.setStyle(
            "-fx-background-color: rgb(0, 218, 142);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 8px;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0, 0, 1);"
        );
        
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: rgb(34, 220, 155);" + 
            "-fx-text-fill: white;" +
            "-fx-background-radius: 8px;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 8, 0, 0, 1);"
        ));
        
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: rgb(0, 218, 142);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 8px;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0, 0, 1);"
        ));
        
        button.setOnMousePressed(e -> button.setStyle(
            "-fx-background-color: rgb(0, 190, 120);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 8px;" +
            "-fx-translate-y: 2px;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 3, 0, 0, 1);"
        ));
        
        button.setOnMouseReleased(e -> button.setStyle(
            "-fx-background-color: rgb(0, 218, 142);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 8px;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0, 0, 1);"
        ));
    }
    

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Settings");
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Style the alert dialog
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle(
            "-fx-background-color: #212121;" +
            "-fx-text-fill: white;"
        );
        
        // Apply styles to the buttons in the alert
        dialogPane.lookupButton(ButtonType.OK).setStyle(
            "-fx-background-color: rgb(0, 218, 142);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 5px;"
        );
        
        alert.showAndWait();
    }
}