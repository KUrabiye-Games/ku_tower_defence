package com.kurabiye.kutd.view;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;;
public class MapView {
   
    private static final int TILE_SIZE = 64;
    private static final int ROWS = 9;
    private static final int COLS = 16;
    private static final int TILE_COUNT = 32;
    private static final int GRASS_TILE_ID = 5;
    private static final int INTERACTIVE_TILE_ID = 15;


    private final int[][] map = {
        { 5, 5, 5, 5, 16, 5, 17, 5, 5, 5, 24, 25, 7, 5, 5, 19 },
        { 0, 1, 2, 5, 0, 1, 2, 5, 5, 18, 28, 29, 6, 23, 16, 5 },
        { 4, 15, 7, 15, 7, 22, 8, 13, 13, 9, 1, 9, 10, 5, 5, 5 },
        { 8, 2, 8, 9, 10, 5, 5, 5, 5, 5, 5, 5, 5, 17, 27, 5 },
        { 5, 7, 19, 18, 5, 5, 5, 0, 1, 2, 21, 5, 5, 31, 5, 5},
        { 5, 7, 5, 5, 20, 0, 13, 10, 15, 8, 13, 2, 5, 5, 5, 5 },
        { 5, 4, 5, 0, 13, 10, 0, 1, 2, 5, 30, 6, 5, 5, 5, 5 },
        { 23, 7, 15, 7, 5, 5, 4, 18, 8, 13, 13, 10, 16, 5, 18, 5 },
        { 5, 8, 13, 10, 5, 5, 7, 5, 5, 5, 5, 5, 5, 5, 5, 5 }
    };

    private final Image[] tileImages = new Image[TILE_COUNT];
    private Image[] buttonImages = new Image[3]; // For the three button icons
    private Pane root;
    private Canvas canvas;
    private HBox buttonContainer;
    private int lastClickedRow = -1;
    private int lastClickedCol = -1;
    private boolean isPaused = false;
    private double gameSpeed = 1.0;

