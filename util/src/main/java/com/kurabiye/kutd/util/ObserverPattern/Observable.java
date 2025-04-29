package com.kurabiye.kutd.util.ObserverPattern;

public interface Observable {

    void addObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObservers(Object arg);

}
