package com.lld.oops.NestedAndInnerClass;

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

    // LEARNING: Static nested class — tied to the outer class, not an instance.
    // WHY: Can access outer static members (os, count) but not instance fields
    // (brand, price).
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

    // LEARNING: Inner class — tied to a specific Computer instance.
    // WHY: Can access both static (os) and instance (brand) members of the outer
    // class.
    class Battery {
        private int capacity;

        public Battery(int capacity) {
            this.capacity = capacity;
        }

        public void displayBatteryInfo() {
            System.out.println("Battery Capacity: " + capacity + "mAh, Brand: " + brand + ", OS: " + os);
        }
    }

    // LEARNING: Local inner class — defined inside a method, scoped to it.
    // WHY: One-off helper logic that belongs to a single method, not the whole
    // class.
    public void describeComputer() {
        class Summary {
            void displaySummary() {
                System.out.println("Brand: " + brand + ", Price: $" + price);
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

        // LEARNING: Anonymous inner class — one-shot interface implementation, no named
        // class needed.
        // WHY: Avoids creating a separate class for single-use behaviour like
        // OS-specific boot logic.
        Bootable windowBootable = new Bootable() {
            String os = "Windows";

            @Override
            public void boot() {
                System.out.println("Booting with " + os + "...");
            }
        };

        Bootable linuxBootable = new Bootable() {
            String os = "Linux";

            @Override
            public void boot() {
                System.out.println("Booting with " + os + "...");
            }
        };

        windowBootable.boot();
        linuxBootable.boot();
    }
}