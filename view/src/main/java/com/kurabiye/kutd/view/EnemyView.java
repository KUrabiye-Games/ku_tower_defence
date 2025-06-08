package com.kurabiye.kutd.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


import com.kurabiye.kutd.model.Coordinates.*;
import java.util.List;

import com.kurabiye.kutd.model.Enemy.EnemyType;
import com.kurabiye.kutd.model.Enemy.IEnemy;

import com.kurabiye.kutd.model.Player.UserPreference;

/**
 * EnemyView class for rendering enemies on the game canvas.
 * This class is responsible for visualizing enemies and their animations.
 */
public class EnemyView {
    private final int TILE_SIZE;
    private final int COLS = 16; // Number of columns in the game map
   // private final int ROWS = 9; // Number of rows in the game map
    
    // Different enemy images for different enemy types
    private Image[] enemyImages;
    
    public EnemyView(int tileSize) {
        this.TILE_SIZE = tileSize;
        loadEnemyImages();
    }
    
    /**
     * Load enemy sprite images from resources
     */
    private void loadEnemyImages() {
        // We need images for each enemy type from the enum
        enemyImages = new Image[EnemyType.values().length * 6];
        
        for (EnemyType type : EnemyType.values()) {
            for (int i = 0; i < 6; i++) {
                String imagePath = String.format("/assets/enemies/%s%d.png", type.name().toLowerCase(), i);
                try {
                    enemyImages[type.getValue() * 6 + i] = new Image(getClass().getResourceAsStream(imagePath));
                } catch (Exception e) {
                    // If the image cannot be loaded, create a fallback image
                    enemyImages[type.getValue() * 6 + i] = createFallbackImage(type);
                }
            }
        }
    }

    /**
     * Create a fallback image if the enemy sprite cannot be loaded
     */
    private Image createFallbackImage(EnemyType type) {
        // Create a simple colored circle as fallback
        javafx.scene.canvas.Canvas canvas = new javafx.scene.canvas.Canvas(TILE_SIZE, TILE_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        // Different colors for different enemy types
        switch (type) {
            case GOBLIN:
                gc.setFill(javafx.scene.paint.Color.GREEN);
                break;
            case KNIGHT:
                gc.setFill(javafx.scene.paint.Color.GRAY);
                break;
            default:
                gc.setFill(javafx.scene.paint.Color.RED);
        }
        
        // Draw a circle
        gc.fillOval(4, 4, TILE_SIZE - 8, TILE_SIZE - 8);
        
        // Convert canvas to image
        return canvas.snapshot(null, null);
    }
 
    /**
     * Render all enemies on the canvas
     * @param gc GraphicsContext to draw on
     * @param enemies List of enemies to render
     */
    public void renderEnemies(GraphicsContext gc, List<IEnemy> enemies, int enemyImage) {
        for (IEnemy enemy : enemies) {
            renderEnemy(gc, enemy, enemyImage);
        }
    }
    
    /**
     * Render a single enemy on the canvas
     * @param gc GraphicsContext to draw on
     * @param enemy Enemy to render
     */
    private void renderEnemy(GraphicsContext gc, IEnemy enemy, int enemyImage) {
        // Skip rendering dead enemies or those that have arrived at destination
        if (enemy.isDead() || enemy.hasArrived()) {
            return;
        }
        
        // Get the enemy's current position
        Point2D position = enemy.getCoordinate();
        
        // Transform model coordinates to view coordinates
        double modelWidth = 1920;  // The width used in the model
        //double modelHeight = 1080; // The height used in the model
        double scaleFactor = TILE_SIZE * COLS / modelWidth; // Calculate the scale factor
        
        // Scale positions from model space to view space
        double viewX = position.getX() * scaleFactor;
        double viewY = position.getY() * scaleFactor;
        
        // Center the enemy image on the path point by offsetting half the tile size
        double centeredX = viewX - (TILE_SIZE / 2);
        double centeredY = viewY - (TILE_SIZE / 2);
        
        // Determine which image to use based on enemy type
        EnemyType enemyType = enemy.getEnemyType();
        int imageEIndex = enemyType.getValue();
        
        // If the image is loaded successfully
        if (enemyImages[imageEIndex * 6 + enemyImage] != null) {
            // Atlas:
            // Draw the enemy image
            // According to the enemy speed vector, we can determine the direction
            // and image reflection might be needed
            // I will use dot product to determine the direction

            enemy.getMoveDirection().dotProduct(new Point2D(1, 0));
            if (enemy.getMoveDirection().dotProduct(new Point2D(1, 0)) < 0) {
                // Flip the image horizontally
                gc.scale(-1, 1);
                gc.drawImage(enemyImages[enemyType.getValue() * 6 + enemyImage], -centeredX - TILE_SIZE, centeredY, TILE_SIZE, TILE_SIZE);
                gc.scale(-1, 1); // Reset scale
            } else {
                // Draw normally
                gc.drawImage(enemyImages[enemyType.getValue() * 6 + enemyImage], centeredX, centeredY, TILE_SIZE, TILE_SIZE);
            }

           
            
            // Draw health bar above the enemy
            renderHealthBar(gc, enemy, viewX - TILE_SIZE/2, viewY - TILE_SIZE/2);
        }
    }
    
    /**
     * Render health bar above the enemy
     * @param gc GraphicsContext to draw on
     * @param enemy Enemy whose health to display
     * @param x X coordinate of the enemy
     * @param y Y coordinate of the enemy
     */
    private void renderHealthBar(GraphicsContext gc, IEnemy enemy, double x, double y) {
        // We need to find maximum health for the enemy type from user preferences
        // For now, let's use the current health as relative value
        float currentHealth = enemy.getHealth();
        
        // Get the initial health for this enemy type from user preferences
        UserPreference prefs = UserPreference.getInstance();
        int maxHealth = prefs.getEnemyHealth()[enemy.getEnemyType().getValue()];
        
        // Calculate health percentage
        double healthPercentage = currentHealth / maxHealth;
        
        x = x + TILE_SIZE / 4; // Center the health bar above the enemy

        // Bar dimensions
        double barWidth = TILE_SIZE / 2;
        double barHeight = 5;
        double barY = y + 10; // Position above the enemy
        
        // Draw background (empty health)
        gc.setFill(javafx.scene.paint.Color.RED);
        gc.fillRect(x, barY, barWidth, barHeight);
        
        // Draw filled health
        gc.setFill(javafx.scene.paint.Color.GREEN);
        gc.fillRect(x, barY, barWidth * healthPercentage, barHeight);
    }
}