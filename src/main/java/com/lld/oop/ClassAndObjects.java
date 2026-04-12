package com.lld.oop;

public class ClassAndObjects {
    private String brand;
    private String color;
    private int speed;

    public ClassAndObjects(String brand, String color, int speed) {
        this.brand = brand;
        this.color = color;
        this.speed = speed;
    }

    public void accelerate() {
        speed += 10;

        System.out.println("Current speed: " + speed);
    }

    public void brake() {
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
        ClassAndObjects car1 = new ClassAndObjects("Toyota", "Red", 0);
        car1.accelerate();
        car1.brake();

        ClassAndObjects car2 = new ClassAndObjects("Honda", "Blue", 20);
        car2.accelerate();
        car2.brake();
    }
}
