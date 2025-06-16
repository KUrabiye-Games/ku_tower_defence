package com.kurabiye.kutd.view.Animation;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.kurabiye.kutd.model.Coordinates.Point2D;

public class AnimationManager {

    private final List<AnimationInstance> animations = new ArrayList<>();
    private int nextId = 0;

    /**
     * Eski kullanım için: ID döndürmez
     */
    public void createAnimation(GraphicsContext gc, Image spriteSheet, Point2D position,
                                double frameDuration, double totalDuration, int width, int height) {
        createAnimationReturningId(gc, spriteSheet, position, frameDuration, totalDuration, width, height);
    }

    /**
     * Yeni kullanım: animation ID döner (iptal etmek için)
     */
    public int createAnimationReturningId(GraphicsContext gc, Image spriteSheet, Point2D position,
                                          double frameDuration, double totalDuration, int width, int height) {
        Sprite sprite = new Sprite(gc, spriteSheet, frameDuration, totalDuration,
                                   (int) position.getX(), (int) position.getY(), width, height);
        AnimationInstance instance = new AnimationInstance(nextId++, sprite, totalDuration);
        animations.add(instance);
        return instance.id;
    }

    /**
     * Animasyonu manuel olarak bitirir
     */
    public void stopAnimation(int animationId) {
        animations.removeIf(anim -> anim.id == animationId);
    }

    public void update(double deltaTime) {
        Iterator<AnimationInstance> iter = animations.iterator();
        while (iter.hasNext()) {
            AnimationInstance anim = iter.next();
            anim.sprite.update(deltaTime, anim.sprite.getX(), anim.sprite.getY());
            anim.remainingTime -= deltaTime;
            if (anim.remainingTime <= 0) {
                iter.remove();
            }
        }
    }

    private static class AnimationInstance {
        int id;
        Sprite sprite;
        double remainingTime;

        AnimationInstance(int id, Sprite sprite, double duration) {
            this.id = id;
            this.sprite = sprite;
            this.remainingTime = duration;
        }
    }
}
