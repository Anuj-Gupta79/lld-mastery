package com.lld.oop;

class LivingBeing {
    public void move() {
        System.out.println("Living being is moving");
    }
}

class Human extends LivingBeing {
    @Override
    public void move() {
        System.out.println("Human is moving");
    }
}

class Birds extends LivingBeing {
    @Override
    public void move() {
        System.out.println("Birds is moving");
    }
}

class Animal extends LivingBeing {

    public void makeNoise() {
        System.out.println("Animal is making noise");
    }

    @Override
    public void move() {
        System.out.println("Animal is moving");
    }
}

public class PolymorphismDemo {
    public static void main(String[] args) {
        // LEARNING: Polymorphism allows us to treat objects of different classes that
        // share a common superclass as objects of the superclass type.
        // In this example, we have an array of LivingBeing objects that can hold
        // instances of Human, Birds, and Animal classes.
        // We can call the move() method on each of these objects, and the correct
        // implementation will be executed based on the actual type of the object at
        // runtime.
        // This is known as dynamic method dispatch, which is a key feature of
        // polymorphism in Java.
        // It allows for flexibility and extensibility in our code, as we can add new
        // subclasses of LivingBeing without modifying the existing code that uses the
        // LivingBeing type.
        LivingBeing[] livingBeings = { new Human(), new Birds(), new Animal() };

        for (LivingBeing being : livingBeings) {
            if (being instanceof Animal) {
                // LEARNING: If we want to call a method that is specific to the Animal class
                // (like makeNoise()),
                // we can use the instanceof operator to check if the object is an instance of
                // the Animal class,
                // and then cast it to the Animal type to call the makeNoise() method.
                ((Animal) being).makeNoise();

                // Doubt: What if there are other classes which also have makeNoise() method,
                // how to call that method without using instanceof and type casting?
                // Here is the topic comes abstraction, we can create an abstract class or
                // interface with the makeNoise() method and let all the classes implement that
                // interface or extend that abstract class, then we can call the makeNoise()
                // method without using instanceof and type casting.
            }

            being.move();
        }
    }
}
