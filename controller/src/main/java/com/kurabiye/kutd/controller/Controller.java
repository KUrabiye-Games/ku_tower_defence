package com.kurabiye.kutd.controller;


public abstract class Controller {
    public Controller() {
        initialize();
    }
    
    protected abstract void initialize();
}
