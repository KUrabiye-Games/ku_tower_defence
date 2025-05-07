package com.kurabiye.kutd.util.FactoryPattern;

public interface EnumFactory<T, E extends Enum<E>> {
    // This interface defines a factory method for creating objects of type T
    T create(E type); // Method to create an object of type T

}