    public void start(Stage stage) {
        loadTiles();
        loadButtonIcons();

        canvas = new Canvas(COLS * TILE_SIZE, ROWS * TILE_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawMap(gc);

        root = new Pane(canvas); // Changed from StackPane to Pane

        // Create control buttons
        Button pauseButton = createControlButton("Pause");
        Button speedUpButton = createControlButton("2x");

        pauseButton.setOnAction(e -> {
            isPaused = !isPaused;
            pauseButton.setText(isPaused ? "Resume" : "Pause");
            System.out.println("Game " + (isPaused ? "paused" : "resumed"));
        });

        speedUpButton.setOnAction(e -> {
            gameSpeed = (gameSpeed == 1.0) ? 2.0 : 1.0;
            speedUpButton.setText(gameSpeed == 1.0 ? "2x" : "1x");
            System.out.println("game speed set to: " + gameSpeed + "x");
        });
        // Create container for control buttons
        HBox controls = new HBox(10);
        controls.setAlignment(Pos.TOP_RIGHT);
        controls.getChildren().addAll(pauseButton, speedUpButton);
        controls.setLayoutX((COLS * TILE_SIZE - 180)); // Top right
        controls.setLayoutY(10);

        root.getChildren().add(controls);

        Scene scene = new Scene(root);
        stage.setTitle("Game Map");
        stage.setScene(scene);
        stage.show();

        setupClickHandler();
    }
    
    private Button createControlButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);" +
                        "-fx-text-fill: white;" + 
                         "-fx-font-size: 14px;" +
                        "-fx-padding: 5 10;" +             
                        "-fx-background-radius: 5;"
        );
        
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: rgba(50, 50, 50, 0.7);" +
                        "-fx-text-fill: white;" + 
                         "-fx-font-size: 14px;" +
                        "-fx-padding: 5 10;" +             
                        "-fx-background-radius: 5;"
        ));

        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: rgba(0, 0, 0, 0.7);" +             
            "-fx-text-fill: white;" +             
            "-fx-font-size: 14px;" +             
            "-fx-padding: 5 10;" +             
            "-fx-background-radius: 5;"));
        
        return button;
    }

    private void loadTiles() {
        for (int i = 0; i < TILE_COUNT; i++) {
            String path = "/assets/tiles/tile" + i + ".png";
            tileImages[i] = new Image(getClass().getResourceAsStream(path));
        }
    }

    private void loadButtonIcons() {
        // Load images for the buttons
        for (int i = 0; i < 3; i++) {
            
            String path;

            if (i == 0) {
                path = "/assets/buttons/star.png"; // First button icon
            } else if (i == 1) {
                path = "/assets/buttons/arrow.png"; // Second button icon
            } else {
                path = "/assets/buttons/bomb.png"; // Third button icon
            }

            buttonImages[i] = new Image(getClass().getResourceAsStream(path));
        }
    }

    private void drawMap(GraphicsContext gc) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                // make grass background
                gc.drawImage(tileImages[GRASS_TILE_ID], col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);

                // place the items
                int tileId = map[row][col];
                if (tileId != GRASS_TILE_ID) {
                    gc.drawImage(tileImages[tileId], col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }

    private void setupClickHandler() {
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            int col = (int) (event.getX() / TILE_SIZE);
            int row = (int) (event.getY() / TILE_SIZE);
    
            if (row >= 0 && row < ROWS && col >= 0 && col < COLS && map[row][col] == INTERACTIVE_TILE_ID) {
                if (buttonContainer != null) {
                    root.getChildren().remove(buttonContainer);
                }
    
                if (lastClickedRow == row && lastClickedCol == col) {
                    lastClickedRow = -1;
                    lastClickedCol = -1;
                    return;
                }
    
                lastClickedRow = row;
                lastClickedCol = col;
    
                buttonContainer = new HBox(10);
                buttonContainer.setAlignment(Pos.CENTER);
                
                // Calculate position based on clicked tile
                double tileLeftX = col * TILE_SIZE;
                double tileTopY = row * TILE_SIZE;
                
                // Position container centered above the clicked tile
                buttonContainer.setLayoutX(tileLeftX + (TILE_SIZE/2) - 105); // Center minus half of total buttons width
                
                // Position above the tile with margin
                double buttonsY = tileTopY - 80; // 80 pixels above tile
                if (buttonsY < 0) {
                    buttonsY = tileTopY + TILE_SIZE + 10; // Show below if near top
                }
                buttonContainer.setLayoutY(buttonsY);
    
                // Create buttons with arc positioning
                for (int i = 0; i < 3; i++) {
                    Button button = new Button();
                    if (buttonImages[i] != null) {
                        button.setGraphic(new javafx.scene.image.ImageView(buttonImages[i]));
                    }
                    button.setStyle("-fx-background-color: transparent; -fx-padding: 5;");
                    button.setPrefSize(64, 64);
                    
                    // Apply vertical offset for arc effect
                    if (i == 0) { // Left button
                        button.setTranslateY(20);
                    } 
                    else if (i == 2) { // Right button
                        button.setTranslateY(20);
                    }
                    // Middle button stays at default height
                    
                    final int buttonId = i;
                    button.setOnAction(e -> handleButtonClick(buttonId, row, col));
                    
                    buttonContainer.getChildren().add(button);
                }
    
                root.getChildren().add(buttonContainer);
            } else {
                if (buttonContainer != null) {
                    root.getChildren().remove(buttonContainer);
                    buttonContainer = null;
                    lastClickedRow = -1;
                    lastClickedCol = -1;
                }
            }
        });
    }

    private void handleButtonClick(int buttonId, int row, int col) {
        System.out.println("Button " + buttonId + " clicked on tile at row " + row + ", col " + col);
        // Here you can add whatever functionality you want for each button
        // For example, change the map tile, trigger an action, etc.
        
        // After handling, remove the buttons
        if (buttonContainer != null) {
            root.getChildren().remove(buttonContainer);
            buttonContainer = null;
            lastClickedRow = -1;
            lastClickedCol = -1;
        }
    }
}
