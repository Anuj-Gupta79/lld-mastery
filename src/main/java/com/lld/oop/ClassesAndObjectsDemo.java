package com.lld.oop;

// LEARNING : Creating classes and objects in Java
public class ClassesAndObjectsDemo {

    // LEARNING : Always use private access modifier for class attributes to
    // encapsulate data and provide controlled access through public methods
    // (getters and setters).
    private String brand;
    private String color;
    private int speed;

    // LEARNING : Constructor to initialize the attributes of the class when an
    // object is created.
    public ClassesAndObjectsDemo(String brand, String color, int speed) {
        this.brand = brand;
        this.color = color;
        this.speed = speed;
    }

    public void accelerate() {
        speed += 10;

        System.out.println("Current speed: " + speed);
    }

    public void brake() {
        // LEARNING : Implementing a simple braking mechanism that reduces speed by 10
        // units, ensuring it doesn't go below 0.
        if (speed >= 10) {
            speed -= 10;
        } else {
            speed = 0;
        }

        System.out.println("Current speed: " + speed);
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

    public static void main(String[] args) {
        ClassesAndObjectsDemo car1 = new ClassesAndObjectsDemo("Toyota", "Red", 0);
        car1.accelerate();
        car1.brake();

        ClassesAndObjectsDemo car2 = new ClassesAndObjectsDemo("Honda", "Blue", 20);
        car2.accelerate();
        car2.brake();
    }
}
