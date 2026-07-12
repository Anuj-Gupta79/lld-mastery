package com.lld.problems.elevator.code.models;

public class Door {
    private boolean isOpen;

    public Door() {
        this.isOpen = false;
    }

    public void close() {
        this.isOpen = false;
        System.out.println("Closing the door!");
    }

    public void open() {
        this.isOpen = true;
        System.out.println("Opening the door!");
    }

    public boolean isOpen() {
        return this.isOpen;
    }
}
