package com.kurabiye.kutd.view.Animation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import javafx.scene.image.WritableImage;
import javafx.scene.SnapshotParameters;

import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

/* Sprite.java
 * This class is responsible for handling the sprite animations in the game. 
 * It takes an image and splits it into frames for animation.
 * The class provides methods to update the current frame and render the sprite on the screen.
 * 
 * 
 * @author Atlas Berk Polat, Pınar Dai
 * @version 2.0
 * @since 2025-06-06
 * 
 */


public class Sprite {

    private GraphicsContext gc;

    private Image[] frames;

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

    private double currentTime;
    private double desiredAnimationLength;
    private double desiredTotalLength;

    private int positionX;
    private int positionY;
    private int width;
    private int height;

///////
    public Sprite(GraphicsContext gc, Image image, double desiredAnimationLength, double desiredTotalLength, int positionX, int positionY, int width, int height) {
        this.gc = gc;
        this.desiredAnimationLength = desiredAnimationLength;
        this.desiredTotalLength = desiredTotalLength;
////////
    public Sprite(GraphicsContext gc, Image image, double frameDuration, double totalDuration, int positionX, int positionY, int width, int height) {
        this.gc = gc;
        this.frameDuration = frameDuration;    
        this.totalDuration = totalDuration;   
///////
        this.positionX = positionX;
        this.positionY = positionY;
        this.width = width;
        this.height = height;
///////
        this.currentTime = 0;

        // Frame sayısını hesapla
        int frameCount = (int) (image.getWidth() / image.getHeight());
        int frameWidth = (int) (image.getWidth() / frameCount);
        int frameHeight = (int) image.getHeight();

        frames = new Image[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = new WritableImage(image.getPixelReader(), i * frameWidth, 0, frameWidth, frameHeight);
        }

        // İlk frame’i çiz
        this.update(0, this.positionX, this.positionY);
    }

    public void update(double deltaTime, int coordinateX, int coordinateY) {
        currentTime += deltaTime;

        if (currentTime >= desiredTotalLength) {
            return;
        }

        int repetition = (int) (currentTime / desiredAnimationLength);
        double remainingTime = (currentTime - repetition * desiredAnimationLength);

        int currentFrame = (int) (((remainingTime % desiredAnimationLength) / desiredAnimationLength) * frames.length);
        if (currentFrame >= frames.length) currentFrame = frames.length - 1;

        gc.drawImage(frames[currentFrame], coordinateX - width / 2, coordinateY - height / 2, width, height);
///////

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
        
//////// animation_merge
    }

}
