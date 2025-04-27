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
        System.out.println("Map Editor button pressed");
        gameManager.openMapEditor();
    }

    private void onSettingsButtonPressed() {
        System.out.println("Settings button pressed");
        gameManager.openSettings();
    }

    private void onExitButtonPressed() {
        System.out.println("Exit button pressed");
        gameManager.exitGame();
    }
}


    
