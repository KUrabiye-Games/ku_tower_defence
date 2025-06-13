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
    // private GamePlayView gamePlayView;
    // private GamePlayController controller;
    // private Stage stage;

    // /**
    //  * Initializes the JavaFX toolkit before running any tests.
    //  * This is necessary to ensure that JavaFX components can be created and manipulated in the test environment.
    //  */
    // @BeforeAll
    // static void initJavaFX() throws InterruptedException {
    //     // Initialize JavaFX toolkit for testing
    //     CountDownLatch latch = new CountDownLatch(1);
    //     Platform.startup(() -> latch.countDown());
    //     latch.await(5, TimeUnit.SECONDS);
    // }

    // /**
    //  * Sets up the test environment before each test case.
    //  * This method initializes the GamePlayView and GamePlayController, and prepares a Stage for testing.
    //  * It uses a CountDownLatch to ensure that the JavaFX components are fully initialized before proceeding with the tests.
    //  */
    // @BeforeEach
    // void setUp() throws InterruptedException {
    //     CountDownLatch latch = new CountDownLatch(1);
    //     Platform.runLater(() -> {
    //         gamePlayView = new GamePlayView();
    //         controller = new GamePlayController();
    //         stage = new Stage();
    //         latch.countDown();
    //     });
    //     latch.await(5, TimeUnit.SECONDS);
    // }

    // /**
    //  * Test to ensure that the start method of GamePlayView completes without exceptions.
    //  * This test verifies that the method can be called and that it initializes the view correctly.
    //  */
    // @Test
    // void testStartMethodCompletes() throws InterruptedException {
    //     CountDownLatch latch = new CountDownLatch(1);
    //     Platform.runLater(() -> {
    //         assertDoesNotThrow(() -> {
    //             gamePlayView.start(stage, controller);
    //         });
    //         assertNotNull(gamePlayView.getController());
    //         latch.countDown();
    //     });
    //     latch.await(5, TimeUnit.SECONDS);
    // }

    // /**
    //  * Test to verify that the stage is configured correctly when the start method is called. 
    //  * This includes checking the title of the stage and ensuring that it is not null.
    //  */
    // @Test
    // void testStageConfiguration() throws InterruptedException {
    //     CountDownLatch latch = new CountDownLatch(1);
    //     Platform.runLater(() -> {
    //         gamePlayView.start(stage, controller);
    //         assertEquals("Game Map", stage.getTitle());
    //         latch.countDown();
    //     });
    //     latch.await(5, TimeUnit.SECONDS);
    // }

    // /**
    //  * Test to ensure that the controller is set correctly in the GamePlayView.
    //  * This test checks that the controller instance passed to the start method is the same as the one retrieved from the view.
    //  */
    // @Test
    // void testControllerIntegration() throws InterruptedException {
    //     CountDownLatch latch = new CountDownLatch(1);
    //     Platform.runLater(() -> {
    //         gamePlayView.start(stage, controller);
    //         assertNotNull(gamePlayView.getController());
    //         assertSame(controller, gamePlayView.getController());
    //         latch.countDown();
    //     });
    //     latch.await(5, TimeUnit.SECONDS);
    // }
}