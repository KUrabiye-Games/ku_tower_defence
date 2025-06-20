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
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


import java.util.ArrayList;
import java.util.HashSet;

import javafx.scene.text.TextAlignment;
import javafx.scene.control.Tooltip;

import java.util.List;
import java.util.Set;


import com.kurabiye.kutd.controller.GamePlayController;
import com.kurabiye.kutd.controller.MapSelectionController;
import com.kurabiye.kutd.model.Collectable.GoldBag;
import com.kurabiye.kutd.model.Collectable.ICollectable;
import com.kurabiye.kutd.model.Coordinates.Point2D;
import com.kurabiye.kutd.model.Enemy.EnemyType;
import com.kurabiye.kutd.model.Enemy.IEnemy;
import com.kurabiye.kutd.model.Listeners.IGameUpdateListener;
import com.kurabiye.kutd.model.Managers.GameState;
import com.kurabiye.kutd.model.Map.GameMap;
import com.kurabiye.kutd.model.Projectile.IProjectile;
import com.kurabiye.kutd.model.Projectile.ProjectileState;
import com.kurabiye.kutd.model.Projectile.ProjectileType;
import com.kurabiye.kutd.util.DynamicList.DynamicArrayList;
import com.kurabiye.kutd.util.ObserverPattern.Observer;
import com.kurabiye.kutd.view.Animation.AnimationManager;
import com.kurabiye.kutd.view.Animation.Sprite;
import com.kurabiye.kutd.model.Tower.ITower;
import com.kurabiye.kutd.model.Tower.TowerType;
import com.kurabiye.kutd.view.Animation.AnimationManager;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.geometry.VPos;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;

public class GamePlayView implements IGameUpdateListener, Observer {

    private MusicManager musicManager;

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

    private static final int TILE_COUNT = 35;
    private static final int GRASS_TILE_ID = 5;
    private static final int INTERACTIVE_TILE_ID = 15;

    private final Image[] tileImages = new Image[TILE_COUNT];
    private Image[] buttonImages = new Image[3]; // For the three button icons

    private Image blueButtonImage;
    private Image iconsImage;
    private Image playImage; // Play button image
    private Image pauseImage; // Pause button image
    private Image accelerateImage; // Speed up image
    private Image settingsImage; // Settings image
    private Image goldBagImage = new Image(getClass().getResourceAsStream("/assets/collectables/gold_bag.png"));

    private Button playPauseButton;
    private boolean isGamePlaying = true;
    private boolean isGameAccelerated = false;
    private boolean isEndGamePopupShown = false; // Flag to prevent multiple popups
    private Stage currentStage; // Store the stage reference
    private Pane root;
    private Canvas canvas;
    private GraphicsContext gc;
    private HBox buttonContainer;

    private ITower selectedTower = null;

    private GamePlayController controller;
    private AnimationManager animationManager = new AnimationManager(TILE_SIZE, COLS);
    private Image goldBagSpriteSheet = new Image(getClass().getResourceAsStream("/assets/animations/G_Spawn.png"));
    private Image explosionSpriteSheet = new Image(getClass().getResourceAsStream("/assets/animations/Explosions.png"));

    private EnemyView enemyView;
    // private TowerView towerView;
    private ProjectileView projectileView;


    private Image[] projectileImages = new Image[4]; // Array to store projectile images

    List<IEnemy> enemies;
    List<ITower> towers;
    // Projectiles projectiles;

    // Removed Enemiea Projectiles from here

    List<IProjectile> projectiles;

    private int currentGold;
    private int currentHealth;
    private int currentWave;

    private Text goldText;
    private Text healthText;
    private Text waveText; // Add this as a class field

    
    private int[][] map;

    // Gold sound effect
    private AudioClip goldSound;

