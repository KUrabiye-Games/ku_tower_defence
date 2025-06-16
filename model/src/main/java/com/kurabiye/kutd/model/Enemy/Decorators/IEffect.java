package com.kurabiye.kutd.model.Enemy.Decorators;

public interface IEffect {

     public void update(double deltaTime); // Method to update the effect based on the time elapsed

     public boolean isExpired();

     public void removeEffect(); // Method to remove the effect from the enemy

     public EffectTypes getEffectType(); // Method to get the type of effect

     public int getPriority(); // Method to get the priority of the effect
}
