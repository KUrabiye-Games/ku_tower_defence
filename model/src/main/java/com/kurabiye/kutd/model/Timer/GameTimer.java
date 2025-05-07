package com.kurabiye.kutd.model.Timer;

/* GameTimer.java
 * This class is responsible for managing the game timer.
 * It is used to keep track of the time elapsed during the game.
 * It can be used to get the delta time between frames and to manage the game loop.
 * It is a singleton class, meaning that there is only one instance of this class in the game.
 * 
 * @author Atlas Berk Polat
 * @version 1.0
 * @since 2025-04-25
 * 
 */

public class GameTimer {


    
    private static final double TIME_COFACTOR_MILISEC = 1000; // The time cofactor used to convert seconds to milliseconds

    private double timeCoefficient = 1; // The time coefficient used to adjust the game speed

    // Using volatile to ensure visibility across threads
    private static volatile GameTimer instance; // Singleton instance of the GameTimer class

    private double lastTime; // The last time the game timer was updated
    private double deltaTime; // The time elapsed since the last update

    private GameTimer() {
        lastTime = System.currentTimeMillis(); // Initialize the last time to the current time
    }

    /**
     * Thread-safe singleton implementation using double-checked locking
     * @return The singleton instance of GameTimer
     */
    public static GameTimer getInstance() {
        // First check (no locking)
        if (instance == null) {
            // Lock for thread safety
            synchronized (GameTimer.class) {
                // Second check (with locking)
                if (instance == null) {
                    instance = new GameTimer(); // Create a new instance if it doesn't exist
                }
            }
        }
        return instance; // Return the singleton instance
    }

    /**
     * Resets the singleton instance (useful for testing)
     */
    public static synchronized void resetInstance() {
        instance = null;
    }

    /*
     * This should be called every frame to update the game timer.
     * It calculates the delta time and updates the last time.
     * 
     */
    public synchronized void update() {
        long currentTime = System.currentTimeMillis(); // Get the current time
        deltaTime = currentTime - lastTime; // Calculate the delta time
        lastTime = currentTime; // Update the last time to the current time
    }

    public synchronized void resetTimer() {
        lastTime = System.currentTimeMillis(); // Reset the last time to the current time
    }


    /**
     * This method returns the delta time in seconds.
     * It divides the delta time by the time cofactor to convert it to seconds.
     * @return the delta time in seconds
     */
    public synchronized double getDeltaTime() {
        return (deltaTime / TIME_COFACTOR_MILISEC) * timeCoefficient; // Return the delta time
    }

    /**
     * Get the time coefficient used to adjust game speed
     * @return the time coefficient value
     */
    public synchronized double getTimeCoefficient() {
        return timeCoefficient; // Return the time coefficient
    }
    
    /**
     * Set the time coefficient to adjust game speed
     * @param coefficient the new time coefficient
     */
    public synchronized void setTimeCoefficient(double coefficient) {
        if (coefficient > 0) {
            timeCoefficient = coefficient;
        }
    }
}
