package com.kurabiye.kutd.controller;

import com.kurabiye.kutd.model.Managers.GameManager;
import com.kurabiye.kutd.model.Map.GameMap;

import javafx.stage.Stage;

public class MainMenuController {


    public MainMenuController() {
        
    }

    // Called when the "Play Game" button is pressed
    public GamePlayController onPlayButtonPressed() {
       
        GameMap defaultGameMap = GameMap.getPrebuiltMap();
        GameManager gameManager = new GameManager(defaultGameMap);
        GamePlayController gamePlayController = new GamePlayController(gameManager);
        return gamePlayController;
    }

    // Called when the "Settings" button is clicked (can be implemented later)
    public void openSettings() {
        System.out.println("Opening settings...");
    }

    // Called when the "Quit" button is clicked
    public void quitGame() {
        System.out.println("Quitting the game...");
        System.exit(0); // Exit the application
    }
}
