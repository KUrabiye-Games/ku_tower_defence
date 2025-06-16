package com.kurabiye.kutd.view;

import javafx.application.Application;
import javafx.stage.Stage;

public class View extends Application {

    @Override
    public void start(Stage stage) {
        Image image = new Image(getClass().getResourceAsStream("/assets/cookie_bitten1.png"));
        stage.getIcons().add(image);

        // Start the Launch Screen
        launchScreen.start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
