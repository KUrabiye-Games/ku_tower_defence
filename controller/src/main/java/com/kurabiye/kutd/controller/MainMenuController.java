package com.kurabiye.kutd.controller;

import com.kurabiye.kutd.model.Map.GameMapRepository;

public class MainMenuController {
    private final SettingsController settingsController;
    private final GameMapRepository mapRepository;

    public MainMenuController() {
        this.settingsController = new SettingsController();
        this.mapRepository = GameMapRepository.getInstance();
    }

    // Called when the "Play Game" button is pressed
    public MapSelectionController onPlayButtonPressed() {
       
        MapSelectionController mapSelectionController = new MapSelectionController();
        return mapSelectionController;
    }


    // Called when the "Settings" button is clicked (can be implemented later)
    public SettingsController onSettingsButtonPressed() {
        
        SettingsController settingsController = new SettingsController();
        return settingsController;
    }

    public MapEditorController onMapEditorButtonPressed() {
        
        MapEditorController mapEditorController = new MapEditorController();
        return mapEditorController;
    }

    // Called when the "Quit" button is clicked
    public void quitGame() {
      
        System.exit(0); // Exit the application
    }
}
