package com.kurabiye.kutd.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class View extends Application {
    @Override
    public void start(Stage stage) {
        // Create a simple UI
        VBox root = new VBox(10);
        Button button = new Button("Hello from Tower Defence Game");
        root.getChildren().add(button);
        
        // Set up the scene
        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("KU Tower Defence");
        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
