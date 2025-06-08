package com.kurabiye.kutd.model.Managers;

import com.kurabiye.kutd.model.Collectable.GoldBag;
import com.kurabiye.kutd.model.Collectable.ICollectable;
import com.kurabiye.kutd.model.Coordinates.Point2D;
import com.kurabiye.kutd.model.Player.Player;
import com.kurabiye.kutd.model.Player.UserPreference;
import com.kurabiye.kutd.util.DynamicList.DynamicArrayList;

public class CollactableManager {

    DynamicArrayList<ICollectable<?>> collectables = new DynamicArrayList<>(); // List to hold collectable items

    private final double CLICK_RADIUS = 30.0; // Pixels - how close the click needs to be

    private Player player; // Reference to the player, if needed for player-specific logic


    //
    private static final int ARCHER_COST = (int)(UserPreference.getInstance().getTowerConstructionCost()[2][0] / 2); // Default amount of gold in a GoldBag, can be adjusted

    /**
     * Constructor for the CollactableManager class.
     * 
     * @param player The player instance associated with this manager.
     */
    public CollactableManager(Player player) {
        // This constructor can be expanded to include player-specific initialization if needed
        this.player = player; // Set the player reference
    }

    /**
     * Updates all collectables, removing expired ones
     * 
     * @effects collectables are updated based on the time elapsed since the last update.
     * @param deltaTime The time elapsed since the last update, used for updating collectables
     * 
     */
    public void updateCollectables(double deltaTime) {
        for (ICollectable<?> collectable : collectables) {
            collectable.update(deltaTime);
            
            // Remove expired collectables
            if (collectable instanceof GoldBag && ((GoldBag) collectable).isExpired()) {
                collectables.removeLater(collectable);
            }
        }
        collectables.removeCommit();
    }

    /**
     * Handles clicking on collectables
     * @param clickPosition The position where the user clicked
     * @return true if a collectable was collected, false otherwise
     */
    public boolean handleClick(Point2D clickPosition) {
        
        
        for (ICollectable<?> collectable : collectables) {
            double distance = collectable.getCoordinates().distance(clickPosition);
            
            if (distance <= CLICK_RADIUS && collectable instanceof GoldBag) {
                // check if the type is GoldBag
                if (collectable instanceof GoldBag) {
                    GoldBag goldBag = (GoldBag) collectable;
                
                    // Give gold to player
                    player.earnGold(goldBag.getItem());
                
                    // Remove the collected item
                    collectables.removeLater(collectable);
                    collectables.removeCommit();
                
                    return true; // Successfully collected
                }
            }
        }
        return false; // Nothing was collected
    }



    /**
     * Spawns a gold bag when an enemy dies
     * @param position The position where the gold bag should be spawned
     * @param goldAmount The amount of gold in the bag
     */
    public void spawnGoldBag(Point2D position) {
        int goldAmount = (int)((Math.random() * (ARCHER_COST - 2)) + 2); // Default amount of gold, can be adjusted as needed
        GoldBag goldBag = new GoldBag(position, goldAmount);
        collectables.add(goldBag);
    }



    /**
     * Gets the list of collectables
     * 
     * @return The list of collectables
     */
    public DynamicArrayList<ICollectable<?>> getCollectables() {
        return collectables;
    }


}
