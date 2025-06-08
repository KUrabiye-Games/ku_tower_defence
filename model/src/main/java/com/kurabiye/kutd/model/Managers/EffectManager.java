package com.kurabiye.kutd.model.Managers;

import com.kurabiye.kutd.model.Enemy.EnemyType;
import com.kurabiye.kutd.model.Enemy.IEnemy;
import com.kurabiye.kutd.model.Enemy.Decorators.SynergeticMoveDecorator;
import com.kurabiye.kutd.util.DynamicList.DynamicArrayList;

public class EffectManager {

    private static final int TILE_SIZE = 120; // Size of a tile in pixels


    private DynamicArrayList<IEnemy> enemies; // List of enemies to apply effects on

    /**
     * Constructor for the EffectManager class.
     * Initializes the list of enemies.
     *
     * @param enemies List of enemies in the game.
     */
    public EffectManager(DynamicArrayList<IEnemy> enemies) {
        this.enemies = enemies; // Set the list of enemies
    }

    /**
     * Applies effects to the enemies based on their states.
     * This method iterates through the list of enemies and applies effects based on their states.
     */

    public void applyEffects(double deltaTime) {
        applySynergeticMovement(deltaTime);
        applySlowDown(deltaTime);
        // Add more effects as needed
    }

    class SynergeticPair{

        SynergeticMoveDecorator enemy1; // this is suppposed to be the knight but they can be swapped
        IEnemy enemy2; // this is supposed to be the goblin but they can be swapped

        SynergeticPair(IEnemy enemy1, IEnemy enemy2) {
            // check the types of the enemies
            if (enemy1.getEnemyType() == EnemyType.KNIGHT && enemy2.getEnemyType() == EnemyType.GOBLIN) {
                this.enemy1 = new SynergeticMoveDecorator(enemy1);
                this.enemy2 = enemy2;
            }else {
                throw new IllegalArgumentException("Invalid enemy types for synergetic pair");
            }
        }

        public SynergeticMoveDecorator getSynergeticKnight() {
            return enemy1;
        }

        public IEnemy getGoblin() {
            return enemy2;
        }

        public double getDistance() {
            return enemy1.getCoordinate().distance(enemy2.getCoordinate());
        }
    }


    private DynamicArrayList<SynergeticPair> synergeticPairs = new DynamicArrayList<>();


    private void applySynergeticMovement(double deltaTime) {


        // Check for synergetic movement behavior

            for (IEnemy knight : enemies) {
                if (knight.getEnemyType() == EnemyType.KNIGHT) { // Check if the enemy is a knight
                    for (IEnemy goblin : enemies) {
                        if (goblin.getEnemyType() == EnemyType.GOBLIN) { // Check if the enemy is a knight
                            double distance = knight.getCoordinate().distance(goblin.getCoordinate()); // Calculate distance between knight and goblin
                            if (distance < TILE_SIZE) { // If the distance is less than 1 tile
                                // Check if the goblin isn't already decorated with synergetic movement
                                if (!(knight instanceof SynergeticMoveDecorator)) {
                                    // Wrap the knight with the synergetic movement decorator
                                    // Implementation would go here

                                    enemies.removeLater(knight); // Remove the goblin from the enemies list
                                    // Create a new synergetic enemy with the knight's properties

                                    SynergeticPair synergeticPair = new SynergeticPair(knight, goblin); // Create a new synergetic pair
                                    synergeticPairs.addLater(synergeticPair); // Add the synergetic pair to the list

                                    enemies.addLater(synergeticPair.getSynergeticKnight());
                                }
                            }
                        }
                    }
                }
            }

        // Check if there are any synergetic pairs that lost their synergetic partner


        for (SynergeticPair pair : synergeticPairs) {
            if (pair.getDistance() > TILE_SIZE) { // If the distance between the knight and goblin is greater than 1 tile
                enemies.removeLater(pair.getSynergeticKnight()); // Remove the synergetic knight from the enemies list
                synergeticPairs.removeLater(pair); // Remove the synergetic pair from the list
                // Add the normal knight back to the enemies list
                enemies.addLater(pair.getSynergeticKnight().removeDecoration()); // Remove the synergetic movement decorator and add the normal knight back
            }
        }

        // Commit the changes to the enemies list
        enemies.commitAll(); // Commit the changes to the enemies list
        synergeticPairs.commitAll(); // Commit the changes to the synergetic pairs list

    }

    private void applySlowDown(double deltaTime) {
        // Check for slowdown behavior

       

    }

}
