package com.kurabiye.kutd.model.Tower;

    public enum TowerType {  // already static enum
        ARTILLERY(0), // 0 // Artillery tower type
        MAGE(1), // 1 // Mage tower type
        ARCHER(2); // 2 // Archer tower type


        private final int value;

        TowerType(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }
