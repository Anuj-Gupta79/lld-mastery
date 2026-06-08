package com.lld.oops.StaticAndFinal;

class Constants {

    // LEARNING: static final is the standard way to define constants in Java.
    public static final String APP_NAME = "My Application";
    public static final int MAX_USERS = 100;
}

class Counter {

    // LEARNING: One shared copy exists for the entire class.
    private static int count = 0;

    // LEARNING: final fields must be assigned exactly once.
    final int id;

    // LEARNING: Runs once when the class is loaded.
    // WHY: Useful for one-time initialization.
    static {
        System.out.println("Static block executed, When the class is loaded.");
    }

    public Counter() {
        count++;
        this.id = count;
    }

    public static int getCount() {
        return count;
    }

    // LEARNING: final methods cannot be overridden by subclasses.
    public final void display() {
        System.out.println("Counter ID: " + id);
    }
}

class Utility {

    // LEARNING: Static methods belong to the class, not an object.
    public static void printMessage(String message) {
        System.out.println("Message: " + message);
    }
}

class Measure extends Counter {

    // WHY: Overriding a final method causes a compilation error.

    // @Override
    // public void display() {
    // System.out.println("Counter ID: " + id);
    // }
}

public class StaticAndFinalDemo {

    public static void main(String[] args) {

        System.out.println("App Name: " + Constants.APP_NAME);
        System.out.println("Max Users: " + Constants.MAX_USERS);

        Counter counter1 = new Counter();
        Counter counter2 = new Counter();

        System.out.println("Current Count: " + Counter.getCount());

        counter1.display();
        counter2.display();

        Utility.printMessage("Hello, World!");

        // LEARNING: final freezes the reference, not the object.
        final Counter ref = new Counter();

        ref.display();

        // ref = new Counter(); // Compilation error
    }
}