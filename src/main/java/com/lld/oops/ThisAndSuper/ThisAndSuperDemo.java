package com.lld.oops.ThisAndSuper;

class Animal {
    private String name;
    private String type;

    Animal(String name) {

        // LEARNING: this() calls another constructor in the same class.
        // WHY: Constructor chaining avoids duplicated initialization logic.
        this(name, "Unknown");
    }

    Animal(String name, String type) {

        // LEARNING: this.field disambiguates instance variables from parameters.
        this.name = name;
        this.type = type;
    }

    public void displayInfo() {
        System.out.println("Name: " + name);
        System.out.println("Type: " + type);
    }
}

class Dog extends Animal {
    private String breed;

    Dog(String name, String type, String breed) {

        // LEARNING: super() initializes the parent portion of the object.
        // WHY: Parent constructors always execute before child constructors.
        super(name, type);

        this.breed = breed;
    }

    Dog(String name, String breed) {
        this(name, "Domestic", breed);
    }

    Dog(String name) {
        this(name, "Domestic", "Mixed");
    }

    @Override
    public void displayInfo() {

        // LEARNING: super.method() reuses parent behaviour inside an override.
        super.displayInfo();

        System.out.println("Breed: " + breed);
    }
}

public class ThisAndSuperDemo {

    public static void main(String[] args) {
        Dog dog1 = new Dog("Buddy", "Golden Retriever");
        Dog dog2 = new Dog("Max");
        Dog dog3 = new Dog("Charlie", "Domestic", "Beagle");

        System.out.println("Dog 1:");
        dog1.displayInfo();

        System.out.println("Dog 2:");
        dog2.displayInfo();

        System.out.println("Dog 3:");
        dog3.displayInfo();
    }
}