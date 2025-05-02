package com.kurabiye.kutd.view;

import com.kurabiye.kutd.controller.GamePlayController;
import com.kurabiye.kutd.controller.MainMenuController;

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

    public void start(Stage stage) {
        BorderPane root = new BorderPane();

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

            GamePlayController gamePlayController = controller.onPlayButtonPressed();
            GamePlayView mapView = new GamePlayView();
            mapView.start(stage, gamePlayController);

        });

        mapEditorButton.setOnAction(e -> {
            // TODO: Open Map Editor
            System.out.println("Map Editor button clicked");
        });

        settingsButton.setOnAction(e -> {
            // TODO: Open Settings
            System.out.println("Settings button clicked");
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
