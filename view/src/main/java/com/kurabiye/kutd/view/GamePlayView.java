package com.kurabiye.kutd.view;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

import com.kurabiye.kutd.controller.GamePlayController;
import com.kurabiye.kutd.model.Enemy.Enemy;
import com.kurabiye.kutd.model.Listeners.IGameUpdateListener;
import com.kurabiye.kutd.model.Map.GameMap;
import com.kurabiye.kutd.util.ObserverPattern.Observer;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;

public class GamePlayView implements IGameUpdateListener, Observer {
    
    // Reference dimensions that the game was designed for
    private static final int REFERENCE_WIDTH = 1920;
    private static final int REFERENCE_HEIGHT = 1080;
    
    // The actual screen dimensions
    private static final int SCREEN_WIDTH = (int) Screen.getPrimary().getBounds().getWidth();
    private static final int SCREEN_HEIGHT = (int) Screen.getPrimary().getBounds().getHeight();
    
    // Keep the map grid fixed at 16x9
    private static final int ROWS = 9;
    private static final int COLS = 16;
    
    // Calculate scaling factors
    private static final double SCALE_X = (double) SCREEN_WIDTH / REFERENCE_WIDTH;
    private static final double SCALE_Y = (double) SCREEN_HEIGHT / REFERENCE_HEIGHT;
    
    // Use the smaller scaling factor to maintain aspect ratio
    private static final double SCALE = Math.min(SCALE_X, SCALE_Y);
    
    // Calculate the tile size based on the scale
    private static final int TILE_SIZE = (int) (REFERENCE_WIDTH / COLS * SCALE);
    
    // Calculate the actual canvas size
    private static final int CANVAS_WIDTH = TILE_SIZE * COLS;
    private static final int CANVAS_HEIGHT = TILE_SIZE * ROWS;
    
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

    private EnemyView enemyView;

    ArrayList<Enemy> enemies;

    private int[][] map;
    
    public void start(Stage stage, GamePlayController controller) {
        loadTiles();
        loadButtonIcons();

        this.controller = controller;
        this.enemyView = new EnemyView(TILE_SIZE);  // Pass just the tile size

        this.enemies = controller.getGameManager().getEnemies();

        controller.setGameUpdateListener(this);
        controller.setPlayerObserver(this);
        controller.startGame();

        map = GameMap.toIntArray(controller.getGameManager().getGameMap());

        // Create canvas with the calculated dimensions
        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawMap(gc);
    
        // Create root pane to center the canvas
        root = new Pane();
        root.setPrefSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        
        // Position canvas in the center if it's smaller than the screen
        canvas.setLayoutX((SCREEN_WIDTH - CANVAS_WIDTH) / 2);
        canvas.setLayoutY((SCREEN_HEIGHT - CANVAS_HEIGHT) / 2);
        
        root.getChildren().add(canvas);
        
        addUIElements();
    
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.setTitle("Game Map");
        stage.setScene(scene);
        stage.setMaximized(true);
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

    // Method called by the controller to update the game view
    @Override
    public void onGameUpdate(double deltaTime) { 
        // This must be called on the JavaFX Application Thread 
        // So we wrap it in Platform.runLater
        Platform.runLater(() -> { updateView(); }); 
    }

    private void updateView() {

        System.out.println("Update view called");
        System.out.println("Enemies: " + enemies);

        // Log positions of enemies
        for (Enemy enemy : enemies) {
            System.out.println("Enemy position: " + enemy.getCoordinate());
        }

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawMap(gc);

        enemyView.renderEnemies(gc, enemies);
    }

    @Override
    public void update(Object arg) {
        
        System.out.println("Observer update called with argument: " + arg);
    }
}
