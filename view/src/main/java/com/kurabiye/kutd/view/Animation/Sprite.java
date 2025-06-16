package com.kurabiye.kutd.view.Animation;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

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

    private double frameDuration;     // Her frame'in süresi (örneğin 0.2 saniye)
    private double totalDuration;     // Animasyonun toplam süresi (örneğin 1.8 saniye)


    private int positionX; // X position of the sprite on the screen
    private int positionY; // Y position of the sprite on the screen
    private int width; // Width of the sprite
    private int height; // Height of the sprite



    public Sprite(GraphicsContext gc, Image image, double frameDuration, double totalDuration, int positionX, int positionY, int width, int height) {
        this.gc = gc;
        this.frameDuration = frameDuration;    
        this.totalDuration = totalDuration;   
        this.positionX = positionX;
        this.positionY = positionY;
        this.width = width;
        this.height = height;

        int frameCount = (int) (image.getWidth() / image.getHeight());
        frames = new Image[frameCount];
        PixelReader reader = image.getPixelReader();
        int frameWidth = (int) image.getWidth() / frameCount;
        int frameHeight = (int) image.getHeight();

        for (int i = 0; i < frameCount; i++) {
            frames[i] = new WritableImage(reader, i * frameWidth, 0, frameWidth, frameHeight);
        }

        this.update(0, this.positionX, this.positionY);
    }


    public int getX() {
    return positionX;
}

    public int getY() {
        return positionY;
    }



    public void update(double deltaTime, int coordinateX, int coordinateY) {
        currentTime += deltaTime;

        int frameIndex;
        double animationDuration = frames.length * frameDuration;

        if (currentTime >= totalDuration) {
            // Animasyon süresi geçtiyse yok say, çizmeyebiliriz bile (ya da son frame opsiyonel)
            return;
        } else if (currentTime >= animationDuration) {
            // Animasyon bitti ama gösterim süresi devam ediyor
            frameIndex = frames.length - 1;
        } else {
            frameIndex = (int) (currentTime / frameDuration);
        }

        if (frameIndex >= frames.length) frameIndex = frames.length - 1;

        gc.drawImage(frames[frameIndex], positionX - width / 2, positionY - height / 2, width, height);
        
    }


}
