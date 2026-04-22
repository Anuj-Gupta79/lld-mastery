package com.lld.oops;

interface Bootable {
    void boot();
}

class Computer {
    private String brand;
    private int price;
    private static int count;
    private static String os;

    static {
        os = "Windows";
        System.out.println("Static block executed, When the class is loaded.");
    }

    public Computer(String brand, int price) {
        this.brand = brand;
        this.price = price;
        count++;
    }

    // LEARNING: Static nested classes are associated with the outer class and can
    // access its static members, but they cannot access non-static members
    // directly. They are instantiated without needing an instance of the outer
    // class. In contrast, inner classes are associated with an instance of the
    // outer class and can access both static and non-static members of the outer
    // class. They require an instance of the outer class to be instantiated.
    static class Processor {
        private String model;
        private int cores;

        public Processor(String model, int cores) {
            this.model = model;
            this.cores = cores;
        }

        public void displayProcessorInfo() {
            System.out.println(
                    "Processor Model: " + model + ", Cores: " + cores + ", OS: " + os + ", Total Computers: " + count);
        }
    }

    // LEARNING: Inner classes are non-static nested classes that are associated
    // with an instance of the outer class. They can access both static and
    // non-static members of the outer class. To instantiate an inner class, we need
    // an instance of the outer class. In this example, the Battery class is an
    // inner class of the Computer class, and it can access the brand and os
    // attributes of the Computer class when displaying battery information.
    class Battery {
        private int capacity;

        public Battery(int capacity) {
            this.capacity = capacity;
        }

        public void displayBatteryInfo() {
            System.out
                    .println("Battery Capacity: " + capacity + "mAh, Computer Brand: " + brand + ", OS: " + os);
        }
    }

    // LEARNING: Local inner classes are defined within a method and can only be
    // accessed within that method. They can access final or effectively final
    // variables from the enclosing method. In this example, the Summary class is a
    // local inner class defined within the describeComputer() method, and it can
    // access the brand and price variables of the Computer class to display a
    // summary of the computer's information. Local inner classes are useful for
    // encapsulating helper classes that are only relevant within the context of a
    // specific method, and they help to keep the code organized and maintainable by
    // limiting the scope of the inner class to where it is needed.
    public void describeComputer() {

        class Summary {
            void displaySummary() {
                System.out.println("Computer Brand: " + brand + ", Price: $" + price);
            }
        }

        new Summary().displaySummary();
    }

}

public class NestedAndInnerClassDemo {

    public static void main(String[] args) {
        Computer computer = new Computer("Dell", 1200);
        computer.describeComputer();

        Computer.Processor processor = new Computer.Processor("Intel i7", 8);
        processor.displayProcessorInfo();

        Computer.Battery battery = computer.new Battery(5000);
        battery.displayBatteryInfo();

        // LEARNING: Anonymous inner classes are a way to create a one-time use class
        // that implements an interface or extends a class without having to explicitly
        // define a new class. They are often used for event handling or when we need to
        // provide a specific implementation of an interface or class without the need
        // for a separate named class. In this example, we are creating an anonymous
        // inner class that implements the Bootable interface and provides an
        // implementation for the boot() method. This allows us to define the behavior
        // of the boot() method without having to create a separate named class for it.
        // Anonymous inner classes can be useful for simplifying code and reducing the
        // number of classes in a program, but they can also make code harder to read if
        // overused or used in complex scenarios. It is important to use anonymous inner
        // classes judiciously and consider readability when deciding whether to use
        // them in your code.
        Bootable windowBootable = new Bootable() {
            String os = "Windows";

            @Override
            public void boot() {
                System.out.println("Booting the computer with " + os + "...");
            }
        };

        Bootable linuxBootable = new Bootable() {
            String os = "Linux";

            @Override
            public void boot() {
                System.out.println("Booting the computer with " + os + "...");
            }
        };

        windowBootable.boot();
        linuxBootable.boot();
    }

}