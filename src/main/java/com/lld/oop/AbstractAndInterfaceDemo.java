package com.lld.oop;

abstract class LivingBeing {
    public abstract void move();

    public abstract void eat();

    public void breathe() {
        System.out.println("Living being is breathing");
    }
}

interface Flyable {
    void fly();
}

interface Swimmable {
    void swim();
}

class Person extends LivingBeing implements Swimmable {
    @Override
    public void move() {
        System.out.println("Person is moving");
    }

    @Override
    public void eat() {
        System.out.println("Person is eating");
    }

    @Override
    public void swim() {
        System.out.println("Person is swimming");
    }
}

class Duck extends LivingBeing implements Flyable, Swimmable {
    @Override
    public void move() {
        System.out.println("Duck is moving");
    }

    @Override
    public void eat() {
        System.out.println("Duck is eating");
    }

    @Override
    public void fly() {
        System.out.println("Duck is flying");
    }

    @Override
    public void swim() {
        System.out.println("Duck is swimming");
    }
}

class Eagle extends LivingBeing implements Flyable {
    @Override
    public void move() {
        System.out.println("Eagle is moving");
    }

    @Override
    public void eat() {
        System.out.println("Eagle is eating");
    }

    @Override
    public void fly() {
        System.out.println("Eagle is flying");
    }
}

class Cat extends LivingBeing {
    @Override
    public void move() {
        System.out.println("Cat is moving");
    }

    @Override
    public void eat() {
        System.out.println("Cat is eating");
    }
}

public class AbstractAndInterfaceDemo {

    public static void main(String[] args) {

        // LEARNING: Abstract classes IS-A relationship. Basically abstract classes are
        // used to represent a common base class for a group of related classes that
        // share common attributes and behaviors. They provide a way to define a common
        // interface and some shared implementation for the subclasses, while still
        // allowing the subclasses to provide their own specific implementations for
        // certain methods.
        // In this example, we have an abstract class LivingBeing that defines common
        // behaviors (move, eat, breathe) for all living beings. The Person, Duck,
        // Eagle, and Cat classes extend the LivingBeing class and provide their own
        // specific implementations for the move() and eat() methods, while they inherit
        // the breathe() method from the LivingBeing class. This allows us to treat all
        // these classes as LivingBeing objects and call their common behaviors without
        // worrying about their specific types. This is a key feature of object-oriented
        // programming that promotes code reusability and flexibility.
        LivingBeing[] beings = { new Person(), new Duck(), new Eagle(), new Cat() };

        for (LivingBeing being : beings) {
            being.move();
            being.eat();
            being.breathe();
        }

        System.out.println("------------------------------------------");

        // LEARNING: Interfaces CAN-DO relationship, they are used to define a contract
        // for what a class can do, without specifying how it does it.
        // In this example, we have two interfaces, Flyable and Swimmable, that define
        // the behaviors of flying and swimming, respectively. The Person class
        // implements the Swimmable interface, which means that it can swim, while the
        // Duck class implements both the Flyable and Swimmable interfaces, which means
        // that it can both fly and swim. The Eagle class implements only the Flyable
        // interface, which means that it can fly but not swim. This allows us to treat
        // these classes as Flyable or Swimmable objects and call their respective
        // behaviors without worrying about their specific types. This is a key feature
        // of object-oriented programming that promotes code flexibility and allows us
        // to define common behaviors across unrelated classes without forcing them to
        // share a common superclass.
        Flyable[] flyables = { new Duck(), new Eagle() };
        for (Flyable flyable : flyables) {
            flyable.fly();
        }

        System.out.println("------------------------------------------");

        // WHY: Swimmable[] swimmables is better than just calling
        // duck.swim() and person.swim() directly?
        // LEARNING: Using an array of Swimmable objects allows us to treat different
        // classes that implement the Swimmable interface in a uniform way. This
        // promotes code flexibility and scalability, as we can easily add new classes
        // that implement the Swimmable interface without modifying the existing code
        // that uses the Swimmable type. It also allows us to call the swim() method on
        // each object without worrying about their specific types, as long as they
        // implement the Swimmable interface. This is a key feature of object-oriented
        // programming that promotes code reusability and flexibility, as we can define
        // common behaviors across unrelated classes without forcing them to share a
        // common superclass. In contrast, calling duck.swim() and person.swim()
        // directly would require us to know the specific types of the objects and would
        // not allow us to easily add new classes that can swim without modifying the
        // existing code.
        Swimmable[] swimmables = { new Person(), new Duck() };
        for (Swimmable swimmable : swimmables) {
            swimmable.swim();
        }
    }
}
