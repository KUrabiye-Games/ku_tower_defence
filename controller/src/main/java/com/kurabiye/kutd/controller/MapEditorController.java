// package com.kurabiye.kutd.controller;

// import com.kurabiye.kutd.view.MapEditorView;
// import com.kurabiye.kutd.model.MapManager;

// public class MapEditorController {
//     private MapEditorView mapEditorView;
//     private MapManager mapManager;
//     private MapPersistenceManager mapPersistenceManager;

//     public MapEditorController( MapManager mapManager) {
       
//     }

//     private void onTilePlaced(int x, int y, String tileType) {
//         boolean success = mapManager.placeTile(x, y, tileType);
//         if (success) {
//             mapEditorView.updateTile(x, y, tileType);
//         } else {
//             mapEditorView.showError("Cannot place tile here.");
//         }
//     }

//     private void onTileRemoved(int x, int y) {
//         boolean success = mapManager.removeTile(x, y);
//         if (success) {
//             mapEditorView.clearTile(x, y);
//         } else {
//             mapEditorView.showError("No tile to remove here.");
//         }
//     }

//     private void onSaveMap(String mapName) {
//         boolean success = mapPersistenceManager.saveMap(mapName, mapEditor.getCurrentMapGrid());
//         if (success) {
//             mapEditorView.showSuccess("Map saved successfully!");
//         } else {
//             mapEditorView.showError("Failed to save map.");
//         }
//     }

//     private void onLoadMap(String mapName) {
//         var loadedMapGrid = mapPersistenceManager.loadMap(mapName);
//         if (loadedMapGrid != null) {
//             mapEditor.setMapGrid(loadedMapGrid);
//             mapEditorView.displayMap(loadedMapGrid);
//         } else {
//             mapEditorView.showError("Failed to load map.");
//         }
//     }

//     private void onClearMap() {
//         mapManager.clearMap();
//         mapEditorView.displayMap(mapManager.getCurrentMapGrid());
//     }
// }

