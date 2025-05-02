package com.kurabiye.kutd.model.Coordinates;

/**
 * A 2D geometric point that represents a location in (x,y) coordinate space.
 * This class is an alternative to JavaFX's Point2D, providing similar functionality
 * but contained within the project's own codebase.
 * 
 * We wanted to avoid using JavaFX's Point2D class to prevent unnecessary dependencies
 * 
 * @author JavaFX
 * @version 1.0
 * @since 2025-05-02
 */
public class Point2D {


    public static final Point2D ZER_POINT2D = new Point2D(0, 0); // A constant representing the origin point (0,0)
    
    private final double x; // The x coordinate
    private final double y; // The y coordinate
    
    /**
     * Creates a new instance of Point2D with the specified coordinates.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Gets the x coordinate.
     * 
     * @return the x coordinate
     */
    public double getX() {
        return x;
    }
    
    /**
     * Gets the y coordinate.
     * 
     * @return the y coordinate
     */
    public double getY() {
        return y;
    }
    
    /**
     * Adds the specified coordinates to this point and returns the result as a new Point2D.
     * 
     * @param x the x coordinate to add
     * @param y the y coordinate to add
     * @return a new Point2D with the result of addition
     */
    public Point2D add(double x, double y) {
        return new Point2D(this.x + x, this.y + y);
    }
    
    /**
     * Adds the coordinates of the specified point to this point and returns the result as a new Point2D.
     * 
     * @param point the point whose coordinates are to be added
     * @return a new Point2D with the result of addition
     */
    public Point2D add(Point2D point) {
        return add(point.getX(), point.getY());
    }
    
    /**
     * Subtracts the specified coordinates from this point and returns the result as a new Point2D.
     * 
     * @param x the x coordinate to subtract
     * @param y the y coordinate to subtract
     * @return a new Point2D with the result of subtraction
     */
    public Point2D subtract(double x, double y) {
        return new Point2D(this.x - x, this.y - y);
    }
    
    /**
     * Subtracts the coordinates of the specified point from this point and returns the result as a new Point2D.
     * 
     * @param point the point whose coordinates are to be subtracted
     * @return a new Point2D with the result of subtraction
     */
    public Point2D subtract(Point2D point) {
        return subtract(point.getX(), point.getY());
    }
    
    /**
     * Multiplies this point's coordinates by the specified factor and returns the result as a new Point2D.
     * 
     * @param factor the factor to multiply by
     * @return a new Point2D with the result of multiplication
     */
    public Point2D multiply(double factor) {
        return new Point2D(x * factor, y * factor);
    }
    
    /**
     * Calculates the magnitude (length) of this point's vector from the origin.
     * 
     * @return the magnitude of the vector
     */
    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }
    
    /**
     * Returns a normalized version of this point as a new Point2D.
     * A normalized point has the same direction but a magnitude of 1.
     * 
     * @return a new Point2D with the normalized coordinates
     */
    public Point2D normalize() {
        double mag = magnitude();
        if (mag == 0) {
            return new Point2D(0, 0);
        }
        return new Point2D(x / mag, y / mag);
    }
    
    /**
     * Calculates the distance between this point and the specified coordinates.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the distance
     */
    public double distance(double x, double y) {
        double dx = this.x - x;
        double dy = this.y - y;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * Calculates the distance between this point and another point.
     * 
     * @param point the other point
     * @return the distance
     */
    public double distance(Point2D point) {
        return distance(point.getX(), point.getY());
    }
    
    /**
     * Calculates the dot product of this point and the specified coordinates.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the dot product
     */
    public double dotProduct(double x, double y) {
        return this.x * x + this.y * y;
    }
    
    /**
     * Calculates the dot product of this point and another point.
     * 
     * @param point the other point
     * @return the dot product
     */
    public double dotProduct(Point2D point) {
        return dotProduct(point.getX(), point.getY());
    }
    
    /**
     * Creates a new Point2D that is interpolated between this point and the specified point.
     * 
     * @param point the other point
     * @param t the interpolation parameter (0.0 for this point, 1.0 for the other point)
     * @return the interpolated point
     */
    public Point2D interpolate(Point2D point, double t) {
        double dx = point.getX() - this.x;
        double dy = point.getY() - this.y;
        return new Point2D(this.x + dx * t, this.y + dy * t);
    }
    
    /**
     * Returns a point that is the midpoint between this point and the specified point.
     * 
     * @param point the other point
     * @return the midpoint
     */
    public Point2D midpoint(Point2D point) {
        return interpolate(point, 0.5);
    }
    
    /**
     * Compares this point with the specified object for equality.
     * 
     * @param obj the object to compare with
     * @return true if the objects are the same or if the other object is a Point2D with the same coordinates
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Point2D other = (Point2D) obj;
        // Using a small epsilon for floating point comparison
        final double EPSILON = 1e-10;
        return Math.abs(x - other.x) < EPSILON && Math.abs(y - other.y) < EPSILON;
    }
    
    /**
     * Returns a hash code for this point.
     * 
     * @return a hash code value
     */
    @Override
    public int hashCode() {
        long bits = java.lang.Double.doubleToLongBits(x);
        bits ^= java.lang.Double.doubleToLongBits(y) * 31;
        return (int) (bits ^ (bits >>> 32));
    }
    
    /**
     * Returns a string representation of this point.
     * 
     * @return a string representation
     */
    @Override
    public String toString() {
        return "Point2D[x=" + x + ", y=" + y + "]";
    }
}