    /**
     * Initializes and starts the game view with the provided stage and controller.
     * 
     * @requires stage != null && controller != null
     * @requires JavaFX Application Thread is running
     * @requires controller.getGameManager() != null
     * @requires all required asset files exist in resources (/assets/tiles/,
     *           /assets/buttons/, /assets/projectiles/, /assets/ui/)
     * 
     * @modifies this.controller, this.currentStage, this.isEndGamePopupShown
     * @modifies this.enemyView, this.enemies, this.towers, this.projectiles
     * @modifies this.currentGold, this.currentHealth, this.currentWave
     * @modifies this.canvas, this.gc, this.root, this.map
     * @modifies stage (sets title, scene, maximized state, and shows the stage)
     * @modifies controller (sets listeners and starts the game)
     * 
     * @effects Loads all required assets (tiles, button icons, projectile images)
     * @effects Creates and configures the game canvas with calculated dimensions
     * @effects Sets up the UI layout with game elements positioned on screen
     * @effects Initializes the game state by connecting to the controller's game
     *          manager
     * @effects Registers this view as a listener for game updates and observer for
     *          player/map changes
     * @effects Starts the game thread through the controller
     * @effects Sets the stage title to "Game Map" and maximizes the window
     * @effects Sets up mouse click handlers for tile interactions
     * @effects Applies custom cursor if available, falls back to default cursor on
     *          failure
     */
    public void start(Stage stage, GamePlayController controller) {

        /*
         * int x = 5;
         * 
         * if (x == 5) {
         * throw new IllegalArgumentException("Stage and controller cannot be null");
         * }
         */

        loadTiles();
        loadButtonIcons();
        loadProjectileImages();

        musicManager = new MusicManager();
        musicManager.playcombat();

        this.currentStage = stage; // Store the stage
        this.isEndGamePopupShown = false; // Reset flag on start

        this.controller = controller;
        this.enemyView = new EnemyView(TILE_SIZE); // Pass just the tile size
        // this.towerView = new TowerView(TILE_SIZE); // Pass just the tile size

        this.projectileView = new ProjectileView(projectileImages, TILE_SIZE, COLS);

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

        map = controller.getGameManager().getGameMap().toIntArray();

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

        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);

        stage.setTitle("Game Map");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();

