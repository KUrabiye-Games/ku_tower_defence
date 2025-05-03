package com.kurabiye.kutd.view;

import com.kurabiye.kutd.model.Tower.Tower;
import com.kurabiye.kutd.model.Projectile.Projectile.ProjectileType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class TowerView {
    private static final int TOWER_IMAGE_COUNT = 3;
    private final Image[] towerImages = new Image[TOWER_IMAGE_COUNT];
    private final int tileSize;
    
    public TowerView(int tileSize) {
        this.tileSize = tileSize;
        loadTowerImages();
    }
    
    private void loadTowerImages() {
        // Load tower images based on their projectile type
        towerImages[ProjectileType.ARROW.getValue()] = new Image(getClass().getResourceAsStream("/assets/tiles/tile26.png"));
        towerImages[ProjectileType.MAGIC.getValue()] = new Image(getClass().getResourceAsStream("/assets/tiles/tile21.png"));
        towerImages[ProjectileType.ARTILLERY.getValue()] = new Image(getClass().getResourceAsStream("/assets/tiles/tile20.png"));
    }
    
    public void renderTowers(GraphicsContext gc, ArrayList<Tower> towers) {
        if (towers == null || towers.isEmpty()) {
            return;
        }
        
        for (Tower tower : towers) {
            // Get the projectile type to determine which image to use
            ProjectileType projectileType = tower.getProjectileType();
            
            // Get tower position
            int col = tower.getTileCoordinate().getTileX();
            int row = tower.getTileCoordinate().getTileY();
            
            // Draw the tower at its position
            gc.drawImage(towerImages[projectileType.getValue()], 
                        col * tileSize, 
                        row * tileSize, 
                        tileSize, 
                        tileSize);
        }
    }
}