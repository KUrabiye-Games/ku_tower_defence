package com.kurabiye.kutd.model.Managers;


import com.kurabiye.kutd.model.Enemy.IEnemy;

import com.kurabiye.kutd.model.Managers.EffectManagers.SlowDownManager;
import com.kurabiye.kutd.model.Managers.EffectManagers.SynergeticMovementManager;
import com.kurabiye.kutd.util.DynamicList.DynamicArrayList;

public class MainEffectManager {




    //private DynamicArrayList<IEnemy> enemies; // List of enemies to apply effects on

    private SynergeticMovementManager synergeticMovementManager; // Manager for synergetic movement effects
    private SlowDownManager slowDownManager; // Manager for slow down effects

    /**
     * Constructor for the EffectManager class.
     * Initializes the list of enemies.
     *
     * @param enemies List of enemies in the game.
     */
    public MainEffectManager(DynamicArrayList<IEnemy> enemies) {
        
        this.synergeticMovementManager = new SynergeticMovementManager(enemies); // Initialize the synergetic movement manager
        this.slowDownManager = new SlowDownManager(enemies); // Initialize the slow down manager
    }
    /**
     * Applies effects to the enemies based on their states.
     * This method iterates through the list of enemies and applies effects based on their states.
     */

    public void applyEffects(double deltaTime) {
        synergeticMovementManager.applySynergeticMovement(deltaTime); // Apply synergetic movement effects
        slowDownManager.applySlowDown(deltaTime);
        // Add more effects as needed
    }

    public SlowDownManager getSlowDownManager() {
        return slowDownManager; // Get the slow down manager
    }

    


}
