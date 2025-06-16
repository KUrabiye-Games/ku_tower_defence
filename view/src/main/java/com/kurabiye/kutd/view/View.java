package com.kurabiye.kutd.view;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class View extends Application {

    @Override
    public void start(Stage stage) {
        // Start the Launch Screen
        LaunchScreenView launchScreen = new LaunchScreenView();
        launchScreen.start(stage);
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}
