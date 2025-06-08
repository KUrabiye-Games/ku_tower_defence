package com.kurabiye.kutd.model.Enemy.Decorators;
import com.kurabiye.kutd.model.Enemy.IEnemy;


/**
 * SynergeticMoveDecorator is a decorator for the IEnemy interface that adds synergetic movement behavior.
 * It extends the EnemyDecorator class to provide additional functionality to the enemy's movement.
 * 
 * @author Atlas Berk Polat
 * @version 1.0
 * @since 2025-05-28
 */

public class SynergeticMoveDecorator extends EnemyDecorator {

    // This class is a decorator for the IEnemy interface
    // It adds synergetic movement behavior to the enemy

    private int goblinSpeed; // Speed of the goblin
    private int knightSpeed; // Speed of the knight

    public SynergeticMoveDecorator(IEnemy enemy, int goblinSpeed, int knightSpeed) {
        super(enemy); // Initialize the decorator with an IEnemy instance
        this.goblinSpeed = goblinSpeed; // Set the goblin speed
        this.knightSpeed = knightSpeed; // Set the knight speed
    }

    @Override
    public void move(double deltaTime) {
        // Implement synergetic movement logic here
        // For example, modify the enemy's move direction based on some conditions
        super.move(deltaTime); // Call the original move method from the decorated enemy
    }

    public void applySynergeticMovement() {
        // Implement the synergetic movement logic here
        // This could involve modifying the enemy's path or speed based on certain conditions
        // For example, if the enemy is a goblin, it might move faster when near a knight
        enemy.setSpeed((goblinSpeed + knightSpeed) / 2); // Example of combining speeds);
    }

    @Override
    public IEnemy removeDecoration(){
        // This method can be used to remove the decoration and return the original enemy

        enemy.setSpeed(knightSpeed);

        return enemy; // Return the original enemy without the synergetic movement behavior
    }

}
