package com.kurabiye.kutd.view.Animation;

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AnimationManager {
    private static AnimationManager instance;
    private final List<AnimationInstance> animations = new ArrayList<>();

    private AnimationManager() {}

    public static AnimationManager getInstance() {
        if (instance == null) {
            instance = new AnimationManager();
        }
        return instance;
    }

    public void createAnimation(Sprite sprite, double duration, double x, double y) {
        animations.add(new AnimationInstance(sprite, duration, x, y));
    }

    public void update(double deltaTime) {
        Iterator<AnimationInstance> iter = animations.iterator();
        while (iter.hasNext()) {
            AnimationInstance anim = iter.next();
            anim.update(deltaTime);
            if (anim.isFinished()) {
                iter.remove();
            }
        }
    }

    public void render(GraphicsContext gc) {
        for (AnimationInstance anim : animations) {
            anim.render(gc);
        }
    }

    private static class AnimationInstance {
        private final Sprite sprite;
        private final double duration;
        private double elapsedTime = 0;
        private final double x, y;

        AnimationInstance(Sprite sprite, double duration, double x, double y) {
            this.sprite = sprite;
            this.duration = duration;
            this.x = x;
            this.y = y;
        }

        void update(double dt) {
            elapsedTime += dt;
        }

        void render(GraphicsContext gc) {
            if (!isFinished()) {
                sprite.update(elapsedTime, (int) x, (int) y);
            }
        }

        boolean isFinished() {
            return elapsedTime >= duration;
        }
    }
}
