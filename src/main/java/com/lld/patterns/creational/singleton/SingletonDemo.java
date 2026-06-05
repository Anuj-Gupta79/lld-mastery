package com.lld.patterns.creational.singleton;

// LEARNING: Singleton ensures only one instance exists and provides a global access point.
public class SingletonDemo {

    // LEARNING: Eager init — instance created at class load time, guaranteed
    // thread-safe by JVM.
    // WHY: Simple and safe, but wastes memory if instance is never used.
    static class Variant1 {
        private static final Variant1 instance = new Variant1();

        private Variant1() {
        }

        public static Variant1 getInstance() {
            return instance;
        }
    }

    // LEARNING: Lazy init — instance created only on first call, but not
    // thread-safe.
    // WHY: Two threads hitting null check simultaneously can each create a separate
    // instance.
    static class Variant2 {
        private static Variant2 instance;

        private Variant2() {
        }

        public static Variant2 getInstance() {
            if (instance == null) {
                instance = new Variant2();
            }
            return instance;
        }
    }

    // LEARNING: Double-checked locking — lazy + thread-safe. Outer check avoids
    // lock on every call.
    // WHY: volatile prevents CPU from returning a partially constructed instance
    // from cache.
    static class Variant3 {
        private static volatile Variant3 instance;

        private Variant3() {
        }

        public static Variant3 getInstance() {
            if (instance == null) {
                synchronized (Variant3.class) {
                    if (instance == null) {
                        instance = new Variant3();
                    }
                }
            }
            return instance;
        }
    }

    public static void main(String[] args) {
        Variant1 v11 = Variant1.getInstance();
        Variant1 v12 = Variant1.getInstance();
        Variant2 v21 = Variant2.getInstance();
        Variant2 v22 = Variant2.getInstance();
        Variant3 v31 = Variant3.getInstance();
        Variant3 v32 = Variant3.getInstance();

        System.out.println(v11 + " " + v12);
        System.out.println(v21 + " " + v22);
        System.out.println(v31 + " " + v32);
    }
}