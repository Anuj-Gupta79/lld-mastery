package com.lld.oops.Polymorphism;

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
    @Override
    public void move() {
        System.out.println("Animal is moving");
    }

    public void makeNoise() {
        System.out.println("Animal is making noise");
    }
}

public class PolymorphismDemo {
    public static void main(String[] args) {
        // LEARNING: Declared type is LivingBeing — actual type resolved at runtime
        // (dynamic dispatch).
        // WHY: Caller code stays the same regardless of which subclass is added later.
        LivingBeing[] livingBeings = { new Human(), new Birds(), new Animal() };

        for (LivingBeing being : livingBeings) {
            // LEARNING: instanceof + cast needed to reach Animal-specific method not on the
            // superclass.
            // WHY: If makeNoise() belonged to LivingBeing or an interface, this cast would
            // be unnecessary.
            if (being instanceof Animal) {
                ((Animal) being).makeNoise();
            }
            being.move();
        }
    }
}