package com.kurabiye.kutd.model.Player;

import java.util.ArrayList;

import com.kurabiye.kutd.util.ObserverPattern.Observable;
import com.kurabiye.kutd.util.ObserverPattern.Observer;

/* This class represents a player in the game.
 * It gets the user preferences from the UserPreferences class and sets the player name, score, level, health, and gold.
 * 
 * 
 * 
 * 
 */

public class Player implements Observable{

    public enum PlayerState {
        ALIVE,
        DEAD
    }
    private PlayerState playerState = PlayerState.ALIVE; // Player's alive status

    
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

    public synchronized boolean buyTower(int cost) {
        if (currentGold >= cost) { // Check if player has enough gold
            currentGold -= cost; // Deduct cost from player's gold
            // Notify observers of gold change
            notifyObservers(currentGold);
            return true; // Purchase successful
        }
        return false; // Purchase failed due to insufficient gold
    }
    public synchronized void earnGold(int amount) {
        currentGold += amount; // Add gold to player's total
        notifyObservers(this);
    }

    public synchronized void sellTower(int cost) {
        currentGold += cost; // Add cost to player's total gold
        notifyObservers(this);
    }
    /*
     * This method is used to deduct health from the player when they take damage.
     * @return true if the player is still alive, false if the player is dead.
     * This method is synchronized to ensure thread safety when modifying the player's health.
     */
    public synchronized void loseHealth() {
        currentHealth --; // Deduct damage from player's health
        if (currentHealth <= 0) {
            playerState = PlayerState.DEAD; // Set player state to dead
        }
        notifyObservers(this); // Notify observers of health change
    }

    public synchronized int getCurrentScore() {
        return currentScore; // Get player's score
    }
    
    public synchronized int getCurrentGold() {
        return currentGold; // Get player's gold
    }
    
    public synchronized int getCurrentHealth() {
        return currentHealth; // Get player's health
    }
    
    public synchronized void earnScore(int amount) {
        currentScore += amount; // Add score to player's total
        notifyObservers(this); // Notify observers of score change
    }

    public synchronized PlayerState getPlayerState() {
        return playerState; // Get player's state
    }


    /// Observer Pattern Methods
    /// 
    /// These methods are used to implement the Observer pattern, allowing other objects to observe changes in the Player's state.
    
    ArrayList<Observer> observers = new ArrayList<>(); // List of observers

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer); // Add an observer to the list
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer); // Remove an observer from the list
    }

    @Override
    public void notifyObservers(Object arg) {
        for (Observer observer : observers) {
            observer.update(this); // Notify each observer with the current Player object and argument
        }
    }

    

    

}
