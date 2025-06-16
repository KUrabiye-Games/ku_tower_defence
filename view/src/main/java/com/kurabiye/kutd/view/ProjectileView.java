package com.kurabiye.kutd.view;

import com.kurabiye.kutd.model.Coordinates.Point2D;
import com.kurabiye.kutd.model.Projectile.IProjectile;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.List;

public class ProjectileView {
    private final Image[] projectileImages;
    private final int TILE_SIZE;
    private final int COLS;
    private final double MODEL_WIDTH = 1920.0;

    public ProjectileView(Image[] projectileImages, int tileSize, int cols) {
        this.projectileImages = projectileImages;
        this.TILE_SIZE = tileSize;
        this.COLS = cols;
    }

    public void renderProjectiles(GraphicsContext gc, List<IProjectile> projectiles) {
        double scaleFactor = TILE_SIZE * COLS / MODEL_WIDTH;

        for (IProjectile projectile : projectiles) {
            Point2D position = projectile.getCoordinate();
            double viewX = position.getX() * scaleFactor;
            double viewY = position.getY() * scaleFactor;

            Image projectileImage = null;
            double imageSize = 20;
            boolean shouldRotate = false;

            switch (projectile.getProjectileType()) {
                case ARROW:
                    projectileImage = projectileImages[0];
                    imageSize = 30;
                    shouldRotate = true;
                    break;
                case MAGIC:
                    projectileImage = projectileImages[1];
                    imageSize = 35;
                    break;
                case ARTILLERY:
                    projectileImage = projectileImages[2];
                    imageSize = 15;
                    break;
            }

            if (projectileImage != null) {
                if (shouldRotate) {
                    double angle = Math.toDegrees(Math.atan2(
                        projectile.getSpeedVector().getY(),
                        projectile.getSpeedVector().getX()
                    ));
                    gc.save();
                    gc.translate(viewX, viewY);
                    gc.rotate(angle);
                    gc.drawImage(projectileImage, -imageSize / 2, -imageSize / 2, imageSize, imageSize);
                    gc.restore();
                } else {
                    gc.drawImage(projectileImage, viewX - imageSize / 2, viewY - imageSize / 2, imageSize, imageSize);
                }
            }
        }
    }
}