package com.kurabiye.kutd.view.Animation;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/* Sprite.java
 * This class is responsible for handling the sprite animations in the game. 
 * It takes an image and splits it into frames for animation.
 * The class provides methods to update the current frame and render the sprite on the screen.
 * 
 * 
 * @author Atlas Berk Polat
 * @version 1.0
 * @since 2025-05-04
 * 
 */

public class Sprite {


    GraphicsContext gc; // Graphics context for rendering the sprite



    private Image[] frames; // Array of frames for the sprite animation


    private double currentTime; // Time duration for each frame

    private double desiredAnimationLength; // Desired length of the animation in frames

    private double desiredTotalLength; // Total length of the animation in frames

    private int positionX; // X position of the sprite on the screen
    private int positionY; // Y position of the sprite on the screen
    private int width; // Width of the sprite
    private int height; // Height of the sprite



    public Sprite(GraphicsContext gc, Image image, double desiredAnimationLength, double desiredTotalLength, int positionX, int positionY, int width, int height) {
        this.gc = gc; // Initialize the graphics context
       
        this.desiredAnimationLength = desiredAnimationLength; // Calculate the desired animation length

        this.desiredTotalLength = desiredTotalLength; // Calculate the total length of the animation
        this.positionX = positionX; // Set the X position of the sprite
        this.positionY = positionY; // Set the Y position of the sprite
        this.width = width; // Set the width of the sprite
        this.height = height; // Set the height of the sprite
        currentTime = 0; // Initialize the current time to 0

        // Split the image into frames

        // divide the length of the image to the height of the image to get the number of frames

        int frameCount = (int) (image.getWidth() / image.getHeight()); // Calculate the number of frames
        frames = new Image[frameCount]; // Initialize the frames array

        for (int i = 0; i < frameCount; i++) {
            frames[i] = new Image(image.getUrl(), image.getWidth() / frameCount, image.getHeight(), false, false); // Create a new image for each frame
        }

        // render the first frame

        this.update(0, this.positionX, this.positionY); // Render the first frame on the screen
    }



    public void update(double deltaTime, int coordinateX, int coordinateY) {
        currentTime += deltaTime; // Update the current time

        if (currentTime >= desiredTotalLength) { // If the total animation is complete
            return;
            
        }

        int repetation = (int) (currentTime / desiredAnimationLength); // Calculate the number of repetitions

        double remainingTime = (currentTime - repetation *  desiredAnimationLength); // Calculate the remaining time

        int currentFrame = (int) (((remainingTime % desiredAnimationLength) / desiredAnimationLength) * frames.length) ; // Calculate the current frame


        // Render the current frame

        gc.drawImage(frames[currentFrame], coordinateX - width / 2, coordinateY - height / 2, width, height); // Draw the current frame on the canvas

        
    }


}
