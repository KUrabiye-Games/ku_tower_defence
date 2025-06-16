// package com.kurabiye.kutd.view;

// import org.junit.jupiter.api.*;
// import static org.junit.jupiter.api.Assertions.*;
// import javafx.scene.canvas.Canvas;

// public class MapEditorViewTest {
//     private MapEditorView mapEditor;
//     private Canvas canvas;

//     @BeforeEach
//     void setUp() {
//         mapEditor = new MapEditorView();
//         // Initialize necessary components
//         canvas = new Canvas(MapEditorView.COLS * MapEditorView.TILE_SIZE,
//                 MapEditorView.ROWS * MapEditorView.TILE_SIZE);
//         mapEditor.canvas = canvas;
//         mapEditor.gc = canvas.getGraphicsContext2D();
//         mapEditor.initializeMapData();
//     }

//     @Test
//     void testHandleMapClick_ValidCoordinates_UpdatesTile() {
//         // Set a specific tile type to place
//         mapEditor.selectedTileType = 15; // Buildable tile

//         // Click at column 5, row 3 (middle of the canvas)
//         double x = 5 * MapEditorView.TILE_SIZE + 10; // 10 pixels into the tile
//         double y = 3 * MapEditorView.TILE_SIZE + 10;

//         mapEditor.handleMapClick(x, y);

//         // Verify the tile was updated
//         assertEquals(15, mapEditor.mapData[3][5],
//                 "Tile at clicked position should be updated to selected tile type");
//     }

//     @Test
//     void testHandleMapClick_EdgeCoordinates_UpdatesTile() {
//         // Set a different tile type
//         mapEditor.selectedTileType = 0; // Path start tile

//         // Click at the very edge of the canvas (last column, first row)
//         double x = (MapEditorView.COLS - 1) * MapEditorView.TILE_SIZE;
//         double y = 0;

//         mapEditor.handleMapClick(x, y);

//         // Verify the tile was updated
//         assertEquals(0, mapEditor.mapData[0][MapEditorView.COLS - 1],
//                 "Edge tile should be updated to selected tile type");
//     }

//     @Test
//     void testHandleMapClick_OutsideCanvas_NoUpdate() {
//         // Store original map data
//         int[][] originalMap = new int[MapEditorView.ROWS][MapEditorView.COLS];
//         for (int i = 0; i < MapEditorView.ROWS; i++) {
//             System.arraycopy(mapEditor.mapData[i], 0, originalMap[i], 0, MapEditorView.COLS);
//         }

//         // Click outside the canvas (negative coordinates)
//         mapEditor.handleMapClick(-10, -10);

//         // Click outside the canvas (beyond width/height)
//         mapEditor.handleMapClick(MapEditorView.COLS * MapEditorView.TILE_SIZE + 10,
//                 MapEditorView.ROWS * MapEditorView.TILE_SIZE + 10);

//         // Verify no tiles were changed
//         assertArrayEquals(originalMap, mapEditor.mapData,
//                 "Map data should remain unchanged when clicking outside canvas");
//     }

//     @Test
//     void testHandleMapClick_DragOperation_UpdatesMultipleTiles() {
//         mapEditor.selectedTileType = 5; // Ground tile

//         // Simulate drag from (2,2) to (4,4)
//         mapEditor.handleMapClick(2 * MapEditorView.TILE_SIZE + 5, 2 * MapEditorView.TILE_SIZE + 5);
//         mapEditor.handleMapClick(3 * MapEditorView.TILE_SIZE + 5, 3 * MapEditorView.TILE_SIZE + 5);
//         mapEditor.handleMapClick(4 * MapEditorView.TILE_SIZE + 5, 4 * MapEditorView.TILE_SIZE + 5);

//         // Verify all dragged-over tiles were updated
//         assertEquals(5, mapEditor.mapData[2][2], "Tile (2,2) should be updated");
//         assertEquals(5, mapEditor.mapData[3][3], "Tile (3,3) should be updated");
//         assertEquals(5, mapEditor.mapData[4][4], "Tile (4,4) should be updated");

//     }
// }