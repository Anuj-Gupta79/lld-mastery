package com.lld.oops.ObjectMethods;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class Employee {
    private String name;
    private int id;
    private int salary;

    public Employee(String name, int id, int salary) {
        this.name = name;
        this.id = id;
        this.salary = salary;
    }

    // LEARNING: toString() gives a human-readable representation of the object.
    // WHY: Without it, printing an object shows class name + hash code — useless
    // for debugging.
    @Override
    public String toString() {
        return "Employee Name: " + name + ", Employee ID: " + id + ", Employee Salary: " + salary;
    }

    // LEARNING: equals() defines what "same employee" means — here, same name +
    // same id.
    // WHY: Default equals() checks reference equality (same memory address), not
    // logical equality.
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Employee employee = (Employee) obj;
        return id == employee.id && Objects.equals(name, employee.name);
    }

    // LEARNING: hashCode() must be consistent with equals() — same logical object =
    // same hash.
    // WHY: HashSet/HashMap use hash first to find the bucket, then equals() to
    // confirm. Break this contract and duplicates sneak in.
    @Override
    public int hashCode() {
        int result = Objects.hashCode(name);
        result = 31 * result + id;
        return result;
    }
}

public class ObjectMethodsDemo {

    public static void main(String[] args) {
        Employee emp1 = new Employee("Alice", 101, 50000);
        Employee emp2 = new Employee("Alice", 101, 60000);

        System.out.println("Employee 1: " + emp1);
        System.out.println("Employee 2: " + emp2);
        System.out.println("------------------------------------------");

        System.out.println("Are emp1 and emp2 equal? " + emp1.equals(emp2));

        Set<Employee> employeeSet = new HashSet<>();
        employeeSet.add(emp1);
        employeeSet.add(emp2);

        // emp1 and emp2 have same name+id → same hash → same bucket → equals() confirms
        // duplicate → set stays size 1
        System.out.println("Number of unique employees in the set: " + employeeSet.size());

        // LEARNING: Objects.equals(a, b) is null-safe — handles null without throwing
        // NPE.
        // WHY: a.equals(b) throws NPE if a is null. Fields from a DB can legitimately
        // be null.
        Employee emp3 = new Employee(null, 101, 55000);
        System.out.println("Is emp1 equal to emp3? " + emp1.equals(emp3));
        System.out.println("Is emp3 equal to emp1? " + emp3.equals(emp1));
    }
}