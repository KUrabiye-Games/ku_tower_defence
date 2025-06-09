package com.kurabiye.kutd.model.Projectile;

public enum ProjectileType { // Enum for different projectile types
        ARTILLERY(0), // Arrow projectile type
        MAGIC(1), // Magic projectile type
        ARROW(2); // Artillery projectile type

        private final int value;

        ProjectileType(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }