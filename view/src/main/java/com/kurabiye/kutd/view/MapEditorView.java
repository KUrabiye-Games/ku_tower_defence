package com.kurabiye.kutd.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;

import java.util.List;

import com.kurabiye.kutd.controller.MapEditorController;
import com.kurabiye.kutd.model.Coordinates.TilePoint2D;
import com.kurabiye.kutd.model.Map.GameMap;
import com.kurabiye.kutd.model.Map.MapOperationResult;

/**
 *  MapEditorView
 */
public class MapEditorView {


    // Jasmine: You could use a constant as a flag instead of Integer
    private static final int NO_TILE_FLAG = 1024; // Width of the map in pixels

     static final int TILE_SIZE = 64;
     static final int ROWS = 9;
     static final int COLS = 16;
    private static final int BUTTON_SIZE = 48;

    private final Image[] tileImages = new Image[32]; //0-31 tile images
     
        
     int[][] mapData;
     private String currentMapName = null; //name of the currently loaded map
     int selectedTileType = 15; //default to buildable tile

     Canvas canvas;
     GraphicsContext gc;
     Label statusLabel;

     private Button deleteButton;


     private boolean settingStartPoint = false;
     private boolean settingEndPoint = false;

     private MapEditorController controller;

    public void start(Stage stage, MapEditorController controller) {
        
        this.controller = controller;
        mapData = controller.getTileCodeMatrix();

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
        /*Pane canvasContainer = new Pane(canvas);
        root.setCenter(canvasContainer);
        root.setLeft(tileSelectionPanel);
        root.setBottom(controlButtons);
        root.setTop(statusLabel);*/
        ScrollPane canvasScrollPane = new ScrollPane(canvas);
        canvasScrollPane.setPrefSize(COLS * TILE_SIZE, ROWS * TILE_SIZE);
        canvasScrollPane.setPannable(true);

        root.setCenter(canvasScrollPane);
        root.setLeft(tileSelectionPanel);
        root.setBottom(controlButtons);
        root.setTop(statusLabel);

        setupMouseHandlers();

        Scene scene = new Scene(root, COLS * TILE_SIZE + 250, ROWS * TILE_SIZE + 100);
        stage.setTitle("Map Editor");
        stage.setScene(scene);
        stage.setMaximized(true);
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

     void initializeMapData() {
        mapData = new int[ROWS][COLS];
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                mapData[row][col] = NO_TILE_FLAG; //default to grass
            }
        }
    }

    private void drawMap() {

        gc.clearRect(0, 0, COLS * TILE_SIZE, ROWS * TILE_SIZE);

        mapData = controller.getTileCodeMatrix();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                gc.drawImage(tileImages[5], col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                
                int topTile = mapData[row][col];
                if (topTile != NO_TILE_FLAG && topTile != 5 && tileImages[topTile] != null) {
                    gc.drawImage(tileImages[topTile], col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
                /*int tileId = mapData[row][col];
                if (tileId >= 0 && tileId < tileImages.length && tileImages[tileId] != null) {
                    gc.drawImage(tileImages[tileId], col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }*/
            }
        }

        TilePoint2D start = controller.getStartPoint();
        TilePoint2D end = controller.getEndPoint();

        if (start != null) {
            highlightTile(start.getTileX(), start.getTileY(), Color.GREEN);
        }
        if (end != null) {
            highlightTile(end.getTileX(), end.getTileY(), Color.RED);
        }
    }

    private void highlightTile(int x, int y, Color color) {
        gc.setStroke(color);
        gc.setLineWidth(3);
        gc.strokeRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    private HBox createControlButtons(Stage stage) {
        HBox buttonBox = new HBox(10);
        buttonBox.setStyle("-fx-background-color: #444444; -fx-padding: 10px;");

        Button saveButton = createTextButton("Save Map", this::saveMap);
        Button loadButton = createTextButton("Load Map", this::loadMap);
        this.deleteButton = new Button("Delete Map");
        Button clearButton = createTextButton("Clear Map", this::clearMap);
        Button returnButton = createTextButton("Main Menu", () -> returnToMenu(stage));
        
        deleteButton.setDisable(true);
        deleteButton.setOnAction(e -> deleteCurrentMap());
        deleteButton.setStyle("-fx-base: #ff4444; -fx-text-fill: white; -fx-font-weight: bold;");

        clearButton.setStyle("-fx-base: #ffa500; -fx-text-fill: white; -fx-font-weight: bold;");

        Button setStartButton = new Button("Set Start Point");
        setStartButton.setOnAction(e -> {
            settingStartPoint = true;
            settingEndPoint = false;
            statusLabel.setText("Click to set start point");
        });

        Button setEndButton = new Button("Set End Point");
        setEndButton.setOnAction(e -> {
            settingEndPoint = true;
            settingStartPoint = false;
            statusLabel.setText("Click to set end point");
        });

        buttonBox.getChildren().addAll(saveButton, loadButton, deleteButton, clearButton, returnButton, setStartButton, setEndButton);
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

    /**
     * Handles mouse clicks on the map canvas to place tiles.
     * 
     * Requires:
     * - x and y coordinates must be within canvas bounds (0 <= x < canvasWidth, 0
     * <= y < canvasHeight)
     * - selectedTileType must be a valid tile type (0 <= selectedTileType < 32)
     * 
     * Modifies:
     * - Updates the mapData array at the calculated row and column
     * - Modifies the canvas graphics by redrawing the affected tile
     * 
     * Effects:
     * - The tile at the clicked position is changed to the selectedTileType
     * - The visual representation on the canvas is updated to reflect the change
     */
    void handleMapClick(double x, double y) {
        if (x < 0 || x >= COLS * TILE_SIZE || y < 0 || y >= ROWS * TILE_SIZE) {
            return;
        }

        int col = (int) (x / TILE_SIZE);
        int row = (int) (y / TILE_SIZE);


        if (selectedTileType == 5) {
            //request to remove any top layer
            mapData[row][col] = NO_TILE_FLAG;
        } else {
            //Replace top tile (only one can exist at a time)
            mapData[row][col] = selectedTileType;
        }

        drawMap();



        if (settingStartPoint) {
            controller.setStartPoint(col, row);
            settingStartPoint = false;
            statusLabel.setText("Start point set");
        } else if (settingEndPoint) {
            controller.setEndPoint(col, row);
            settingEndPoint = false;
            statusLabel.setText("End point set");
        } else {
            controller.placeTile(col, row, selectedTileType);
        }
        drawMap();
    }


    private void returnToMenu(Stage stage) {

        // Reset stage to normal size before returning to main menu
        stage.setMaximized(false);
        stage.setWidth(600);
        stage.setHeight(420);
        stage.centerOnScreen();

        new MainMenuView().start(stage);
    }



    private void saveMap() {
        if (controller.getStartPoint() == null || controller.getEndPoint() == null) {
            showError("Please set both start and end points before saving");
            return;
        }

        if (controller.getCurrentMapName() != null) {
            // Saving existing map
            MapOperationResult result = controller.saveMap(currentMapName);
            if (result.isSuccess()) {
                showSuccess(result.getMessage());
            } else {
                showError(result.getMessage());
            }
        } else {
            // New map - ask for name
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Save Map");
            dialog.setHeaderText("Enter a name for your map:");
            dialog.setContentText("Map name:");

            dialog.showAndWait().ifPresent(mapName -> {
                MapOperationResult result = controller.saveMap(mapName);
                if (result.isSuccess()) {
                    currentMapName = mapName;
                    deleteButton.setDisable(false);
                    showSuccess(result.getMessage());
                } else {
                    showError(result.getMessage());
                }
            });
        }
    }

    private void loadMap() {
        List<String> mapNames = controller.getAvailableMapNames();
        if (mapNames.isEmpty()) {
            showError("No saved maps available");
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(mapNames.get(0), mapNames);
        dialog.setTitle("Load Map");
        dialog.setHeaderText("Select a map to load:");
        dialog.setContentText("Map:");

        dialog.showAndWait().ifPresent(mapName -> {
            clearMap();
            GameMap loadedMap = controller.loadMap(mapName);
            if (loadedMap != null) {
                drawMap();
                currentMapName = mapName;
                deleteButton.setDisable(false);
                showSuccess("Map loaded successfully");
            } else {
                showError("Failed to load map");
                clearMap();
            }
        });
    }

    private void deleteCurrentMap() {
         if (currentMapName == null || currentMapName.isEmpty()) {
            showError("No map loaded to delete");
            return;
        }

         // Confirm deletion
        if (currentMapName == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Map");
        alert.setHeaderText("Delete " + currentMapName + "?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (controller.deleteMap(currentMapName)) {
                    showSuccess("Map deleted successfully");
                    clearMap();
                    currentMapName = null;
                } else {
                    showError("Failed to delete map");
                }
            }
        });
    }

    private void clearMap() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        controller.clearMap();
        currentMapName = null;
        deleteButton.setDisable(true);
        drawMap();
    }

    public void showError(String message) {
        statusLabel.setText("Error: " + message);
        statusLabel.setStyle("-fx-background-color: #ff4444; -fx-padding: 5px;");
    }

    public void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-background-color: #44ff44; -fx-padding: 5px;");
    }

}