package com.kurabiye.kutd.model.Enemy.Decorators;

public abstract class AbstractEffect implements IEffect {

    protected final static double INFINITY_EFFECT_DURATION = Double.POSITIVE_INFINITY; // Constant for infinite effect duration

    protected double remainingEffectTime; // Remaining effect time for the enemy

    public AbstractEffect(double remainingEffectTime) {
        this.remainingEffectTime = remainingEffectTime; // Initialize the effect with the remaining effect time
    }

    @Override
    public void update(double deltaTime) {

        if (remainingEffectTime > 0) {
            remainingEffectTime -= deltaTime; // Update the remaining effect time based on the elapsed time
        }
        
    }

    @Override
    public boolean isExpired() {
        return remainingEffectTime <= 0; // Check if the effect has expired
    }

    @Override
    public void removeEffect() {
        // This method can be overridden by subclasses to implement specific removal behavior
        // The default implementation does nothing.
        remainingEffectTime = 0; // Set the remaining effect time to zero to indicate removal
    }

    @Override
    public abstract EffectTypes getEffectType(); // Abstract method to get the type of effect


    @Override
    public int getPriority() {
        // This method can be overridden by subclasses to implement specific priority behavior
        // The default implementation returns 0, indicating no specific priority
        return 0; // Default priority is 0
    }
}
