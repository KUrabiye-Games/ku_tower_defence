package com.kurabiye.kutd.model.Enemy;

public enum EnemyType { // Enum for different enemy types
        GOBLIN(0), // Goblin enemy type
        KNIGHT(1); // Knight enemy type
        
        private final int value;
        
        EnemyType(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }
