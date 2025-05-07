package com.kurabiye.kutd.util.FactoryPattern;

public interface CodeFactory<T> {

    // This interface defines a factory method for creating objects of type T
    T create(int code); // Method to create an object of type T
    // This method is used to create an object of type T
    // The implementation of this method will be provided by the classes that implement this interface

}
