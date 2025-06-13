package com.kurabiye.kutd.model.Enemy.Decorators;

import java.util.ArrayList;

import com.kurabiye.kutd.model.Coordinates.Point2D;
import com.kurabiye.kutd.model.Enemy.EnemyType;
import com.kurabiye.kutd.model.Enemy.IEnemy;
import com.kurabiye.kutd.model.Enemy.MoveStrategy.IMoveStrategy;
import com.kurabiye.kutd.model.Projectile.IProjectile;


public abstract class EnemyDecorator implements IEnemy {


    public static final double INFINITY_EFFECT_DURATION = Double.POSITIVE_INFINITY; // Constant for infinite effect duration

    protected double remainigEffectTime = INFINITY_EFFECT_DURATION; // Default effect duration in seconds

    // This class is an abstract decorator for the IEnemy interface
    // It can be used to add additional functionality to the IEnemy interface
    // without modifying the original interface or its implementations

    protected IEnemy enemy;



    public EnemyDecorator(IEnemy enemy) {
        this.enemy = enemy; // Initialize the decorator with an IEnemy instance
    }

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
    public void move(double deltaTime) {
        enemy.move(deltaTime);
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

     /** 
     * This method is used to remove the decoration from the enemy.
     * It returns the original enemy without any decoration.
     * * @return The original enemy without decoration.
     */
    public IEnemy removeDecoration() {
        return enemy; // Return the original enemy without decoration
    }

    @Override
    public Point2D getMoveDirection() {
        return enemy.getMoveDirection();
    }


    public abstract EffectTypes getEffectType() ; // Abstract method to get the effect type of the decorator

    public double getRemainingEffectTime() {
        return remainigEffectTime; // Get the remaining effect time of the decorator
    }

    public boolean isOver(){
        return remainigEffectTime <= 0; // Check if the effect time is over
    }

    public void setRemainingEffectTime(double remainingEffectTime) {
        this.remainigEffectTime = remainingEffectTime; // Set the remaining effect time of the decorator
    }


}
