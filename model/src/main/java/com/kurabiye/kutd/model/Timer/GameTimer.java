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


    
    private static final long TIME_COFACTOR_MILISEC = 1000; // The time cofactor used to convert seconds to milliseconds

    private long timeCoefficient = 1; // The time coefficient used to adjust the game speed

    private static GameTimer instance; // Singleton instance of the GameTimer class

    private long lastTime; // The last time the game timer was updated
    private long deltaTime; // The time elapsed since the last update

    private GameTimer() {
        lastTime = System.currentTimeMillis(); // Initialize the last time to the current time
    }

    public static GameTimer getInstance() {
        if (instance == null) {
            instance = new GameTimer(); // Create a new instance if it doesn't exist
        }
        return instance; // Return the singleton instance
    }

    /*
     * This should be called every frame to update the game timer.
     * It calculates the delta time and updates the last time.
     * 
     * 
     */

    public void update() {
        long currentTime = System.currentTimeMillis(); // Get the current time
        deltaTime = currentTime - lastTime; // Calculate the delta time
        lastTime = currentTime; // Update the last time to the current time
    }


    /*
        * This method returns the delta time in seconds.
        * It divides the delta time by the time cofactor to convert it to seconds.
        
        * 
        * @return The delta time in seconds
     */

    public long getDeltaTime() {
        return (deltaTime / TIME_COFACTOR_MILISEC) * timeCoefficient; // Return the delta time
    }

    public long getTimeCoefficient() {
        return timeCoefficient; // Return the time coefficient
    }
    public void setTimeCoefficient(long timeCoefficient) {
        this.timeCoefficient = timeCoefficient; // Set the time coefficient
    }

}
