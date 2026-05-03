package com.lld.patterns.creational.singleton;

public class SingletonDemo {

    // Variant1 is eager initialization, which is thread-safe but may lead to
    // resource wastage if the instance is never used.
    // Why is Variant1 thread-safe?
    // 1. The instance is created at the time of class loading, which is guaranteed
    // to be thread-safe by the Java ClassLoader.
    // 2. The getInstance() method simply returns the already created instance, so
    // there are no synchronization issues when accessing it.
    // 3. But it may lead to resource wastage if the instance is never used, as it
    // is created regardless of whether it is needed or not.
    static class Variant1 {
        // LEARNING: instance is created at class loading time, which is eager
        // initialization
        // WHY: this is thread-safe because class loading is synchronized by the JVM,
        // but it may lead to resource wastage if the instance is never used, as it is
        // created regardless of whether it is needed or not.
        private static final Variant1 instance = new Variant1();

        private Variant1() {
        }

        public static Variant1 getInstance() {
            return instance;
        }
    }

    // Variant2 is lazy initialization, which is not thread-safe and can lead to
    // multiple instances if accessed by multiple threads simultaneously.
    // Why is Variant2 not thread-safe?
    // 1. If two or more threads call the getInstance() method at the same time,
    // they may both find that the instance variable is null and create separate
    // instances of the Variant2 class, violating the singleton pattern.
    // 2. The lack of synchronization in the getInstance() method allows multiple
    // threads to access it simultaneously, which can lead to race conditions and
    // inconsistent behavior when creating the singleton instance.
    static class Variant2 {

        // LEARNING: instance is not initialized until getInstance() is called, which is
        // lazy initialization
        // WHY: this can save memory and improve startup time if the instance is never
        // used, but it is not thread-safe and can lead to multiple instances if
        // accessed by multiple threads simultaneously.
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

    // Variant3 is double-checked locking, which is thread-safe and efficient, but
    // requires the instance variable to be volatile to prevent instruction
    // reordering issues.
    // Why use double-checked locking?
    // 1. To improve performance by reducing the overhead of acquiring a lock every
    // time the getInstance() method is called.
    // 2. To ensure thread safety while creating the singleton instance, preventing
    // multiple instances from being created in a multi-threaded environment.
    // 3. To provide a lazy initialization of the singleton instance, allowing it to
    // be created only when it is needed, which can save memory and improve startup
    // time.
    static class Variant3 {
        // LEARNING: volatile ensures all threads read from main memory, not CPU cache
        // WHY: without this, a thread may see stale null even after instance is created
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

    // LEARNINGS:
    // Singleton pattern ensures that a class has only one instance and provides a
    // global point of access to it.
    // Why use Singleton pattern?
    // 1. To control access to a shared resource, such as a database connection or
    // a configuration manager.
    // 2. To ensure that only one instance of a class is created, which can save
    // memory and improve performance.
    // 3. To provide a global point of access to an instance, which can simplify
    // code and reduce coupling between classes.
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
