package com.kurabiye.kutd.controller;

import com.kurabiye.kutd.view.MainMenuView;
import com.kurabiye.kutd.model.GameManager;

public class MainMenuController extends Controller{

    private MainMenuView mainMenuView;
    private GameManager gameManager;

    public MainMenuController(MainMenuView mainMenuView, GameManager gameManager) {
        this.mainMenuView = mainMenuView;
        this.gameManager = gameManager;
        // Controller's constructor already calls initialize()
    }

    @Override
    protected void initialize() {
        mainMenuView.setPlayButtonAction(() -> onPlayButtonPressed());
        mainMenuView.setMapEditorButtonAction(() -> onMapEditorButtonPressed());
        mainMenuView.setSettingsButtonAction(() -> onSettingsButtonPressed());
        mainMenuView.setExitButtonAction(() -> onExitButtonPressed());
    }

    private void onPlayButtonPressed() {
        MapSelectionView mapSelectionView = new MapSelectionView();
        mapSelectionView.setOnMapSelected(this::onMapSelected);
        mapSelectionView.show();
    }

    private void onMapSelected(String mapName) {
        // Load the selected map
        Tile[][] selectedMap = mapPersistenceManager.loadMap(mapName);
        
        if (selectedMap == null) {
            mainMenuView.showError("Failed to load the selected map.");
            return;
        }
        
        // Initialize the Gameplay View
        GamePlayView gamePlayView = new GamePlayView();
        
        // Pass the selected map to the GameManager and start the game
        gameManager.startGame(selectedMap);
    
        // Create the Gameplay Controller to connect GameManager and GamePlayView
        new GamePlayController(gamePlayView, gameManager);
        
        gamePlayView.show();
    }
    

    private void onMapEditorButtonPressed() {
        // Initialize Map Editor independently
        MapEditorView mapEditorView = new MapEditorView();
        MapEditor mapEditor = new MapEditor();
        MapPersistenceManager mapPersistenceManager = new MapPersistenceManager();
        new MapEditorController(mapEditorView, mapEditor, mapPersistenceManager);
        mapEditorView.show();
    }

    private void onSettingsButtonPressed() {
        // Initialize Settings independently
        SettingsView settingsView = new SettingsView();
        SettingsManager settingsManager = new SettingsManager();
        new SettingsController(settingsView, settingsManager);
        settingsView.show();
    }

    private void onExitButtonPressed() {
        System.exit(0);
    }
}


    
