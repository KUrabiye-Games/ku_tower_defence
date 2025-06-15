package com.kurabiye.kutd.view.Animation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.SnapshotParameters;

public class Sprite {

    private GraphicsContext gc;

    private Image[] frames;

    private double currentTime;
    private double desiredAnimationLength;
    private double desiredTotalLength;

    private int positionX;
    private int positionY;
    private int width;
    private int height;

    public Sprite(GraphicsContext gc, Image image, double desiredAnimationLength, double desiredTotalLength, int positionX, int positionY, int width, int height) {
        this.gc = gc;
        this.desiredAnimationLength = desiredAnimationLength;
        this.desiredTotalLength = desiredTotalLength;
        this.positionX = positionX;
        this.positionY = positionY;
        this.width = width;
        this.height = height;
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
    }

}
