package com.kurabiye.kutd.model.Managers.EffectManagers;

import com.kurabiye.kutd.model.Enemy.EnemyType;
import com.kurabiye.kutd.model.Enemy.IEnemy;
import com.kurabiye.kutd.model.Enemy.Decorators.EnemyDecorator;
import com.kurabiye.kutd.model.Enemy.Decorators.SynergeticMoveDecorator;
import com.kurabiye.kutd.util.DynamicList.DynamicArrayList;

public class SynergeticMovementManager {

     private static final int TILE_SIZE = 120; // Size of a tile in pixels



    private DynamicArrayList<IEnemy> enemies;

    /**
     * Constructor for the SynergeticMovementManager class.
     * Initializes the list of enemies.
     *
     * @param enemies List of enemies in the game.
     */
    public SynergeticMovementManager(DynamicArrayList<IEnemy> enemies) {
        this.enemies = enemies; // Set the list of enemies
    }

    public void applySynergeticMovement(double deltaTime) {


        // Check for synergetic movement behavior

            for (IEnemy knight : enemies) {
                if (knight.getEnemyType() == EnemyType.KNIGHT) { // Check if the enemy is a knight


                    IEnemy foundGoblin = null; // Variable to store the found goblin

                    for (IEnemy goblin : enemies) {
                        if (goblin.getEnemyType() == EnemyType.GOBLIN) { // Check if the enemy is a knight
                            double distance = knight.getCoordinate().distance(goblin.getCoordinate()); // Calculate distance between knight and goblin
                            if (distance < TILE_SIZE) { // If the distance is less than 1 tile
                                // Check if the goblin isn't already decorated with synergetic movement

                                foundGoblin = goblin; // Set the flag to true if a goblin is found
                                break;                                
                            }
                        }
                    }


                    // Special thanks to Utku for the synergetic movement idea 


                    if (foundGoblin != null) { // If a goblin is found within 1 tile of the knight
                                   if (!(knight instanceof EnemyDecorator)) {
                                        // No previous effect, decorate the knight with synergetic movement
                                        EnemyDecorator decoratedKnight = new EnemyDecorator(knight);
                                        enemies.addLater(decoratedKnight); // Add the decorated knight to the list of enemies
                                        enemies.removeLater(knight); // Remove the original knight from the list of enemies
                                    } else {
                                        // Add a new synergetic move decorator to the knight
                                        ((EnemyDecorator) knight).addEffect(new SynergeticMoveDecorator(knight, foundGoblin));
                                    }
                                    
                    }
                    }
                }
            

        // Commit
        //enemies.removeCommit();
        //enemies.addCommit();

    }



    

}
