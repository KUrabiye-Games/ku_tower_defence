package com.kurabiye.kutd.view;

import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.kurabiye.kutd.controller.GamePlayController;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class GameStartTest {
    private GamePlayView gamePlayView;
    private GamePlayController controller;
    private Stage stage;

    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        // Initialize JavaFX toolkit for testing
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(() -> latch.countDown());
        latch.await(5, TimeUnit.SECONDS);
    }

    @BeforeEach
    void setUp() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            gamePlayView = new GamePlayView();
            controller = new GamePlayController();
            stage = new Stage();
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testStartMethodCompletes() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            assertDoesNotThrow(() -> {
                gamePlayView.start(stage, controller);
            });
            assertNotNull(gamePlayView.getController());
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testStageConfiguration() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            gamePlayView.start(stage, controller);
            assertEquals("Game Map", stage.getTitle());
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testControllerIntegration() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            gamePlayView.start(stage, controller);
            assertNotNull(gamePlayView.getController());
            assertSame(controller, gamePlayView.getController());
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }
}