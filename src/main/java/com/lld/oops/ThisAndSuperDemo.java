package com.lld.oops;

/**
 * Interesting Fact:
 * If there is a static variable within the class,
 * we can still use the this keyword to refer to the current instance of the
 * class, but it is not recommended.
 * It can lead to confusion and is generally considered bad practice. Instead,
 * we should use the class name to refer to static variables to make it clear
 * that they belong to the class rather than an instance of the class.
 * 
 * Quick thought would be why Java allows it? why not just throw an error when
 * we try to use this to refer to a static variable?
 * The reason is that Java allows it for backward compatibility and to provide
 * flexibility in certain scenarios.
 * For example, if we have a static variable that is commonly accessed within
 * instance methods, using this can make the code more concise and easier to
 * read in some cases.
 * However, it is important to use it judiciously and be aware of the potential
 * for confusion when using this to refer to static variables.
 * It is generally recommended to use the class name to refer to static
 * variables to avoid any confusion and improve code readability.
 */

class Animal {
    private String name;
    private String type;

    Animal(String name) {
        // LEARNING: The this keyword is used to refer to the current instance of the
        // class. In this constructor, we are calling another constructor of the same
        // class using this() to initialize the name and type attributes with default
        // values.
        // LEARNING: this() must be the FIRST line — cannot have any statement before it
        this(name, "Unknown");
    }

    Animal(String name, String type) {
        // LEARNING: This keyword tells the compiler that we are referring to the
        // instance variable name and type of the current object, not the parameters of
        // the constructor. It helps to distinguish between the instance variables and
        // the parameters when they have the same name.
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
        // LEARNING: The super keyword is used to refer to the parent class (Animal) and
        // call its constructor to initialize the name and type attributes. This allows
        // us to reuse the code in the parent class and avoid duplication.
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
        // LEARNING: The super keyword can also be used to call a method from the parent
        // class. In this case, we are calling the displayInfo() method of the Animal
        // class to display the name and type of the dog before displaying its breed.
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
