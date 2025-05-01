package com.kurabiye.kutd.view;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.kurabiye.kutd.controller.GamePlayController;

import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
public class MapView {
   
    private static final int TILE_SIZE = 64;
    private static final int ROWS = 9;
    private static final int COLS = 16;
    private static final int TILE_COUNT = 32;
    private static final int GRASS_TILE_ID = 5;
    private static final int INTERACTIVE_TILE_ID = 15;

    private final Image[] tileImages = new Image[TILE_COUNT];
    private Image[] buttonImages = new Image[3]; // For the three button icons
    private Image blueButtonImage;
    private Image iconsImage;
    private Pane root;
    private Canvas canvas;
    private HBox buttonContainer;
    private int lastClickedRow = -1;
    private int lastClickedCol = -1;
    private GamePlayController controller;

    private int[][] map;
    
    public void start(Stage stage, GamePlayController controller) {
        loadTiles();
        loadButtonIcons();

        this.controller = controller;

        map = controller.getGameManager()

        canvas = new Canvas(COLS * TILE_SIZE, ROWS * TILE_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawMap(gc);
    
        root = new Pane(canvas);  // Changed from StackPane to Pane

        addUIElements();
    
        Scene scene = new Scene(root);
        stage.setTitle("Game Map");
        stage.setScene(scene);
        stage.show();
    
        setupClickHandler();
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

        blueButtonImage = new Image(getClass().getResourceAsStream("/assets/ui/blue-button.png"));
        iconsImage = new Image(getClass().getResourceAsStream("/assets/ui/status-icons.png"));
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

    private void addUIElements() {
        VBox buttonColumn = new VBox(10); // Vertical layout with spacing
        buttonColumn.setLayoutX(64); // Next to icons
        buttonColumn.setLayoutY(10);

        for (int i = 0; i < 3; i++) {
            ImageView blueButton = new ImageView(blueButtonImage);
            blueButton.setFitWidth(120);
            blueButton.setFitHeight(42);
            buttonColumn.getChildren().add(blueButton);
        }

        ImageView icons = new ImageView(iconsImage);
        icons.setFitWidth(48);
        icons.setFitHeight(48 * 3); // Match button height
        icons.setLayoutX(10);
        icons.setLayoutY(10);

        root.getChildren().addAll(icons, buttonColumn);
    }
}
