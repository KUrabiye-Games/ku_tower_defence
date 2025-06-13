package com.kurabiye.kutd.model.Player;

import java.util.ArrayList;

import com.kurabiye.kutd.util.ObserverPattern.Observable;
import com.kurabiye.kutd.util.ObserverPattern.Observer;

/** This class represents a player in the game.
 * It gets the user preferences from the UserPreferences class and sets the player name, score, level, health, and gold.
 * 
 * It uses the facade pattern to manage the player's economy, including earning and spending gold, buying and selling towers, and losing health.
 * 
 * @author Atlas Berk Polat
 * @version 2.0
 * @since 2025-05-13
 */

public class Player implements Observable{

  
    private PlayerState playerState = PlayerState.ALIVE; // Player's alive status

    private GameEconomy gameEconomy = new GameEconomy(); // Game economy object to manage gold

    public Player() {
    }

    public boolean buyTower(int cost) {
       return gameEconomy.spendGold(cost); // Spend money to buy a tower
    }

    /**
     * @requires amount > 0
     * This method is used to earn gold for the player.
     * It adds the specified amount to the player's current gold.
     * @param amount
     */
    public void earnGold(int amount) {
        gameEconomy.earnGold(amount); // Earn money for the player
        notifyObservers(this);
    }

    /**
     * This method is used to sell a tower.
     * @requires cost > 0
     * @param cost
     */

    public void sellTower(int cost) {
        gameEconomy.earnGold(cost); // Earn money from selling a tower
        notifyObservers(this);
    }
    /*
     * This method is used to deduct health from the player when they take damage.
     * @return true if the player is still alive, false if the player is dead.
     * This method is synchronized to ensure thread safety when modifying the player's health.
     */
    public  void loseHealth(int damage) {
        if (!gameEconomy.loseHealth(damage)) {
            playerState = PlayerState.DEAD; // Set player state to dead
        }
        notifyObservers(this); // Notify observers of health change
    }

    
    public int getCurrentGold() {
        return gameEconomy.getCurrentGold(); // Get player's gold
    }
    
    public int getCurrentHealth() {
        return gameEconomy.getCurrentHealth(); // Get player's health
    }
    

    public PlayerState getPlayerState() {
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
