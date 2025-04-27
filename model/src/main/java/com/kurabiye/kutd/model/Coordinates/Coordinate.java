
package com.kurabiye.kutd.model.Coordinates;

/* This is the class that represents a coordinate in the game.
 * We define the end points of the map here.
 * We can use this class to define the coordinates of the map, players, enemies, items etc.
 * We can use this class to define the coordinates of the arrows, artillery, etc.
 * We assume that the display on the screen is 1920x1080.
 * We can adjust this depending on the screen size.
 * 
 * @author: Atlas Berk Polat
 * @version: 1.0
 * @since: 2025-04-23
 * 
 * 
 */
public class Coordinate {

    public static final int MAP_WIDTH = 1920; // The width of the map in pixels
    public static final int MAP_HEIGHT = 1080; // The height of the map in pixels

    private int x; // The x coordinate of the point
    private int y; // The y coordinate of the point

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        // Check if the x coordinate is within the bounds of the map
        if (x < 0 || x > MAP_WIDTH) {
            throw new IllegalArgumentException("X coordinate is out of bounds: " + x);
        }
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        // Check if the y coordinate is within the bounds of the map
        if (y < 0 || y > MAP_HEIGHT) {
            throw new IllegalArgumentException("Y coordinate is out of bounds: " + y);
        }
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Check if the object is the same instance
        if (!(o instanceof Coordinate)) return false; // Check if the object is of type Coordinate
        Coordinate that = (Coordinate) o; // Cast the object to Coordinate
        return x == that.x && y == that.y; // Check if the coordinates are equal
    }

    @Override
    public int hashCode() {
        int result = x; // Initialize the hash code with the x coordinate
        result = 31 * result + y; // Combine the x and y coordinates to generate a unique hash code
        return result; // Return the hash code
    }

}
