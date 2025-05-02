package com.kurabiye.kutd.model.Listeners;

public interface IGameUpdateListener {

    /*
     *  This interface is used to listen for game updates.
     *  The given float parameter is deltaTime multiplied by the Time Coefficient.
     *
     */

    void onGameUpdate(float deltaTime); // Method to be called on game update with delta time as parameter
    // This method will be implemented by classes that want to listen for game updates

}
