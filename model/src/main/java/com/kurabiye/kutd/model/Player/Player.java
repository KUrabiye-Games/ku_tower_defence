package com.kurabiye.kutd.model.Player;


/* This class represents a player in the game.
 * It gets the user preferences from the UserPreferences class and sets the player name, score, level, health, and gold.
 * 
 * 
 * 
 * 
 */

public class Player{


    
    private int currentScore; // Player's score
    private int currentGold; // Player's gold
    private int currentHealth; // Player's health

    private UserPreference userPreferences; // User preferences object

    public Player(UserPreference userPreferences) {
        this.userPreferences = userPreferences; // Initialize user preferences
        this.currentGold = this.userPreferences.getStartingGold(); // Set player's gold from user preferences
        this.currentHealth = this.userPreferences.getStartingHealth(); // Set player's health from user preferences
        this.currentScore = 0; // Initialize player's score to 0
    }

    public int getCurrentScore() {
        return currentScore; // Get player's score
    }
    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore; // Set player's score
    }
    public int getCurrentGold() {
        return currentGold; // Get player's gold
    }
    public void setCurrentGold(int currentGold) {
        this.currentGold = currentGold; // Set player's gold
    }
    public int getCurrentHealth() {
        return currentHealth; // Get player's health
    }
    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth; // Set player's health
    }

    

    

}
