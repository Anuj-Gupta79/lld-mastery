package com.lld.oop;

// LEARNING : Inheritance is a fundamental principle of object-oriented programming that allows a new class (called a subclass or child class) to inherit properties and behaviors (fields and methods) from an existing class (called a superclass or parent class).
class Vehicle {
    private String brand;
    private String color;
    private int speed;
    private int numberOfWheels;

    public Vehicle(String brand, String color, int speed, int numberOfWheels) {
        this.brand = brand;
        this.color = color;
        this.speed = speed;
        this.numberOfWheels = numberOfWheels;
    }

    public void move() {
        System.out.println("Vehicle is moving at speed: " + speed);
    }

    public String getBrand() {
        return brand;
    }

    public String getColor() {
        return color;
    }

    public int getSpeed() {
        return speed;
    }

    public String getNumberOfWheels() {
        if (numberOfWheels == 2) {
            return "Two wheels";
        } else if (numberOfWheels == 4) {
            return "Four wheels";
        }
        return numberOfWheels + " wheels";
    }
}

// LEARNING : The Car class is a subclass of the Vehicle class, which means it
// inherits all the properties and behaviors of the Vehicle class.
// The Car class can also have its own unique properties and behaviors, such as
// the number of doors and the ability to honk.
class Car extends Vehicle {
    private int numberOfDoors;

    // LEARNING: We can initialize the Car class using a constructor that calls the
    // constructor of the Vehicle class using the super keyword to set the common
    // attributes, and then initializes its own specific attribute (numberOfDoors).
    public Car(String brand, String color, int speed, int numberOfDoors) {
        super(brand, color, speed, 4);
        this.numberOfDoors = numberOfDoors;
    }

    public void honk() {
        System.out.println("Car is honking!");
    }

    public int getNumberOfDoors() {
        return numberOfDoors;
    }

    // LEARNING: The Car class can override the move method of the Vehicle class to
    // provide a more specific implementation for how a car moves.
    @Override
    public void move() {
        super.move(); // Call the move method of the Vehicle class
        System.out.println("Car is moving at speed: " + getSpeed());
    }
}

class Bike extends Vehicle {
    private boolean hasCarrier;

    public Bike(String brand, String color, int speed, boolean hasCarrier) {
        super(brand, color, speed, 2);
        this.hasCarrier = hasCarrier;
    }

    public void ringBell() {
        System.out.println("Bike is ringing the bell!");
    }

    public boolean hasCarrier() {
        return hasCarrier;
    }

    @Override
    public void move() {
        System.out.println("Bike is moving at speed: " + getSpeed());
    }
}

public class InheritanceDemo {

    public static void main(String[] args) {
        Car car = new Car("Toyota", "Red", 100, 4);
        System.out.println("Car Brand: " + car.getBrand());
        System.out.println("Car Color: " + car.getColor());
        System.out.println("Car Speed: " + car.getSpeed());
        System.out.println("Car Number of Doors: " + car.getNumberOfDoors());
        car.move();
        car.honk();

        Bike bike = new Bike("Yamaha", "Blue", 80, true);
        System.out.println("\nBike Brand: " + bike.getBrand());
        System.out.println("Bike Color: " + bike.getColor());
        System.out.println("Bike Speed: " + bike.getSpeed());
        System.out.println("Bike Has Carrier: " + bike.hasCarrier());
        bike.move();
        bike.ringBell();
    }
}
