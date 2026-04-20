package com.lld.oop;

class Constants {

    // LEARNING: Static final variables are constants and cannot be changed after
    // initialization.
    public static final String APP_NAME = "My Application";
    public static final int MAX_USERS = 100;
}

class Counter {

    // LEARNING: Static variables are shared among all instances of the class.
    private static int count = 0;
    // LEARNING: Final instance variables must be initialized in the constructor and
    // cannot be changed afterward.
    final int id;

    // LEARNING: Static blocks are executed when the class is loaded, and they can
    // be used to initialize static variables.
    static {
        // WHY: static block runs once at class-load time — useful for one-time
        // initialisation (e.g. loading a config, registering a driver)
        System.out.println("Static block executed, When the class is loaded.");
    }

    public Counter() {
        count++;
        this.id = count;
    }

    // LEARNING: Static methods belong to the class and can be called without
    // creating an instance.
    public static int getCount() {
        return count;
    }

    // LEARNING: Final methods cannot be overridden by subclasses.
    public final void display() {
        System.out.println("Counter ID: " + id);
    }
}

class Utility {
    // LEARNING: Static methods can be called without creating an instance of the
    // class.
    public static void printMessage(String message) {
        System.out.println("Message: " + message);
    }
}

class Measure extends Counter {
    // LEARNING: We cannnot override a final method in the parent class.
    // Uncommenting the below code will result in a compilation error.

    // @Override
    // public void display() {
    // System.out.println("Counter ID: " + id);
    // }

}

public class StaticAndFinalDemo {

    public static void main(String[] args) {
        // Accessing static final variables
        System.out.println("App Name: " + Constants.APP_NAME);
        System.out.println("Max Users: " + Constants.MAX_USERS);

        // Creating instances of Counter
        Counter counter1 = new Counter();
        Counter counter2 = new Counter();

        // Accessing static method
        System.out.println("Current Count: " + Counter.getCount());

        // Displaying counter IDs
        counter1.display();
        counter2.display();

        // Using Utility class to print a message
        Utility.printMessage("Hello, World!");

        // LEARNING: final on a reference means the pointer can't change,
        // but the object it points to can still be mutated.
        final Counter ref = new Counter();
        ref.display(); // fine
        // ref = new Counter(); // compile error — can't reassign the reference

        // WHY: final ≠ immutable. It only freezes the variable binding, not the object.
    }

}
