package com.lld.oops.Generic;

import java.util.List;

// LEARNING: Generics let a class or method work on any type while keeping compile-time type safety.
// WHY: Without generics, you'd use Object everywhere and need casts — errors surface at runtime, not compile time.
class Pair<T, V> {
    private T first;
    private V second;

    public Pair(T first, V second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "Pair{first=" + first + ", second=" + second + '}';
    }
}

class GenericUtils {

    // LEARNING: <T extends Comparable<T>> — T must support compareTo(), so findMax
    // works on Integer, String, etc.
    public static <T extends Comparable<T>> T findMax(T[] array) {
        T max = array[0];
        for (T element : array) {
            if (element.compareTo(max) > 0) {
                max = element;
            }
        }
        return max;
    }

    // LEARNING: <T extends Number> — bounds T to Number subtypes so doubleValue()
    // is available on every element.
    public static <T extends Number> double calculateAverage(T[] array) {
        if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty");
        }
        double sum = 0;
        for (T element : array) {
            sum += element.doubleValue();
        }
        return sum / array.length;
    }

    // LEARNING: List<?> wildcard — accepts any List regardless of type; read-only,
    // no add allowed.
    public static void printAll(List<?> list) {
        for (Object element : list) {
            System.out.println(element);
        }
    }
}

public class GenericsDemo {

    public static void main(String[] args) {
        Pair<String, Integer> pair1 = new Pair<>("Hello", 42);
        System.out.println(pair1);

        Pair<Double, String> pair2 = new Pair<>(3.14, "Pi");
        System.out.println(pair2);

        Integer[] intArray = { 1, 2, 3, 4, 5 };
        System.out.println("Max integer: " + GenericUtils.findMax(intArray));
        System.out.println("Average integer: " + GenericUtils.calculateAverage(intArray));

        String[] strArray = { "apple", "banana", "cherry" };
        System.out.println("Max string: " + GenericUtils.findMax(strArray));

        Double[] doubleArray = { 1.5, 2.5, 3.5 };
        System.out.println("Average: " + GenericUtils.calculateAverage(doubleArray));

        GenericUtils.printAll(List.of("one", "two", "three"));
        GenericUtils.printAll(List.of(1, 2, 3));
    }
}