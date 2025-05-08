package com.kurabiye.kutd.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import com.kurabiye.kutd.model.Map.GameMap;
import com.kurabiye.kutd.model.Tile.Tile;
import com.kurabiye.kutd.model.Tile.TileFactory;

/**
 *  MapEditorView
 */
public class MapEditorView {

    private static final int TILE_SIZE = 64;
    private static final int ROWS = 9;
    private static final int COLS = 16;
    private static final int BUTTON_SIZE = 48;

    private final Image[] tileImages = new Image[32]; //0-31 tile images
    private int[][] mapData;
    private int selectedTileType = 15; //default to buildable tile

    private Canvas canvas;
    private GraphicsContext gc;
    private Label statusLabel;

    public void start(Stage stage) {
        loadTileImages();
        initializeMapData();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #2D2D2D;");

        //create the canvas for map editing
        canvas = new Canvas(COLS * TILE_SIZE, ROWS * TILE_SIZE);
        gc = canvas.getGraphicsContext2D();
        drawMap();

        //create tile selection panel with scroll
        ScrollPane tileSelectionPanel = createTileSelectionPanel();
        tileSelectionPanel.setPrefWidth(200);

        //control buttons
        HBox controlButtons = createControlButtons(stage);

        //status bar
        statusLabel = new Label("Selected: Buildable Tile (15)");
        statusLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        statusLabel.setTextFill(Color.WHITE);
        statusLabel.setStyle("-fx-background-color: #333333; -fx-padding: 5px;");

        //layout setup
        Pane canvasContainer = new Pane(canvas);
        root.setCenter(canvasContainer);
        root.setLeft(tileSelectionPanel);
        root.setBottom(controlButtons);
        root.setTop(statusLabel);

        setupMouseHandlers();

        Scene scene = new Scene(root, COLS * TILE_SIZE + 250, ROWS * TILE_SIZE + 100);
        stage.setTitle("Map Editor");
        stage.setScene(scene);
        stage.show();
    }

    private void loadTileImages() {
        for (int i = 0; i < 32; i++) {
            String path = "/assets/tiles/tile" + i + ".png";
            try {
                tileImages[i] = new Image(getClass().getResourceAsStream(path));
            } catch (Exception e) {
                System.err.println("Failed to load tile image: " + path);
                tileImages[i] = createFallbackTileImage(i);
            }
        }
    }

    private Image createFallbackTileImage(int tileCode) {
        Canvas canvas = new Canvas(TILE_SIZE, TILE_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //colors for different tile types
        if (tileCode == 15) {
            gc.setFill(Color.LIGHTGRAY); //buildable
        } else if (tileCode >= 0 && tileCode <= 14) {
            gc.setFill(Color.SANDYBROWN); //path
        } else if (tileCode == 5) {
            gc.setFill(Color.DARKGREEN); //ground
        } else {
            gc.setFill(Color.GRAY); //other
        }

        gc.fillRect(0, 0, TILE_SIZE, TILE_SIZE);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(0, 0, TILE_SIZE, TILE_SIZE);
        gc.setFill(Color.BLACK);
        gc.fillText("" + tileCode, 10, 20);

        return canvas.snapshot(null, null);
    }

    private ScrollPane createTileSelectionPanel() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(0);
        gridPane.setVgap(0);

        //calc dimensions
        int columns = 4;
        int buttonSize = BUTTON_SIZE;
        int panelWidth = columns * buttonSize;
        int rowsNeeded = (int) Math.ceil(32 / (double) columns);
        int panelHeight = rowsNeeded * buttonSize;

        //buttons in 4-column grid
        int count = 0;
        for (int i = 0; i < 32; i++) {
            Button tileButton = createTileButton(i);
            int row = count / columns;
            int col = count % columns;
            gridPane.add(tileButton, col, row);
            count++;
        }

        //create scroll pane (scrolling disabled)
        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        //fixed dimensions
        gridPane.setPrefWidth(panelWidth);
        gridPane.setMinWidth(panelWidth);
        gridPane.setMaxWidth(panelWidth);
        scrollPane.setPrefViewportWidth(panelWidth);
        scrollPane.setPrefViewportHeight(panelHeight);

        return scrollPane;
    }

    private Button createTileButton(int tileType) {
        ImageView imageView = new ImageView(tileImages[tileType]);
        imageView.setFitWidth(BUTTON_SIZE);
        imageView.setFitHeight(BUTTON_SIZE);

        Button button = new Button();
        button.setGraphic(imageView);
        button.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-padding: 0; " +
            "-fx-border-width: 0; " +
            "-fx-background-insets: 0; " +
            "-fx-content-display: graphic-only;" //no label spacing
        );
        button.setTooltip(new Tooltip("Tile " + tileType + " - " + getTileTypeName(tileType)));

        button.setOnAction(e -> {
            selectedTileType = tileType;
            statusLabel.setText("Selected: " + getTileTypeName(tileType) + " (" + tileType + ")");
        });
        return button;
    }

    private String getTileTypeName(int tileType) {
        switch (tileType) {
            case 0:
                return "Path Start";
            case 5:
                return "Ground";
            case 15:
                return "Buildable";
            case 20:
                return "Basic Tower";
            case 21:
                return "Magic Tower";
            case 26:
                return "Archer Tower";
            default:
                if (tileType >= 0 && tileType <= 14)
                    return "Path";
                if (tileType >= 16 && tileType <= 31)
                    return "Decoration";
                return "Unknown";
        }
    }

    private void initializeMapData() {
        mapData = new int[ROWS][COLS];
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                mapData[row][col] = 5; //default to grass
            }
        }
    }

    private void drawMap() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int tileId = mapData[row][col];
                if (tileId >= 0 && tileId < tileImages.length && tileImages[tileId] != null) {
                    gc.drawImage(tileImages[tileId], col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }

    private HBox createControlButtons(Stage stage) {
        HBox buttonBox = new HBox(10);
        buttonBox.setStyle("-fx-background-color: #444444; -fx-padding: 10px;");

        Button saveButton = createTextButton("Save Map", this::saveMap);
        Button loadButton = createTextButton("Load Map", this::loadMap);
        Button validateButton = createTextButton("Validate Map", this::validateMap);
        Button returnButton = createTextButton("Main Menu", () -> returnToMenu(stage));

        buttonBox.getChildren().addAll(saveButton, loadButton, validateButton, returnButton);
        return buttonBox;
    }

    private Button createTextButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setStyle("-fx-base: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        button.setOnAction(e -> action.run());
        return button;
    }

    private void setupMouseHandlers() {
        canvas.setOnMouseClicked(e -> handleMapClick(e.getX(), e.getY()));
        canvas.setOnMouseDragged(e -> handleMapClick(e.getX(), e.getY()));
    }

    private void handleMapClick(double x, double y) {
        int col = (int) (x / TILE_SIZE);
        int row = (int) (y / TILE_SIZE);

        if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
            mapData[row][col] = selectedTileType;
            drawMap();
        }
    }

    private void saveMap() {
        statusLabel.setText("Saving map... (TODO: Implement saving)");
    }

    private void loadMap() {
        statusLabel.setText("Loading map... (TODO: Implement loading)");
    }

    private void validateMap() {
        statusLabel.setText("Validating map... (TODO: Implement validation)");
    }

    private void returnToMenu(Stage stage) {
        new MainMenuView().start(stage);
    }

}