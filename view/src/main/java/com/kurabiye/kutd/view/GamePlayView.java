package com.kurabiye.kutd.view;

import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;

import com.kurabiye.kutd.controller.GamePlayController;
import com.kurabiye.kutd.model.Coordinates.Point2D;
import com.kurabiye.kutd.model.Enemy.Enemy;
import com.kurabiye.kutd.model.Listeners.IGameUpdateListener;
import com.kurabiye.kutd.model.Map.GameMap;
import com.kurabiye.kutd.model.Projectile.Projectile;
import com.kurabiye.kutd.util.ObserverPattern.Observer;
import com.kurabiye.kutd.model.Tower.Tower;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
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
    private Image cursorImage;
    private Image blueButtonImage;
    private Image iconsImage;
    private Image playImage;       // Play button image
    private Image pauseImage;      // Pause button image
    private Image accelerateImage; // Speed up image
    private Image settingsImage;   // Settings image
    private Button playPauseButton;
    private boolean isGamePlaying = true;
    private boolean isGameAccelerated = false;
    private Pane root;
    private Canvas canvas;
    private GraphicsContext gc;
    private HBox buttonContainer;
    private int lastClickedRow = -1;
    private int lastClickedCol = -1;
    private GamePlayController controller;

    private EnemyView enemyView;
    // private TowerView towerView;

    ArrayList<Enemy> enemies;
    ArrayList<Tower> towers;
    // Projectiles projectiles;

    ArrayList<Projectile> projectiles;

    private int currentGold;
    private int currentHealth;
    private int currentWave;

    private Text goldText;
    private Text healthText;

    private int[][] map;
    
    public void start(Stage stage, GamePlayController controller) {
        
        loadTiles();
        loadButtonIcons();

        this.controller = controller;
        this.enemyView = new EnemyView(TILE_SIZE);  // Pass just the tile size
        // this.towerView = new TowerView(TILE_SIZE);  // Pass just the tile size

        this.enemies = controller.getGameManager().getEnemies();
        this.towers = controller.getGameManager().getTowers();
        this.projectiles = controller.getGameManager().getProjectiles();
        this.currentGold = controller.getGameManager().getPlayer().getCurrentGold();
        this.currentHealth = controller.getGameManager().getPlayer().getCurrentHealth();
        this.currentWave = controller.getGameManager().getCurrentWaveIndex();

        controller.setGameUpdateListener(this);
        controller.setPlayerObserver(this);
        controller.setGameMapObserver(this);
        controller.startGame();

        map = GameMap.toIntArray(controller.getGameManager().getGameMap());

        // Create canvas with the calculated dimensions
        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        drawMap(gc);
    
        // Create root pane to center the canvas
        root = new Pane();
        root.setPrefSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        
        // Position canvas in the center if it's smaller than the screen
        canvas.setLayoutX((SCREEN_WIDTH - CANVAS_WIDTH) / 2);
        canvas.setLayoutY(0); // Set to top of screen
        
        root.getChildren().add(canvas);
        
        addUIElements(stage);

        cursorImage = new Image(getClass().getResourceAsStream("/assets/ui/cursor.png"));
    
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        scene.setCursor(new ImageCursor(cursorImage,
                                cursorImage.getWidth() / 2,
                                cursorImage.getHeight() /2));
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

        // Load control button images
        playImage = new Image(getClass().getResourceAsStream("/assets/buttons/play.png"));
        pauseImage = new Image(getClass().getResourceAsStream("/assets/buttons/pause.png"));
        accelerateImage = new Image(getClass().getResourceAsStream("/assets/buttons/accelerate.png"));
        settingsImage = new Image(getClass().getResourceAsStream("/assets/buttons/settings.png"));
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
    
            if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
                int tileId = map[row][col];
    
                if (tileId >= 20 && tileId <= 26) { // Tower tile IDs
                    showSellButton(row, col);
                } else if (tileId == INTERACTIVE_TILE_ID) { // Buildable tile
                    showBuildButtons(row, col);
                } else {
                    removeButtonContainer();
                }
            } else {
                removeButtonContainer();
            }
        });
    }

    private void removeButtonContainer() {
        if (buttonContainer != null) {
            root.getChildren().remove(buttonContainer);
            buttonContainer = null;
            lastClickedRow = -1;
            lastClickedCol = -1;
        }
    }
    private void showSellButton(int row, int col) {
        removeButtonContainer();
    
        buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
    
        // Calculate position based on clicked tile
        double tileLeftX = col * TILE_SIZE;
        double tileTopY = row * TILE_SIZE;
    
        // Position container centered above the clicked tile
        buttonContainer.setLayoutX(tileLeftX + (TILE_SIZE / 2) - 50); // Center minus half of button width
        buttonContainer.setLayoutY(tileTopY - 40); // Position above the tile
    
        Button sellButton = new Button("Sell");
        sellButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold;");
        sellButton.setPrefSize(80, 30);
    
        sellButton.setOnAction(e -> {
            handleSellButtonClick(row, col);
        });
    
        buttonContainer.getChildren().add(sellButton);
        root.getChildren().add(buttonContainer);
    }

    private void handleSellButtonClick(int row, int col) {
        int tileId = map[row][col];
            int towerType;
            switch (tileId) {
                case 20: // Example: Tower type 0
                    towerType = 0;
                    break;
                case 21: // Example: Tower type 1
                    towerType = 1;
                    break;
                case 26: // Example: Tower type 2
                    towerType = 2;
                    break;
                default:
                    System.out.println("Unknown tower type for tile ID: " + tileId);
                    return;
            }
    
            // Call the controller's sellTower method with the tower type
            boolean success = controller.sellTower(col, row, towerType);
            if (success) {
                System.out.println("Tower of type " + towerType + " sold at row " + row + ", col " + col);
            } else {
                System.out.println("Failed to sell tower at row " + row + ", col " + col);
            }
    
           removeButtonContainer();
    }

    private void showBuildButtons(int row, int col) {
        removeButtonContainer();
    
        buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
    
        // Calculate position based on clicked tile
        double tileLeftX = col * TILE_SIZE;
        double tileTopY = row * TILE_SIZE;
    
        // Position container centered above the clicked tile
        buttonContainer.setLayoutX(tileLeftX + (TILE_SIZE / 2) - 105); // Center minus half of total buttons width
        buttonContainer.setLayoutY(tileTopY - 80); // Position above the tile
    
        // Create buttons for building towers
        for (int i = 0; i < 3; i++) {
            Button button = new Button();
            if (buttonImages[i] != null) {
                button.setGraphic(new ImageView(buttonImages[i]));
            }
            button.setStyle("-fx-background-color: transparent; -fx-padding: 5;");
            button.setPrefSize(64, 64);
    
            final int buttonId = i;
            button.setOnAction(e -> handleBuildButtonClick(buttonId, row, col));
    
            buttonContainer.getChildren().add(button);
        }
    
        root.getChildren().add(buttonContainer);
    }

    private void handleBuildButtonClick(int buttonId, int row, int col) {
        System.out.println("Button " + buttonId + " clicked on tile at row " + row + ", col " + col);
        
        // Map button IDs to tower types (0=Magic/Star, 1=Artillery/Bomb, 2=Archer/Arrow)
        int towerType;
        switch(buttonId) {
            case 0: // Star button - creates Magic tower
                towerType = 1; // MAGIC tower type
                break;
            case 1: // Bomb button - creates Artillery tower
                towerType = 2; // ARTILLERY tower type
                break;
            case 2: // Arrow button - creates Archer tower
                towerType = 0; // ARROW tower type
                break;
            default:
                System.out.println("Unknown button ID: " + buttonId);
                return;
        }
        
        // Tell the controller to build a tower of the selected type
        boolean success = controller.buildTower(col, row, towerType);

        System.out.println("Tower List: " + towers);
        
        if (success) {
            System.out.println("Tower of type " + towerType + " built at row " + row + ", col " + col);
        } else {
            System.out.println("Failed to build tower at row " + row + ", col " + col);
        }
        
        removeButtonContainer();
    }

    private void addUIElements(Stage stage) {
        VBox buttonColumn = new VBox(10); // Vertical layout with spacing
        buttonColumn.setLayoutX(64); // Next to icons
        buttonColumn.setLayoutY(10);

        goldText = new Text(""+currentGold);
        goldText.setFill(Color.WHITE);
        goldText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 18));
        goldText.setTextOrigin(VPos.CENTER);

        healthText = new Text(""+currentHealth);
        healthText.setFill(Color.WHITE);
        healthText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 18));
        healthText.setTextOrigin(VPos.CENTER);

        Text waveText = new Text(""+currentWave);
        waveText.setFill(Color.WHITE);
        waveText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 18));
        waveText.setTextOrigin(VPos.CENTER);

        for (int i = 0; i < 3; i++) {
            ImageView blueButton = new ImageView(blueButtonImage);
            blueButton.setFitWidth(120);
            blueButton.setFitHeight(42);
            
            // For the first button (gold button), create a stack pane to overlay text and image
            if (i == 0) {
                StackPane goldButton = new StackPane();
                goldButton.getChildren().addAll(blueButton, goldText);
                StackPane.setAlignment(goldText, Pos.CENTER);
                
                // Add margin to adjust vertical position if needed
                StackPane.setMargin(goldText, new Insets(-10, 0, 0, 0)); // Adjust top margin as needed
                
                buttonColumn.getChildren().add(goldButton);
            } else {
                buttonColumn.getChildren().add(blueButton);
            }

            // Add the health and wave buttons
            if (i == 1) {
                StackPane healthButton = new StackPane();
                healthButton.getChildren().addAll(blueButton, healthText);
                StackPane.setAlignment(healthText, Pos.CENTER);
                StackPane.setMargin(healthText, new Insets(-10, 0, 0, 0));
                buttonColumn.getChildren().add(healthButton);
            } else if (i == 2) {
                StackPane waveButton = new StackPane();
                waveButton.getChildren().addAll(blueButton, waveText);
                StackPane.setAlignment(waveText, Pos.CENTER);
                StackPane.setMargin(waveText, new Insets(-10, 0, 0, 0));
                buttonColumn.getChildren().add(waveButton);
            }
        }  

        ImageView icons = new ImageView(iconsImage);
        icons.setFitWidth(48);
        icons.setFitHeight(48 * 3); // Match button height
        icons.setLayoutX(10);
        icons.setLayoutY(10);

        // Create control buttons for the top-right corner
        HBox controlButtons = createControlButtons(stage);
        // Position at top-right with a margin
        controlButtons.setLayoutX(CANVAS_WIDTH - 200); // Adjust based on total width of buttons
        controlButtons.setLayoutY(20);
 
        root.getChildren().addAll(icons, buttonColumn, controlButtons);
    }

    private HBox createControlButtons(Stage stage) {
        HBox container = new HBox(10); // 10 pixels spacing between buttons
        
        // Create play/pause button with initial play image and store reference
        playPauseButton = createControlButton(pauseImage);
        
        // Create accelerate button
        Button accelerateButton = createControlButton(accelerateImage);
        // Create settings button
        Button settingsButton = createControlButton(settingsImage);
        
        // Set up the play/pause toggle functionality
        playPauseButton.setOnAction(e -> {
            if (isGamePlaying) {
                // Currently playing, pause the game
                controller.pauseGame();
                ((ImageView)playPauseButton.getGraphic()).setImage(playImage);
                
                // Show pause menu
                showPauseMenu(stage);
            } else {
                // Currently paused, resume the game
                controller.resumeGame();
                ((ImageView)playPauseButton.getGraphic()).setImage(pauseImage);
            }
            isGamePlaying = !isGamePlaying;
        });
        
        // Set up accelerate button
        accelerateButton.setOnAction(e -> {
            if (isGameAccelerated) {
                // Currently accelerated, slow down the game
                controller.slowDownGame();
            } else {
                // Currently normal speed, speed up the game
                controller.speedUpGame();
            }
            isGameAccelerated = !isGameAccelerated;
        });

        // Set up settings button
        settingsButton.setOnAction(e -> {
            // Pause the game
            controller.pauseGame();
            isGamePlaying = false;
            ((ImageView)playPauseButton.getGraphic()).setImage(playImage);
            
            // Show pause menu
            showPauseMenu(stage);
        });
        
        container.getChildren().addAll(playPauseButton, accelerateButton, settingsButton);
        return container;
    }

    private Button createControlButton(Image image) {
        Button button = new Button();
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(52);
        imageView.setFitHeight(52);
        button.setGraphic(imageView);
        
        // Remove button background, padding, and border
        button.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-background-insets: 0;" +
            "-fx-background-radius: 0;" +
            "-fx-padding: 0;" +
            "-fx-border-color: transparent;" +
            "-fx-border-width: 0;" +
            "-fx-focus-color: transparent;" +
            "-fx-faint-focus-color: transparent;"
        );
        
        // Keep the transparent style on hover
        button.setOnMouseEntered(e -> {
            button.setOpacity(0.8); // Slight transparency on hover for visual feedback
        });
        
        button.setOnMouseExited(e -> {
            button.setOpacity(1.0); // Restore full opacity
        });
        
        return button;
    }

    private void showPauseMenu(Stage stage) {
        // Create a semi-transparent overlay
        Pane overlay = new Pane();
        overlay.setPrefSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
        
        // Create the pause menu container
        VBox pauseMenu = new VBox(15);
        pauseMenu.setAlignment(Pos.CENTER);
        pauseMenu.setStyle(
            "-fx-background-color: rgba(50, 50, 50, 0.9);" +
            "-fx-background-radius: 15;" +
            "-fx-padding: 20px;"
        );
        pauseMenu.setMaxWidth(300);
        pauseMenu.setMaxHeight(250);
        
        // Create the pause menu title
        Text pauseTitle = new Text("GAME PAUSED");
        pauseTitle.setFont(Font.font("System", FontWeight.BOLD, 24));
        pauseTitle.setFill(Color.WHITE);
        
        // Create buttons
        Button resumeButton = createPauseMenuButton("Resume Game");
        Button mainMenuButton = createPauseMenuButton("Return to Main Menu");
        
        // Add action handlers
        resumeButton.setOnAction(event -> {
            // Resume game
            controller.resumeGame();
            root.getChildren().remove(overlay);
            
            // Update the play/pause button state using the class field
            ((ImageView)playPauseButton.getGraphic()).setImage(pauseImage);
            isGamePlaying = true;
        });
        
        mainMenuButton.setOnAction(event -> {
            // End the current game
            controller.endGame();
            
            // Return to main menu
            MainMenuView mainMenuView = new MainMenuView();
            mainMenuView.start(stage);
        });
        
        // Add all elements to the pause menu
        pauseMenu.getChildren().addAll(pauseTitle, resumeButton, mainMenuButton);
        
        // Center the pause menu on screen
        pauseMenu.setLayoutX((SCREEN_WIDTH - 300) / 2);
        pauseMenu.setLayoutY((SCREEN_HEIGHT - 250) / 2);
        
        // Add overlay and pause menu to the root
        overlay.getChildren().add(pauseMenu);
        root.getChildren().add(overlay);
    }
    
    private Button createPauseMenuButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(200);
        button.setPrefHeight(40);
        button.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        button.setStyle(
            "-fx-background-color: rgb(0, 218, 142);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 8px;"
        );
        
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: rgb(34, 220, 155);" + 
            "-fx-text-fill: white;" +
            "-fx-background-radius: 8px;"
        ));
        
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: rgb(0, 218, 142);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 8px;"
        ));
        
        return button;
    }

    // Method called by the controller to update the game view
    @Override
    public void onGameUpdate(double deltaTime) { 
        // This must be called on the JavaFX Application Thread 
        // So we wrap it in Platform.runLater
        System.out.println("onGameUpdate called with deltaTime: " + deltaTime);
        Platform.runLater(() -> { updateView(deltaTime); });
    }

    private double pastTime = 0.0;

    private void updateView(double deltaTime) {

        // print pastTime and deltaTime
        System.out.println("pastTime: " + pastTime);
        pastTime += deltaTime;
        
        int imgNum = ((int) (pastTime * 6)) % 6;

        // print imgNum
        System.out.println("imgNum: " + imgNum);

        System.out.println("Update view called");
        System.out.println("Enemies: " + enemies);

        // Log positions of enemies
        for (Enemy enemy : enemies) {
            System.out.println("Enemy position: " + enemy.getCoordinate());
        }
        
        // GraphicsContext gc = canvas.getGraphicsContext2D();
        map = GameMap.toIntArray(controller.getGameManager().getGameMap());
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawMap(gc);

        // Draw towers
        // towerView.renderTowers(gc, towers);
        // Draw enemies
        enemyView.renderEnemies(gc, enemies, imgNum);

        // Draw projectiles as small red dots
        gc.setFill(Color.RED);
        for (Projectile projectile : projectiles) {
            // Get the projectile's current position
            Point2D position = projectile.getCoordinate();
            
            // Transform model coordinates to view coordinates using the same scaling as for enemies
            double modelWidth = 1920;  // The width used in the model
            double modelHeight = 1080; // The height used in the model
            double scaleFactor = TILE_SIZE * COLS / modelWidth; // Calculate the scale factor
            
            // Scale positions from model space to view space
            double viewX = position.getX() * scaleFactor;
            double viewY = position.getY() * scaleFactor;
            
            // Log the transformed coordinates
            System.out.println("Drawing projectile at model coordinate: " + position + 
                            ", scaled view coordinate: (" + viewX + ", " + viewY + ")");
            
            // Draw the projectile with the scaled coordinates
            gc.fillOval(viewX - 5, viewY - 5, 10, 10);
            
            // Alternative approach with larger, more visible projectiles based on type
            switch(projectile.getProjectileType()) {
                case ARROW:
                    gc.setFill(Color.DARKGREEN);
                    gc.fillOval(viewX - 3, viewY - 3, 6, 6);
                    break;
                case MAGIC:
                    gc.setFill(Color.BLUE);
                    gc.fillOval(viewX - 3, viewY - 3, 6, 6);
                    break;
                case ARTILLERY:
                    gc.setFill(Color.ORANGE);
                    gc.fillOval(viewX - 3, viewY - 3, 6, 6);
                    break;
            }
        }
        // End of projectile rendering
    }

    @Override
    public void update(Object arg) {
        currentGold = controller.getGameManager().getPlayer().getCurrentGold();
        currentHealth = controller.getGameManager().getPlayer().getCurrentHealth();
        
        goldText.setText(String.valueOf(currentGold));
        healthText.setText(String.valueOf(currentHealth));
        System.out.println("Observer update called with argument: " + arg);

        map = GameMap.toIntArray(controller.getGameManager().getGameMap());
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawMap(gc);
    }
}
