package com.kurabiye.kutd.view;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class View extends Application {

    @Override
    public void start(Stage stage) {
        Image image = new Image(getClass().getResourceAsStream("/assets/cookie_bitten1.png"));
        stage.getIcons().add(image);

        // Start the Launch Screen
        LaunchScreenView launchScreen = new LaunchScreenView();
        launchScreen.start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
