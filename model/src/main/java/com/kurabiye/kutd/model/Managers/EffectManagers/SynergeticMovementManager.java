package com.kurabiye.kutd.model.Managers.EffectManagers;

import com.kurabiye.kutd.model.Enemy.EnemyType;
import com.kurabiye.kutd.model.Enemy.IEnemy;
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

                    boolean foundGoblin = false; // Flag to check if a goblin is found

                    for (IEnemy goblin : enemies) {
                        if (goblin.getEnemyType() == EnemyType.GOBLIN) { // Check if the enemy is a knight
                            double distance = knight.getCoordinate().distance(goblin.getCoordinate()); // Calculate distance between knight and goblin
                            if (distance < TILE_SIZE) { // If the distance is less than 1 tile
                                // Check if the goblin isn't already decorated with synergetic movement

                                foundGoblin = true; // Set the flag to true if a goblin is found
                                break;                                
                            }
                        }
                    }


                    // Special thanks to Utku for the synergetic movement idea 


                    if (foundGoblin && !(knight instanceof SynergeticMoveDecorator)) {
                                    // check if the knight is in a pair with a goblin


                                    

                            SynergeticMoveDecorator synergeticKnight = new SynergeticMoveDecorator(knight); // Create a new synergetic movement decorator for the knight

                            enemies.addLater(synergeticKnight); // Add the synergetic knight to the enemies list
                            enemies.removeLater(knight);

                            // Debugging message
                            //System.out.println("Synergetic movement applied to knight at " + knight.getCoordinate() + " with goblin at ");
                                        

                    }else if (!foundGoblin && knight instanceof SynergeticMoveDecorator) {

                        IEnemy originalKnight = ((SynergeticMoveDecorator) knight).removeDecoration(); // Remove the synergetic movement decorator and get the original knight
                        enemies.removeLater(knight); // Remove the synergetic knight from the enemies list
                        enemies.addLater(originalKnight); // Add the original knight back to the enemies list


                        // Debugging message
                        //System.out.println("Synergetic movement removed from knight at " + knight.getCoordinate() + " as no goblin is found.");

                    }
                }
            }

        // Commit
        enemies.removeCommit();
        enemies.addCommit();

    }



    

}
