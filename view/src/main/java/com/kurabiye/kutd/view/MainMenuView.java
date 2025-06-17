package com.kurabiye.kutd.view;

import com.kurabiye.kutd.controller.GamePlayController;
import com.kurabiye.kutd.controller.MainMenuController;
import com.kurabiye.kutd.controller.MapEditorController;
import com.kurabiye.kutd.controller.MapSelectionController;
import com.kurabiye.kutd.controller.SettingsController;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainMenuView {

    private MainMenuController controller = new MainMenuController();
    private MusicManager musicManager;

    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        // Initialize music manager and start menu music
        musicManager = new MusicManager();
        musicManager.playBackgroundMusic();

        // Load background image
        Image backgroundImage = new Image(getClass().getClassLoader().getResource("assets/background.jpeg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(600);
        backgroundView.setFitHeight(400);
        backgroundView.setPreserveRatio(false); // Fill the entire scene

        // Game title with shadow
        Text titleText = new Text("KU TOWER DEFENSE");
        titleText.setFont(Font.font("System", FontWeight.BOLD, 48));
        titleText.setFill(Color.GOLD);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.BLACK);
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        titleText.setEffect(dropShadow);

        // Create buttons
        Button playButton = createMenuButton("Play Game");
        Button mapEditorButton = createMenuButton("Map Editor"); // New Map Editor button
        Button settingsButton = createMenuButton("Settings");
        Button exitButton = createMenuButton("Exit");

        // Layout for title and buttons
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(titleText, playButton, mapEditorButton, settingsButton, exitButton);

        StackPane mainPane = new StackPane();
        mainPane.getChildren().addAll(backgroundView, vbox);

        root.setCenter(mainPane);

        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Tower Defense - Main Menu");
        stage.setScene(scene);
        stage.show();

        // Button actions (you can fill these later)
        playButton.setOnAction(e -> {
            // TODO: Start the game

            musicManager.stopCurrentMusic();

            MapSelectionController mapSelectionController = controller.onPlayButtonPressed();
            MapSelectionView mapSelectionView = new MapSelectionView();
            mapSelectionView.start(stage, mapSelectionController);
        });

        mapEditorButton.setOnAction(e -> {
            // TODO: Open Map Editor
            musicManager.stopCurrentMusic();
            MapEditorController mapEditorcontroller = controller.onMapEditorButtonPressed();
            MapEditorView mapEditor = new MapEditorView();
            mapEditor.start(stage, mapEditorcontroller);
        });

        settingsButton.setOnAction(e -> {
            SettingsController settingsController = controller.onSettingsButtonPressed();
            SettingsView settingsView = new SettingsView();
            settingsView.show(stage, settingsController, musicManager);
        });

        exitButton.setOnAction(e -> {
            stage.close();
        });
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(200);
        button.setPrefHeight(40);
        button.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #4CAF50, #2E7D32);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-background-radius: 10;"
        );

        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #66BB6A, #388E3C);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-background-radius: 10;"
        ));

        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #4CAF50, #2E7D32);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-background-radius: 10;"
        ));

        return button;
    }
}
