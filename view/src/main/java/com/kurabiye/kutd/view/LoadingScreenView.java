package com.kurabiye.kutd.view;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LoadingScreenView {

    private String[] loadingMessages = {
        "The blades are being sharpened...",
        "Goblins are meeting up...",
        "Archers are preparing their arrows...",
        "Magic shields are being cast...",
        "The castle gates are being closed...",
    };

    private ProgressBar progressBar;
    private Label messageLabel;
    private ScheduledExecutorService scheduler;
    private double progress = 0.0;
    private Stage stage; // Store the stage

    public void start(Stage stage) {
        this.stage = stage; // Save the stage reference

        BorderPane root = new BorderPane();

        // Load background image
        Image backgroundImage = new Image(getClass().getClassLoader().getResource("assets/castle_loadingbg.png").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(600);
        backgroundView.setFitHeight(400);
        backgroundView.setPreserveRatio(false); // Fill the entire scene

        // Create game title with shadow effect
        Text titleText = new Text("KU TOWER DEFENSE");
        titleText.setFont(Font.font("System", FontWeight.BOLD, 44));
        titleText.setFill(Color.GOLD);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.BLACK);
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        titleText.setEffect(dropShadow);

        // Select a random loading message
        Random random = new Random();
        String message = loadingMessages[random.nextInt(loadingMessages.length)];

        // Create a label with the selected message
        messageLabel = new Label(message);
        messageLabel.setFont(Font.font("System", 20));
        messageLabel.setTextFill(Color.WHITE);
        messageLabel.setEffect(new DropShadow(5, Color.BLACK));

        // Create a stylized progress bar
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(300);
        progressBar.setStyle(
            "-fx-accent:rgb(0, 218, 142);" +
            "-fx-control-inner-background: #3c3b3f;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-border-color:rgb(34, 220, 155);" +
            "-fx-border-width: 1px;"
        );

        // VBox to hold the components
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(titleText, messageLabel, progressBar);
        vbox.setPadding(new Insets(20));

        // Animated enemies
        Pane enemiesPane = createAnimatedEnemies();

        // Stack all layers
        StackPane mainPane = new StackPane();
        mainPane.getChildren().addAll(backgroundView, enemiesPane, vbox);

        root.setCenter(mainPane);

        // Set the scene
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Tower Defense - Loading...");
        stage.setScene(scene);
        stage.show();

        // Simulate loading with a progress bar
        simulateLoading();
    }

    private Pane createAnimatedEnemies() {
        Pane enemiesPane = new Pane();
        enemiesPane.setPrefSize(600, 400);

        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            Circle enemy = new Circle(5 + random.nextInt(8));
            enemy.setFill(Color.rgb(255, 80, 80, 0.9));
            enemy.setCenterX(-20);
            enemy.setCenterY(50 + random.nextInt(300));
            enemy.setStroke(Color.BLACK);
            enemiesPane.getChildren().add(enemy);

            Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                    new KeyValue(enemy.centerXProperty(), -20)),
                new KeyFrame(Duration.seconds(5 + random.nextInt(10)),
                    new KeyValue(enemy.centerXProperty(), 620))
            );
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }

        return enemiesPane;
    }

    private void simulateLoading() {
        scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            if (progress >= 1.0) {
                scheduler.shutdown();
                Platform.runLater(this::loadingComplete);
                return;
            }

            progress += 0.01 + (Math.random() * 0.01);
            if (progress > 1.0) progress = 1.0;

            Platform.runLater(() -> {
                progressBar.setProgress(progress);

                if ((progress > 0.3 && progress < 0.32) ||
                    (progress > 0.6 && progress < 0.62) ||
                    (progress > 0.8 && progress < 0.82)) {
                    Random random = new Random();
                    messageLabel.setText(loadingMessages[random.nextInt(loadingMessages.length)]);

                    Timeline timeline = new Timeline(
                        new KeyFrame(Duration.ZERO,
                            new KeyValue(messageLabel.scaleXProperty(), 1.0),
                            new KeyValue(messageLabel.scaleYProperty(), 1.0)),
                        new KeyFrame(Duration.millis(150),
                            new KeyValue(messageLabel.scaleXProperty(), 1.2),
                            new KeyValue(messageLabel.scaleYProperty(), 1.2)),
                        new KeyFrame(Duration.millis(300),
                            new KeyValue(messageLabel.scaleXProperty(), 1.0),
                            new KeyValue(messageLabel.scaleYProperty(), 1.0))
                    );
                    timeline.play();
                }
            });
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    private void loadingComplete() {
        Rectangle flashOverlay = new Rectangle(0, 0, 600, 400);
        flashOverlay.setFill(Color.WHITE);
        flashOverlay.setOpacity(0);

        Scene scene = progressBar.getScene();
        ((StackPane) ((BorderPane) scene.getRoot()).getCenter()).getChildren().add(flashOverlay);

        FadeTransition flash = new FadeTransition(Duration.millis(200), flashOverlay);
        flash.setFromValue(0);
        flash.setToValue(0.8);
        flash.setCycleCount(2);
        flash.setAutoReverse(true);

        flash.setOnFinished(event -> {
            // After flashing, go to MapView
            MainMenuView mainMenuView = new MainMenuView();
            mainMenuView.start(stage);
            ((StackPane) ((BorderPane) scene.getRoot()).getCenter()).getChildren().remove(flashOverlay);
        });

        flash.play();

        messageLabel.setText("Welcome to Tower Defense!");
    }

    public void shutdown() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}