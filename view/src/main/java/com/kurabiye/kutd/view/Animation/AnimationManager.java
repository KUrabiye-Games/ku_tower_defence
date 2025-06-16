package com.kurabiye.kutd.view.Animation;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.kurabiye.kutd.model.Coordinates.Point2D;

public class AnimationManager {

    private final List<AnimationInstance> animations = new ArrayList<>();

    public void createAnimation(GraphicsContext gc, Image spriteSheet, Point2D position, double frameDuration, double totalDuration, int width, int height) {
        Sprite sprite = new Sprite(gc, spriteSheet, frameDuration, totalDuration, (int) position.getX(), (int) position.getY(), width, height);
        animations.add(new AnimationInstance(sprite, totalDuration));
    }

    public void update(double deltaTime) {
        Iterator<AnimationInstance> iter = animations.iterator();
        while (iter.hasNext()) {
            AnimationInstance anim = iter.next();
            anim.sprite.update(deltaTime, anim.sprite.getX(), anim.sprite.getY());

            anim.remainingTime -= deltaTime;
            if (anim.remainingTime <= 0) {
                iter.remove(); // Remove animation if it's finished
            }
        }
    }

    private static class AnimationInstance {
        Sprite sprite;
        double remainingTime;

        AnimationInstance(Sprite sprite, double duration) {
            this.sprite = sprite;
            this.remainingTime = duration;
        }
    }
}
