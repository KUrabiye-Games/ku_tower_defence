package com.kurabiye.kutd.model.Collectable;

import com.kurabiye.kutd.model.Coordinates.Point2D;


public class GoldBag implements ICollectable<Integer> {

    private final int goldAmount;

    

    private Point2D coordinates; // Coordinates of the gold bag, if needed

    private static final double LIFESPAN = 10.0; // 10 seconds

    private double remainingTime = LIFESPAN; // Remaining time for the gold bag to be collected, if needed

    /** 
     * Constructor for the GoldBag class.
     * @throws IllegalArgumentException if the gold amount is negative.
     * @param goldAmount The amount of gold in the bag.
     */
    public GoldBag(Point2D coordinates, int goldAmount) {
        this.coordinates = coordinates; // Set the coordinates of the gold bag

        if (goldAmount < 0) {
            throw new IllegalArgumentException("Gold amount cannot be negative");
        }
         // Ensure the gold amount is non-negative
        this.goldAmount = goldAmount;
    }

    /**
     * Returns the amount of gold in the bag.
     * @return The amount of gold.
     */
    @Override
    public Integer getItem() {
        return goldAmount;
    }

    @Override
    public Point2D getCoordinates() {
        return coordinates;
    }

    @Override
    public void update(double deltaTime) {
        remainingTime -= deltaTime; // Decrease the remaining time by the delta time
        if (remainingTime < 0) {
            remainingTime = 0; // Ensure remaining time does not go below zero
        }
    }

    @Override
    public boolean isExpired() {
        return remainingTime <= 0; // Check if the gold bag has expired
    }

}
