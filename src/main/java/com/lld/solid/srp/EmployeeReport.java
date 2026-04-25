package com.lld.solid.srp;

public class EmployeeReport {
    // LEARNING: Give EmployeeReport class the single responsibility of generating
    // reports for employees.
    public String generateReport(Employee employee, int salary) {
        return "Employee Report: " + employee.getName() + " (ID: " + employee.getId() + ") - Pay: " + salary;
    }
}
