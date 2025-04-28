package com.kurabiye.kutd.view;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LaunchScreenView {

    public void start(Stage stage) {
        // Create layout with white background
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color:rgb(33, 33, 33);");

        
        // Load all cookie PNG images
        Image cookieFullImage = new Image(getClass().getClassLoader().getResource("assets/cookie_full.png").toExternalForm());
        Image cookieBitten2Image = new Image(getClass().getClassLoader().getResource("assets/cookie_bitten2.png").toExternalForm());
        Image cookieBiteImage = new Image(getClass().getClassLoader().getResource("assets/cookie_bite.png").toExternalForm());
        
        // Create ImageViews for each cookie state
        ImageView cookieFull = new ImageView(cookieFullImage);
        ImageView cookieBitten = new ImageView(cookieBitten2Image);
        ImageView cookieBite = new ImageView(cookieBiteImage);
        
        // Set size for all cookie images
        double cookieSize = 200;
        cookieFull.setFitWidth(cookieSize);
        cookieBitten.setFitWidth(cookieSize);
        cookieBite.setFitWidth(cookieSize * 0.3); // Bite is smaller
        
        // Preserve aspect ratio
        cookieFull.setPreserveRatio(true);
        cookieBitten.setPreserveRatio(true);
        cookieBite.setPreserveRatio(true);
        
        // Initially only show the full cookie
        cookieFull.setOpacity(1);
        cookieBitten.setOpacity(0);
        cookieBite.setOpacity(0);
        
        // Position the bite piece to start from the cookie
        cookieBite.setTranslateX(30);
        cookieBite.setTranslateY(-20);
        
        // Create text for company name
        Text companyName = new Text("KUrabiye Games");
        companyName.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        companyName.setFill(Color.rgb(210, 150, 75)); // Cookie-like brown color
        companyName.setOpacity(0); // Start invisible
        companyName.setTranslateY(cookieSize / 2 + 40); // Position below cookie
        
        // Add all elements to the layout
        root.getChildren().addAll(cookieFull, cookieBitten, cookieBite, companyName);
        
        // Create animation sequence
        
        // Show full cookie for a moment
        PauseTransition initialPause = new PauseTransition(Duration.seconds(0.8));
        
        // Transition to bitten cookie and animate bite piece
        FadeTransition fadeOutFull = new FadeTransition(Duration.millis(150), cookieFull);
        fadeOutFull.setFromValue(1.0);
        fadeOutFull.setToValue(0.0);
        
        FadeTransition fadeInBitten = new FadeTransition(Duration.millis(150), cookieBitten);
        fadeInBitten.setFromValue(0.0);
        fadeInBitten.setToValue(1.0);
        
        // Animate the bite piece moving away
        Timeline moveBite = new Timeline();
        moveBite.getKeyFrames().add(
            new KeyFrame(Duration.ZERO, 
                new KeyValue(cookieBite.opacityProperty(), 0),
                new KeyValue(cookieBite.translateXProperty(), 30),
                new KeyValue(cookieBite.translateYProperty(), -20),
                new KeyValue(cookieBite.rotateProperty(), 0)
            )
        );
        moveBite.getKeyFrames().add(
            new KeyFrame(Duration.millis(100), 
                new KeyValue(cookieBite.opacityProperty(), 1)
            )
        );
        moveBite.getKeyFrames().add(
            new KeyFrame(Duration.millis(800), 
                new KeyValue(cookieBite.translateXProperty(), 120),
                new KeyValue(cookieBite.translateYProperty(), -80),
                new KeyValue(cookieBite.rotateProperty(), 45)
            )
        );
        
        // Combine bite transitions
        ParallelTransition biteTransition = new ParallelTransition(
            fadeOutFull, 
            fadeInBitten,
            moveBite
        );
        
        // Fade in company name with a slight bounce effect
        Timeline showCompanyName = new Timeline();
        showCompanyName.getKeyFrames().addAll(
            new KeyFrame(Duration.ZERO, 
                new KeyValue(companyName.opacityProperty(), 0),
                new KeyValue(companyName.scaleXProperty(), 0.8),
                new KeyValue(companyName.scaleYProperty(), 0.8)
            ),
            new KeyFrame(Duration.millis(500), 
                new KeyValue(companyName.opacityProperty(), 1),
                new KeyValue(companyName.scaleXProperty(), 1.1),
                new KeyValue(companyName.scaleYProperty(), 1.1)
            ),
            new KeyFrame(Duration.millis(700), 
                new KeyValue(companyName.scaleXProperty(), 1.0),
                new KeyValue(companyName.scaleYProperty(), 1.0)
            )
        );
        
        // Final pause before moving to loading screen
        PauseTransition finalPause = new PauseTransition(Duration.seconds(1.5));
        
        // Combine all animations in sequence
        SequentialTransition sequence = new SequentialTransition(
            initialPause,
            biteTransition,
            showCompanyName,
            finalPause
        );
        
        // After animation completes move to the Loading Screen
        sequence.setOnFinished(event -> {
            LoadingScreenView loadingScreen = new LoadingScreenView();
            loadingScreen.start(stage);
        });
        
        // Set the scene
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("KU Tower Defence - Launch Screen");
        stage.setScene(scene);
        stage.show();
        
        // Start the animation sequence
        sequence.play();
    }
}