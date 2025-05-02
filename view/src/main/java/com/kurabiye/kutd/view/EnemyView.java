package com.kurabiye.kutd.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import com.kurabiye.kutd.model.Coordinates.*;
import java.util.ArrayList;

import com.kurabiye.kutd.model.Enemy.Enemy;
import com.kurabiye.kutd.model.Player.UserPreference;

/**
 * EnemyView class for rendering enemies on the game canvas.
 * This class is responsible for visualizing enemies and their animations.
 */
public class EnemyView {
    private final int TILE_SIZE;
    
    // Different enemy images for different enemy types
    private Image[] enemyImages;
    
    public EnemyView(int screenWidth, int screenHeight) {
        this.TILE_SIZE = screenWidth / 16; // Dynamically calculate tile size based on screen width
        loadEnemyImages();
    }
    
    /**
     * Load enemy sprite images from resources
     */
    private void loadEnemyImages() {
        // We need images for each enemy type from the enum
        enemyImages = new Image[Enemy.EnemyType.values().length];
        
        for (Enemy.EnemyType type : Enemy.EnemyType.values()) {
            String path = "/assets/enemies/" + type.name().toLowerCase() + ".png";
            // Safely load image or use a placeholder if file not found
            try {
                enemyImages[type.getValue()] = new Image(getClass().getResourceAsStream(path));
            } catch (Exception e) {
                System.err.println("Could not load enemy image: " + path);
                // Create a fallback colored circle image
                enemyImages[type.getValue()] = createFallbackImage(type);
            }
        }
    }
    
    /**
     * Create a fallback image if the enemy sprite cannot be loaded
     */
    private Image createFallbackImage(Enemy.EnemyType type) {
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
    public void renderEnemies(GraphicsContext gc, ArrayList<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            renderEnemy(gc, enemy);
        }
    }
    
    /**
     * Render a single enemy on the canvas
     * @param gc GraphicsContext to draw on
     * @param enemy Enemy to render
     */
    private void renderEnemy(GraphicsContext gc, Enemy enemy) {
        // Skip rendering dead enemies or those that have arrived at destination
        if (enemy.isDead() || enemy.hasArrived()) {
            return;
        }
        
        // Get the enemy's current position
        Point2D position = enemy.getCoordinate();
        
        // Determine which image to use based on enemy type
        Enemy.EnemyType enemyType = enemy.getEnemyType();
        int imageIndex = enemyType.getValue();
        
        // If the image is loaded successfully
        if (enemyImages[imageIndex] != null) {

            gc.setFill(Color.RED);
            gc.fillRect(position.getX(), position.getY(), TILE_SIZE, TILE_SIZE);
            
            gc.drawImage(enemyImages[imageIndex], position.getX(), position.getY(), TILE_SIZE, TILE_SIZE);
            
            // Draw health bar above the enemy
            renderHealthBar(gc, enemy, position.getX(), position.getY());
        }
    }
    
    /**
     * Render health bar above the enemy
     * @param gc GraphicsContext to draw on
     * @param enemy Enemy whose health to display
     * @param x X coordinate of the enemy
     * @param y Y coordinate of the enemy
     */
    private void renderHealthBar(GraphicsContext gc, Enemy enemy, double x, double y) {
        // We need to find maximum health for the enemy type from user preferences
        // For now, let's use the current health as relative value
        float currentHealth = enemy.getHealth();
        
        // Get the initial health for this enemy type from user preferences
        UserPreference prefs = UserPreference.getInstance();
        int maxHealth = prefs.getEnemyHealth()[enemy.getEnemyType().getValue()];
        
        // Calculate health percentage
        double healthPercentage = currentHealth / maxHealth;
        
        // Bar dimensions
        double barWidth = TILE_SIZE;
        double barHeight = 5;
        double barY = y - 10; // Position above the enemy
        
        // Draw background (empty health)
        gc.setFill(javafx.scene.paint.Color.RED);
        gc.fillRect(x, barY, barWidth, barHeight);
        
        // Draw filled health
        gc.setFill(javafx.scene.paint.Color.GREEN);
        gc.fillRect(x, barY, barWidth * healthPercentage, barHeight);
    }
}