package com.kurabiye.kutd.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.kurabiye.kutd.controller.GamePlayController;
import com.kurabiye.kutd.controller.MapSelectionController;
import com.kurabiye.kutd.model.Map.GameMap;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MapSelectionView {
    private final ListView<String> mapList;
    private GameMap selectedMap;
    private MapSelectionController controller;

    public MapSelectionView() {
        this.mapList = new ListView<>();
    }

    public void start(Stage stage, MapSelectionController controller) {
        this.controller = controller;

        // Create main container
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #2c3e50;");

        // Create title
        Text title = new Text("Select a Map");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));
        title.setStyle("-fx-fill: white;");

        // Setup map list
        mapList.getItems().addAll(controller.getAvailableMapNames());
        mapList.setPrefHeight(400);
        mapList.setPrefWidth(300);
        mapList.setStyle(
            "-fx-background-color: #34495e;" +
            "-fx-text-fill: white;" +
            "-fx-control-inner-background: #34495e;"
        );

        // Create buttons
        Button selectButton = createButton("Play Selected Map");
        Button backButton = createButton("Back to Main Menu");

        // Disable select button if no map is selected
        selectButton.setDisable(true);
        mapList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> 
            selectButton.setDisable(newVal == null));

        // Button actions
        selectButton.setOnAction(e -> {
            String selectedName = mapList.getSelectionModel().getSelectedItem();
            if (selectedName != null) {
                selectedMap = controller.getMap(selectedName);
                // Create and show gameplay view
                GamePlayController gamePlayController = new GamePlayController(selectedMap);
                GamePlayView gamePlayView = new GamePlayView();
                gamePlayView.start(stage, gamePlayController);
            }
        });

        backButton.setOnAction(e -> {
            MainMenuView mainMenuView = new MainMenuView();
            mainMenuView.start(stage);
        });

        // Add all elements to root
        root.getChildren().addAll(
            title,
            mapList,
            selectButton,
            backButton
        );

        // Create and set scene
        Scene scene = new Scene(root, 500, 600);
        stage.setTitle("Map Selection");
        stage.setScene(scene);
        stage.show();
    }

    private Button createButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(200);
        button.setPrefHeight(40);
        button.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        button.setStyle(
            "-fx-background-color: #27ae60;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 5;"
        );
        
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: #2ecc71;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 5;"
        ));
        
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: #27ae60;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 5;"
        ));
        
        return button;
    }

    public GameMap getSelectedMap() {
        return selectedMap;
    }
}