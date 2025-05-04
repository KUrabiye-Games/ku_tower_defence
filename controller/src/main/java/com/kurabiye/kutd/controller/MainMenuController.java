package com.kurabiye.kutd.controller;

import com.kurabiye.kutd.model.Listeners.IGameUpdateListener;
import com.kurabiye.kutd.model.Managers.GameManager;
import com.kurabiye.kutd.model.Map.GameMap;

import javafx.stage.Stage;

public class MainMenuController {


    public MainMenuController() {
        
    }

    // Called when the "Play Game" button is pressed
    public GamePlayController onPlayButtonPressed() {
       
        GamePlayController gamePlayController = new GamePlayController();
        return gamePlayController;
    }


    // Called when the "Settings" button is clicked (can be implemented later)
    public SettingsController onSettingsButtonPressed() {
        
        SettingsController settingsController = new SettingsController();
        return settingsController;
    }

    // Called when the "Quit" button is clicked
    public void quitGame() {
      
        System.exit(0); // Exit the application
    }
}
