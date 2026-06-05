package com.lld.oops.ClassesAndObjects;

// LEARNING: Class = blueprint; object = instance created from it via constructor.
public class ClassesAndObjectsDemo {

    // LEARNING: Private fields — data hidden from outside, accessed only via
    // methods (encapsulation).
    private String brand;
    private String color;
    private int speed;

    public ClassesAndObjectsDemo(String brand, String color, int speed) {
        this.brand = brand;
        this.color = color;
        this.speed = speed;
    }

    public void accelerate() {
        speed += 10;
        System.out.println("Current speed: " + speed);
    }

    // LEARNING: Guard ensures speed never goes negative — behaviour owned by the
    // class, not the caller.
    public void brake() {
        speed = (speed >= 10) ? speed - 10 : 0;
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