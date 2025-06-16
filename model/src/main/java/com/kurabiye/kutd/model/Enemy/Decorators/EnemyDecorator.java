package com.kurabiye.kutd.model.Enemy.Decorators;

import java.util.ArrayList;

import com.kurabiye.kutd.model.Enemy.IEnemy;
import com.kurabiye.kutd.model.Enemy.EnemyType;
import com.kurabiye.kutd.model.Enemy.MoveStrategy.IMoveStrategy;
import com.kurabiye.kutd.model.Projectile.IProjectile;
import com.kurabiye.kutd.util.DynamicList.DynamicArrayList;
import com.kurabiye.kutd.model.Coordinates.Point2D;


public class EnemyDecorator implements IEnemy {


    // This class is an abstract decorator for the IEnemy interface
    // It can be used to add additional functionality to the IEnemy interface
    // without modifying the original interface or its implementations

    protected IEnemy enemy;

    DynamicArrayList<AbstractEffect> activeEffects = new DynamicArrayList<>();

   

    public EnemyDecorator(IEnemy enemy) {
        this.enemy = enemy; // Set the enemy to be decorated
        
    }


    @Override
    public void move(double deltaTime) {
        // This method can be overridden by subclasses to implement specific movement behavior
        // The default implementation does nothing.

        // Update the remaining effect time

        for (AbstractEffect effect : activeEffects) {
            effect.update(deltaTime); // Update each active effect
            if (effect.isExpired()) {
                activeEffects.removeLater(effect);
            }
        }
        activeEffects.removeCommit(); // Commit the changes to the active effects list


        // Sort the active effects based on their priority
        activeEffects.sort((effect1, effect2) -> -1 * Integer.compare(effect1.getPriority(), effect2.getPriority()));

        int targetSpeed = this.getSpeed(); // Get the base speed of the enemy


        for (AbstractEffect effect : activeEffects) {
            if (effect instanceof ISpeedDecorator) {
                targetSpeed = ((ISpeedDecorator) effect).getSpeed(targetSpeed); // Get the speed from the effect if it is a speed decorator
            }
        }
        // Set the target speed and then call move
        int originalSpeed = enemy.getSpeed();
        enemy.setSpeed(targetSpeed);
        enemy.move(deltaTime);
        enemy.setSpeed(originalSpeed); // Restore original speed
    }

    ///////////////////////////////////////////////////////////////////
    
    public void addEffect(AbstractEffect effect) {
        if (effect != null) {

            if(activeEffects.contains(effect)){

                activeEffects.remove(effect);
                activeEffects.add(effect); // If the effect already exists, remove it and add it again to update its position in the list

            }else{

                activeEffects.add(effect); // Add the effect to the list of active effects

            }
            
        }
    }

    public boolean hasEffect(EffectTypes effectType) {
     
        for (AbstractEffect effect : activeEffects) {
            if (effect.getEffectType() == effectType) {
                return true; // Return true if the effect type is found in the active effects
            }
        }

        return false; // Return false if the effect type is not found in the active effects
    }

    /////// copy everything from the enemy to the decorator
    
    @Override
    public void setMovePathWithStrategy(ArrayList<Point2D> path, IMoveStrategy moveStrategy) {
        enemy.setMovePathWithStrategy(path, moveStrategy);
    }

    @Override
    public void getDamage(IProjectile projectile) {
        enemy.getDamage(projectile);
    }

    @Override
    public int getKillReward() {
        return enemy.getKillReward();
    }

    @Override
    public boolean isAlive() {
        return enemy.isAlive();
    }

    @Override
    public boolean isDead() {
        return enemy.isDead();
    }

    @Override
    public boolean hasArrived() {
        return enemy.hasArrived();
    }

    @Override
    public float getHealth() {
        return enemy.getHealth();
    }

    @Override
    public int getSpeed() {
        return enemy.getSpeed();
    }

    @Override
    public Point2D getCoordinate() {
        return enemy.getCoordinate();
    }

    @Override
    public Point2D getMoveDirection() {
        return enemy.getMoveDirection();
    }

    @Override
    public void locate(Point2D newCoordinate) {
        enemy.locate(newCoordinate);
    }

    @Override
    public EnemyType getEnemyType() {
        return enemy.getEnemyType();
    }

    @Override
    public void setSpeed(int speed) {
        enemy.setSpeed(speed);
    }

    @Override
    public void locateToStartPoint() {
        enemy.locateToStartPoint(); // Call the locateToStartPoint method of the enemy
    }

}
