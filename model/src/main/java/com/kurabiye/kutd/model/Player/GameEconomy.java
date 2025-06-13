package com.kurabiye.kutd.model.Player;

public class GameEconomy {

        private int currentGold; // Player's gold
        private int currentHealth; // Player's health


        UserPreference userPreference = UserPreference.getInstance(); // User preference instance


        public GameEconomy() {
            this.currentGold = userPreference.getStartingGold(); // Initialize with user's initial gold preference
            this.currentHealth = userPreference.getStartingHealth(); // Initialize with user's initial health preference
        }

        /**
         * Lose health method is used to deduct health from the player when they take damage.
         * @requires amount > 0
         * @param amount
         * @return if the player is still alive (true) or dead (false).
         */

        public synchronized boolean loseHealth(int amount) {
            
            currentHealth -= amount; // Deduct the amount from current health
            if (currentHealth <= 0) {
                currentHealth = 0; // Ensure health does not go below zero
                return false; // Player is dead
            }
            return true; // Player is still alive
        }

        /**
         * This method is to gain health for the player.
         * 
         *  amount > 0
         * 
         * @param amount
         * 
         * @return true if the health was successfully gained, false if the amount is negative.
         */


        public synchronized boolean gainHealth(int amount) {
            if (amount < 0) {
                return false; // Cannot gain negative health
            }
            currentHealth += amount; // Add the amount to current health
            return true; // Successfully gained health 
        }


        /**
         * This method is used to spend money from the player's current gold.
         * It checks if the player has enough gold to spend the specified amount.
         * @param amount
         * @return true if the money was successfully spent, false otherwise.
         */

        public synchronized boolean spendGold(int amount) {
            if (amount <= currentGold) {
                currentGold -= amount; // Deduct the amount from current gold
                return true; // Successfully spent money
            } else {
                return false; // Not enough gold to spend
            }
        }


        /**
         * This method is used to earn money for the player.
         * It adds the specified amount to the player's current gold.
         * It does not allow earning negative money.
         * 
         * @param amount
         * @return true if the money was successfully earned, false if the amount is negative.
         */

        public synchronized boolean earnGold(int amount) {
            if (amount < 0) {
               return false; // Cannot earn negative money
            }
            currentGold += amount; // Add the amount to current gold
            return true; // Successfully earned money
        }


        /**
         * This method is used to get the current amount of gold the player has.
         * 
         * @return currentGold
         */
        public synchronized int getCurrentGold() {
            return currentGold; // Return the current amount of gold
        }

        /**
         * This method is used to get the current amount of health the player has.
         * 
         * @return currentHealth
         */
        public synchronized int getCurrentHealth() {
            return currentHealth; // Return the current amount of health
        }






}
