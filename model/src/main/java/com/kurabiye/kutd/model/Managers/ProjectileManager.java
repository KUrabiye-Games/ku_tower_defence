package com.kurabiye.kutd.model.Managers;

import java.util.List;

import com.kurabiye.kutd.util.DynamicList.DynamicArrayList;
import com.kurabiye.kutd.model.Projectile.IProjectile;

public class ProjectileManager {

    DynamicArrayList<IProjectile> projectiles = new DynamicArrayList<>(); // List to hold all projectiles




    /**
     * Adds a projectile to the list of projectiles.
     *
     * @param projectile The projectile to be added.
     */
    public void addProjectile(IProjectile projectile) {
        if (projectile != null) {
            projectiles.add(projectile);
        }
    }


    /**
     * Move the projectiles based on the elapsed time.
     * @param deltaTime The time elapsed since the last update.
     * 
     */
    public void moveProjectiles(double deltaTime) {
        for (IProjectile projectile : projectiles) {
            projectile.move(deltaTime); // Update the projectile's position based on the delta time
        }
    }



    public synchronized List<IProjectile> getProjectiles() {
        return projectiles;
    }
    

    public DynamicArrayList<IProjectile> getDynamicProjectiles() {
        return projectiles; // Return the list of projectiles
    }

    

}
