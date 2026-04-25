package com.lld.solid.srp;

public class SalaryCalculator {

    // LEARNING: Give SalaryCalculator class the single responsibility of calculating employee salaries.
    public int calculateSalary(Employee employee) {
        return employee.getBasedSalary() + (employee.getHoursWorked() * 2000);
    }
}
