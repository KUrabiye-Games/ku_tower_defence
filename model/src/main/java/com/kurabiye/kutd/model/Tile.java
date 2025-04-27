package com.kurabiye.kutd.model;

public abstract class Tile implements Serializable {

    protected Point position;

    public Tile(Point position) {
        this.position = position;
    }

    /**
     * Returns the grid position of this tile.
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Gets the type of the tile, e.g., "PATH", "TOWER_SLOT", "DECORATION".
     */
    public abstract String getType();

    /**
     * Whether this tile can be walked on by enemies.
     */
    public boolean isWalkable() {
        return false;
    }

    /**
     * Whether a tower can be built on this tile.
     */
    public boolean isBuildable() {
        return false;
    }

    @Override
    public String toString() {
        return getType() + " at " + position.x + "," + position.y;
    }
}

