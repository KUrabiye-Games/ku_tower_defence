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
        System.out.println("Play Game button pressed");
        gameManager.startNewGame();
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


    
