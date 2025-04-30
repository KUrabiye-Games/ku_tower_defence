package com.kurabiye.kutd.view;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MapView {

    private static final int TILE_SIZE = 64;

   
    private static final int ROWS = 9;
    private static final int COLS = 16;
    private static final int TILE_COUNT = 32;
    private static final int GRASS_TILE_ID = 5; 


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

    public void start(Stage stage) {
        loadTiles();

        Canvas canvas = new Canvas(COLS * TILE_SIZE, ROWS * TILE_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawMap(gc);

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root);
        stage.setTitle("Game Map");
        stage.setScene(scene);
        stage.show();
    }

    private void loadTiles() {
        for (int i = 0; i < TILE_COUNT; i++) {
            String path = "/assets/tiles/tile" + i + ".png";
            tileImages[i] = new Image(getClass().getResourceAsStream(path));
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
  
}
