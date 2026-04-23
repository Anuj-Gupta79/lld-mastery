package com.lld.oops;

import java.util.List;

// LEARNING: Generics in Java allow you to create classes, interfaces, and methods that can operate on any type of data. This promotes code reusability and type safety. 
// In this example, we have a generic class Pair that can hold two different types of data (T and V). We also have methods to find the maximum element in an array, calculate the average of an array of numbers, and print all elements of a list using wildcards.
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
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }

    @SuppressWarnings("hiding")
    public <T extends Comparable<T>> T findMax(T[] array) {
        T max = array[0];
        for (T element : array) {
            if (element.compareTo(max) > 0) {
                max = element;
            }
        }
        return max;
    }

    public <T extends Number> double calculateAverage(T[] array) {
        if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty");
        }
        double sum = 0;
        for (T element : array) {
            sum += element.doubleValue();
        }
        return sum / array.length;
    }

    public void printAll(List<?> list) {
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
        Pair<String, Integer> pair3 = new Pair<>("Max", 0);
        Integer maxInt = pair3.findMax(intArray);
        double averageInt = pair3.calculateAverage(intArray);
        System.out.println("Average integer: " + averageInt);
        System.out.println("Max integer: " + maxInt);

        String[] strArray = { "apple", "banana", "cherry" };
        Pair<String, Integer> pair4 = new Pair<>("Max", 0);
        String maxStr = pair4.findMax(strArray);
        System.out.println("Max string: " + maxStr);

        Double[] doubleArray = { 1.5, 2.5, 3.5 };
        Pair<String, Double> pair5 = new Pair<>("Average", 0.0);
        double average = pair5.calculateAverage(doubleArray);
        System.out.println("Average: " + average);

        List<String> stringList = List.of("one", "two", "three");
        pair5.printAll(stringList);

        List<Integer> intList = List.of(1, 2, 3);
        pair5.printAll(intList);

    }
}
