package com.lld.solid.srp;

public class EmployeeRepository {
    
    // LEARNING: Give EmployeeRepository class the single responsibility of saving employee data to the database.
    public void saveToDatabase(Employee employee) {
        System.out.println("Saving employee " + employee.getName() + " to the database.");
    }
}
