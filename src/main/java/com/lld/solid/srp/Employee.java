package com.lld.solid.srp;

public class Employee {
    private int id;
    private String name;
    private int basedSalary;
    private int hoursWorked;

    public Employee(int id, String name, int basedSalary, int hoursWorked) {
        this.id = id;
        this.name = name;
        this.basedSalary = basedSalary;
        this.hoursWorked = hoursWorked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBasedSalary() {
        return basedSalary;
    }

    public void setBasedSalary(int basedSalary) {
        this.basedSalary = basedSalary;
    }

    public int getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(int hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    // LEARNING: Single Responsibility Principle (SRP) - A class should have only
    // one reason to change.
    // EARLIER: Employee class had multiple responsibilities - calculating salary,
    // generating report, and saving to database.
    // NOW: Each responsibility is moved to its own class, adhering to SRP.
    // This code is commented out to demonstrate the refactoring for SRP. The
    // responsibilities are now handled by separate classes.

    // public int calculateSalary() {
    // return basedSalary + (hoursWorked * 2000);
    // }

    // public String generateReport() {
    // return "Employee Report: " + name + " (ID: " + id + ") - Pay: " +
    // calculateSalary();
    // }

    // public void saveToDatabase() {
    // System.out.println("Saving employee " + name + " to the database.");
    // }
}
