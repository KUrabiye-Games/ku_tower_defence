package com.kurabiye.kutd.model.Collectable;

import com.kurabiye.kutd.model.Coordinates.Point2D;

/*  ICollectable.java
 *  This interface is used to define a collectable item.
 *  It is a generic interface that can be used with any type of item.
 *  
 *  @author: Atlas Berk Polat
 *  @version: 2.0
 *  @since: 2025-09-25
 */

public interface ICollectable<T> {


    T getItem();

    /** 
     * Get the coordinates of the collectable item.
     * @return The coordinates of the collectable item.
     */
    Point2D getCoordinates();

    void update(double deltaTime);

    boolean isExpired();

}