        // Use Timeline to delay cursor setting slightly
        Timeline cursorDelay = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            try {
                Image cursorImage = new Image(getClass().getResourceAsStream("/assets/ui/cursor.png"));
                if (cursorImage != null && !cursorImage.isError()) {
                    ImageCursor customCursor = new ImageCursor(cursorImage,
                            cursorImage.getWidth() / 2,
                            cursorImage.getHeight() / 2);
                    scene.setCursor(customCursor);
                    root.setCursor(customCursor);
                }
            } catch (Exception ex) {
                // Failed to load custom cursor, will use default cursor
                ex.printStackTrace();
            }
        }));
        cursorDelay.play();

        setupClickHandler();

        // Initialize gold sound effect
        try {
            goldSound = new AudioClip(getClass().getResource("/assets/audio/gold.mp3").toExternalForm());
        } catch (Exception e) {
            System.err.println("Failed to load gold sound: " + e.getMessage());
            goldSound = null;
        }
    }

    public GamePlayController getController() {
        return controller;
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

    private void loadProjectileImages() {
        projectileImages[0] = new Image(getClass().getResourceAsStream("/assets/projectiles/arrow.png")); // Arrow projectile
        projectileImages[1] = new Image(getClass().getResourceAsStream("/assets/projectiles/magic.png")); // Magic projectile
        projectileImages[2] = new Image(getClass().getResourceAsStream("/assets/projectiles/bomb.png")); // Artillery projectile
        projectileImages[3] = new Image(getClass().getResourceAsStream("/assets/projectiles/magic2.png")); // Ice projectile
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
            // First check for collectables
            double clickX = event.getX();
            double clickY = event.getY();

            // Convert click coordinates to model space
            double modelWidth = 1920; // The width used in the model
            double scaleFactor = TILE_SIZE * COLS / modelWidth;
            Point2D clickPoint = new Point2D(clickX / scaleFactor, clickY / scaleFactor);

            // Try to collect gold bag first
            boolean collected = controller.getGameManager().handleCollectableClick(clickPoint);

            if (collected) {
                animationManager.handleClick(clickPoint);
            }

            if (!collected) {
                // If no collectable was clicked, handle tower/tile interactions
                int col = (int) (clickX / TILE_SIZE);
                int row = (int) (clickY / TILE_SIZE);

                if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
                    int tileId = map[row][col];

                    if (tileId == 20 || tileId == 21 || tileId == 26 || tileId == 32 || tileId == 33 || tileId == 34) {
                        showTowerButton(row, col);
                    } else if (tileId == INTERACTIVE_TILE_ID) {
                        showBuildButtons(row, col);
                    } else {
                        removeButtonContainer();
                    }
                } else {
                    removeButtonContainer();
                }
            } else {
                // If we collected something, remove any UI elements
                removeButtonContainer();
            }
        });
    }

    private void removeButtonContainer() {
        if (buttonContainer != null) {
            root.getChildren().remove(buttonContainer);
            buttonContainer = null;
            selectedTower = null; // Clear the selected tower when buttons are removed
        }
    }

    private void showTowerButton(int row, int col) {
        removeButtonContainer();

        buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);

        double tileLeftX = col * TILE_SIZE;
        double tileTopY = row * TILE_SIZE;

        buttonContainer.setLayoutX(tileLeftX + (TILE_SIZE / 2) - 90);
        buttonContainer.setLayoutY(tileTopY - 40);

        Button sellButton = new Button("Sell");
        sellButton.setStyle(
                "-fx-background-color: linear-gradient(#ff5400, #be1d00);" +
                        "-fx-background-radius: 8;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 12px;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 8, 0, 0, 2);" +
                        "-fx-border-color: #8B0000;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;");
        sellButton.setPrefSize(85, 35);
        sellButton.setOnMouseEntered(e -> sellButton.setStyle(
                "-fx-background-color: linear-gradient(#ff6400, #ce2d00);" +
                        "-fx-background-radius: 8;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 12px;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 10, 0, 0, 3);" +
                        "-fx-border-color: #8B0000;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;"));
        sellButton.setOnMouseExited(e -> sellButton.setStyle(
                "-fx-background-color: linear-gradient(#ff5400, #be1d00);" +
                        "-fx-background-radius: 8;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 12px;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 8, 0, 0, 2);" +
                        "-fx-border-color: #8B0000;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;"));
        sellButton.setOnAction(e -> handleSellButtonClick(row, col));

        ITower clickedTower = null;
        for (ITower tower : controller.getGameManager().getTowers()) {
            if (tower.getTileCoordinate().getTileX() == col && tower.getTileCoordinate().getTileY() == row) {
                clickedTower = tower;
                break;
            }
        }

        selectedTower = clickedTower;

        buttonContainer.getChildren().add(sellButton);

        if (clickedTower != null && clickedTower.canUpgrade()) {
            int upgradeCost = clickedTower.getUpgradeCost();
            Button upgradeButton = new Button("Upgrade\n$" + upgradeCost);
            upgradeButton.setStyle(
                    "-fx-background-color: linear-gradient(#32CD32, #228B22);" +
                            "-fx-background-radius: 8;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-weight: bold;" +
                            "-fx-font-size: 11px;" +
                            "-fx-text-alignment: center;" +
                            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 8, 0, 0, 2);" +
                            "-fx-border-color: #006400;" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius: 8;");
            upgradeButton.setPrefSize(85, 45);
            upgradeButton.setOnMouseEntered(e -> upgradeButton.setStyle(
                    "-fx-background-color: linear-gradient(#3AE03A, #2E8B2E);" +
                            "-fx-background-radius: 8;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-weight: bold;" +
                            "-fx-font-size: 11px;" +
                            "-fx-text-alignment: center;" +
                            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 10, 0, 0, 3);" +
                            "-fx-border-color: #006400;" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius: 8;"));
            upgradeButton.setOnMouseExited(e -> upgradeButton.setStyle(
                    "-fx-background-color: linear-gradient(#32CD32, #228B22);" +
                            "-fx-background-radius: 8;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-weight: bold;" +
                            "-fx-font-size: 11px;" +
                            "-fx-text-alignment: center;" +
                            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 8, 0, 0, 2);" +
                            "-fx-border-color: #006400;" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius: 8;"));
            upgradeButton.setOnAction(e -> handleUpgradeButtonClick(row, col));
            buttonContainer.getChildren().add(upgradeButton);
        }

        root.getChildren().add(buttonContainer);
    }

    private void handleSellButtonClick(int row, int col) {

        // Call the controller's sellTower method with the tower type
        controller.sellTower(col, row);

        removeButtonContainer();
    }

    private void handleUpgradeButtonClick(int row, int col) {

        boolean success = controller.upgradeTower(col, row);

        if (success) {
            removeButtonContainer();
        } else {
            removeButtonContainer();
        }
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

            // Add tooltip based on button type
            Tooltip tooltip = new Tooltip();
            switch (i) {
                case 0: // Mage Tower
                    tooltip.setText("Mage Tower\n" +
                            "Cost: 75 gold\n" +
                            "Damage: 7\n" +
                            "Range: 300\n" +
                            "Attack Speed: 1\n" +
                            "Reselling price: 45 gold");
                    break;
                case 1: // Archer Tower
                    tooltip.setText("Archer Tower\n" +
                            "Cost: 100 gold\n" +
                            "Damage: 8\n" +
                            "Range: 300\n" +
                            "Attack Speed: 5\n" +
                            "Reselling price: 70 gold");
                    break;
                case 2: // Artillery Tower
                    tooltip.setText("Artillery Tower\n" +
                            "Cost: 50 gold\n" +
                            "Damage: 5\n" +
                            "Range: 300\n" +
                            "Attack Speed: 0.5\n" +
                            "Reselling price: 25 gold");
                    break;
            }

            // Style the tooltip (optional)
            tooltip.setStyle(
                    "-fx-font-size: 12px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: white;" +
                            "-fx-background-color: rgba(50, 50, 50, 0.9);" +
                            "-fx-border-color: #555;" +
                            "-fx-border-width: 1px;" +
                            "-fx-border-radius: 3px;" +
                            "-fx-padding: 5px;");

            Tooltip.install(button, tooltip);

            final int buttonId = i;
            button.setOnAction(e -> handleBuildButtonClick(buttonId, row, col));

            buttonContainer.getChildren().add(button);
        }

        root.getChildren().add(buttonContainer);
    }

    private void handleBuildButtonClick(int buttonId, int row, int col) {

        // Map button IDs to tower types (0=Magic/Star, 1=Artillery/Bomb,
        // 2=Archer/Arrow)
        TowerType towerType;
        switch (buttonId) {
            case 0: // Star button - creates Magic tower
                towerType = TowerType.MAGE; // MAGIC tower type
                break;
            case 1: // Arrow button - creates Archer tower
                towerType = TowerType.ARCHER; // ARROW tower type
                break;
            case 2: // Bomb button - creates Artillery tower
                towerType = TowerType.ARTILLERY; // ARTILLERY tower type
                break;
            default:
                return;
        }

        // Tell the controller to build a tower of the selected type
        boolean success = controller.buildTower(col, row, towerType);

        if (success) {
        } else {
        }

        removeButtonContainer();
    }

    private void addUIElements(Stage stage) {
        VBox buttonColumn = new VBox(10); // Vertical layout with spacing
        buttonColumn.setLayoutX(64); // Next to icons
        buttonColumn.setLayoutY(10);

        goldText = new Text("" + currentGold);
        goldText.setFill(Color.WHITE);
        goldText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 18));
        goldText.setTextOrigin(VPos.CENTER);

        healthText = new Text("" + currentHealth);
        healthText.setFill(Color.WHITE);
        healthText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 18));
        healthText.setTextOrigin(VPos.CENTER);

        waveText = new Text(""+currentWave);
        waveText.setFill(Color.WHITE);
        waveText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 18));
        waveText.setTextOrigin(VPos.CENTER);

        for (int i = 0; i < 3; i++) {
            ImageView blueButton = new ImageView(blueButtonImage);
            blueButton.setFitWidth(120);
            blueButton.setFitHeight(42);

            // For the first button (gold button), create a stack pane to overlay text and
            // image
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
                ((ImageView) playPauseButton.getGraphic()).setImage(playImage);

                // Show pause menu
                showPauseMenu(stage);
            } else {
                // Currently paused, resume the game
                controller.resumeGame();
                ((ImageView) playPauseButton.getGraphic()).setImage(pauseImage);
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
            ((ImageView) playPauseButton.getGraphic()).setImage(playImage);

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
                        "-fx-faint-focus-color: transparent;");

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
                        "-fx-padding: 20px;");
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
            ((ImageView) playPauseButton.getGraphic()).setImage(pauseImage);
            isGamePlaying = true;
        });

        mainMenuButton.setOnAction(event -> {
            // End the current game
            musicManager.stopCurrentMusic();

            controller.endGame();

            // Reset stage to normal size before returning to main menu
            stage.setMaximized(false);
            stage.setWidth(600);
            stage.setHeight(420);
            stage.centerOnScreen();
            
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
                        "-fx-background-radius: 8px;");

        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: rgb(34, 220, 155);" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 8px;"));

        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: rgb(0, 218, 142);" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 8px;"));

        return button;
    }

    private void showEndGamePopup(GameState state, Stage stage) {
        // Create a semi-transparent overlay
        Pane overlay = new Pane();
        overlay.setPrefSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");

        // Create the end game menu container
        VBox endGameMenu = new VBox(15);
        endGameMenu.setAlignment(Pos.CENTER);
        endGameMenu.setStyle(
                "-fx-background-color: rgba(50, 50, 50, 0.9);" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 20px;");
        endGameMenu.setMaxWidth(350); // Slightly wider for longer text
        endGameMenu.setMaxHeight(300);

        // Create the end game title based on the state
        String titleText = (state == GameState.GAME_WON) ? "YOU WON!" : "YOU LOST!";
        Text endGameTitle = new Text(titleText);
        endGameTitle.setFont(Font.font("System", FontWeight.BOLD, 28));
        endGameTitle.setFill(Color.WHITE);

        // Create buttons
        Button playAgainButton = createPauseMenuButton("Play Again"); // Reuse styling
        Button mainMenuButton = createPauseMenuButton("Return to Main Menu"); // Reuse styling

        // Add action handlers
        playAgainButton.setOnAction(event -> {
            // Clean up current game
            musicManager.stopCurrentMusic();
            controller.endGame();
            root.getChildren().remove(overlay);

            // Show map selection view for new game
            MapSelectionView mapSelectionView = new MapSelectionView();
            mapSelectionView.start(stage, new MapSelectionController());
        });

        mainMenuButton.setOnAction(event -> {
            
            musicManager.stopCurrentMusic(); // Stop any current music

            controller.endGame(); // Clean up the current game
            root.getChildren().remove(overlay); // Remove the popup
            
            // Return to main menu
            // Reset stage to normal size before returning to main menu
            stage.setMaximized(false);
            stage.setWidth(600);
            stage.setHeight(420);
            stage.centerOnScreen();

            MainMenuView mainMenuView = new MainMenuView();
            mainMenuView.start(stage);
        });

        // Add all elements to the end game menu
        endGameMenu.getChildren().addAll(endGameTitle, playAgainButton, mainMenuButton);

        // Center the end game menu on screen
        endGameMenu.setLayoutX((SCREEN_WIDTH - 350) / 2);
        endGameMenu.setLayoutY((SCREEN_HEIGHT - 300) / 2);

        // Add overlay and end game menu to the root
        overlay.getChildren().add(endGameMenu);
        root.getChildren().add(overlay);

        // Ensure the popup is brought to the front
        overlay.toFront();
    }

    // Method called by the controller to update the game view
    @Override
    public void onGameUpdate(double deltaTime) {
        // This must be called on the JavaFX Application Thread
        // So we wrap it in Platform.runLater
        Platform.runLater(() -> {
            updateView(deltaTime);

            // Check game state for win/loss condition
            GameState currentState = controller.getGameManager().getGameState();
            if (!isEndGamePopupShown && (currentState == GameState.GAME_WON || currentState == GameState.GAME_LOST)) {
                showEndGamePopup(currentState, currentStage);
                isEndGamePopupShown = true; // Set flag to true once popup is shown
            }
        });
    }

    private double pastTime = 0.0;

    private void updateView(double deltaTime) {

        // Check if the popup is already shown, if so, don't update the view further
        if (isEndGamePopupShown) {
            return;
        }

        pastTime += deltaTime;

        int imgNum = ((int) (pastTime * 6)) % 6;

        // GraphicsContext gc = canvas.getGraphicsContext2D();
        map = controller.getGameManager().getGameMap().toIntArray();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawMap(gc);

        for (IProjectile projectile : projectiles) {
            // Get the projectile's current position
            Point2D position = projectile.getCoordinate();

            // Transform model coordinates to view coordinates using the same scaling as for
            // enemies
            double modelWidth = 1920; // The width used in the model
            double scaleFactor = TILE_SIZE * COLS / modelWidth; // Calculate the scale factor

            // Scale positions from model space to view space
            double viewX = position.getX() * scaleFactor;
            double viewY = position.getY() * scaleFactor;

            // Determine the projectile type and select the corresponding image
            Image projectileImage = null;
            double imageSize = 20; // Default size for projectiles
            boolean shouldRotate = false;

            switch (projectile.getProjectileType()) {
                case ARROW:
                    projectileImage = projectileImages[0];
                    imageSize = 30; // Larger size for arrows
                    shouldRotate = true; // Arrows need to be rotated
                    break;
                case MAGIC:
                    // If shot from a level 2 magic tower, use a different image
                  
                        projectileImage = projectileImages[3]; // Use ice projectile image
                    
                    
                    imageSize = 35; // Larger size for magic projectiles
                    break;
                case ARTILLERY:

                    projectileImage = projectileImages[2];
                    imageSize = 15; // Smaller size for bombs
                    break;
            }

            // Draw the projectile image if it exists
            if (projectileImage != null) {
                if (shouldRotate) {
                    // Calculate the rotation angle based on the speed vector
                    double angle = Math.toDegrees(
                            Math.atan2(projectile.getSpeedVector().getY(), projectile.getSpeedVector().getX()));

                    // Save the current state of the GraphicsContext
                    gc.save();

                    // Translate to the center of the projectile
                    gc.translate(viewX, viewY);

                    // Rotate the canvas
                    gc.rotate(angle);

                    // Draw the image centered at (0, 0) after translation
                    gc.drawImage(projectileImage, -imageSize / 2, -imageSize / 2, imageSize, imageSize);

                    // Restore the GraphicsContext to its original state
                    gc.restore();
                } else {
                    // Draw the image without rotation
                    gc.drawImage(projectileImage, viewX - imageSize / 2, viewY - imageSize / 2, imageSize, imageSize);
                }
            }

        


            if (projectile.getProjectileType() == ProjectileType.ARTILLERY &&
                projectile.getProjectileState() == ProjectileState.STOPPED &&
                !projectile.hasExplosionAnimated()) {

                animationManager.createAnimation(
                        gc,
                        explosionSpriteSheet, // patlama sprite'ınızın Image'ı
                        position, // koordinatlar
                        0.2, // frame süresi (örneğin)
                        1.4, // toplam animasyon süresi
                        64, // genişlik
                        64 // yükseklik
                );

                projectile.setExplosionAnimated(true);
            }

        }

        for (IEnemy enemy : enemies) {
            if (enemy.isDead()) {
                //System.out.println("heyyyy");
                if(enemy.getEnemyType() == EnemyType.KNIGHT){
                    Image deathStrip1 = new Image(getClass().getResourceAsStream("/assets/animations/knight_death.png"));
                    animationManager.createAnimation(gc, deathStrip1, enemy.getCoordinate(), 0.2, 1.0, 120, 120);

                }
                if(enemy.getEnemyType() == EnemyType.GOBLIN){
                    Image deathStrip2 = new Image(getClass().getResourceAsStream("/assets/animations/goblin_death.png"));
                    animationManager.createAnimation(gc, deathStrip2, enemy.getCoordinate(), 0.1, 0.8, 64, 64);

                }
                
            }

        }

        // Draw enemies
        enemyView.renderEnemies(gc, enemies, imgNum);
        projectileView.renderProjectiles(gc, projectiles);

        // By Atlas
        renderCollectables(gc);
        renderTowerRanges(gc);
        animationManager.update(deltaTime);


        //controller.getGameManager().getCollisionManager().commitAll();
    
    }


    public void renderCollectables(GraphicsContext gc) {
        DynamicArrayList<ICollectable<?>> collectables = controller.getGameManager().getCollectables();

        // Calculate scale factor
        double modelWidth = 1920; // The width used in the model
        double scaleFactor = TILE_SIZE * COLS / modelWidth;

        for (ICollectable<?> collectable : collectables) {
            if (collectable instanceof GoldBag) {
                GoldBag goldBag = (GoldBag) collectable;
                Point2D pos = goldBag.getCoordinates();

                // Scale the position from model space to view space
                double viewX = pos.getX() * scaleFactor;
                double viewY = pos.getY() * scaleFactor;

                if (!goldBag.isAnimated()) {
                    int id = animationManager.createAnimationReturningId(
                            gc, goldBagSpriteSheet, pos, 0.2, goldBag.getLifespan(), 80, 80);
                    goldBag.setAnimated(true);
                    goldBag.setAnimationId(id); // opsiyonel: animasyonu iptal etmek istersen
                }

                gc.setFill(Color.BLACK);
                gc.fillText(String.valueOf(goldBag.getItem()),
                        viewX, viewY - 20);
            }
        }
    }

    public void renderTowerRanges(GraphicsContext gc) {
        // Only render range if a tower is selected
        if (selectedTower == null) {
            return;
        }

        // Calculate the scale factor just like we do for projectiles
        double modelWidth = 1920; // The width used in the model
        double scaleFactor = TILE_SIZE * COLS / modelWidth;

        // Only render the selected tower's range, not all towers
        double range = selectedTower.getRange() * scaleFactor; // Scale the range
        Point2D position = selectedTower.getTileCoordinate().getCenter();

        // Scale the position coordinates
        double viewX = position.getX() * scaleFactor;
        double viewY = position.getY() * scaleFactor;

        // Calculate the top-left corner of the range oval
        double topLeftX = viewX - range;
        double topLeftY = viewY - (range * 0.6); // Reduce vertical height by 40%

        // Draw a vertically squeezed oval for the range
        gc.setStroke(Color.rgb(190, 120, 120, 0.5));
        gc.setLineWidth(2);
        gc.strokeOval(topLeftX, topLeftY, range * 2, range * 1.2); // Use diameter (range * 2) for width
    }

    @Override
    public void update(Object arg) {
        int previousGold = this.currentGold;
        currentGold = controller.getGameManager().getPlayer().getCurrentGold();
        currentHealth = controller.getGameManager().getPlayer().getCurrentHealth();
        currentWave = controller.getGameManager().getCurrentWaveIndex(); // Update current wave
        
        
         if (goldText != null) {
            goldText.setText(String.valueOf(currentGold));
        }
        
        // Play sound if gold increased
        if (currentGold > previousGold && goldSound != null) {
            goldSound.play();
        }
        
        if (healthText != null) {

            healthText.setText(String.valueOf(currentHealth));
        }
        if (waveText != null) {
            waveText.setText(String.valueOf(currentWave + 1)); // +1 for 1-based display
        }

        map = controller.getGameManager().getGameMap().toIntArray();

        if (gc == null) {
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            drawMap(gc);
        }

        double deltaTime = 0.0; // Placeholder for actual deltaTime
        updateView(deltaTime); // Pass a dummy deltaTime for now
    }
}
