package com.lld.solid.srp;

// LEARNING: Single Responsibility Principle (SRP) - A class should have only one reason to change.
public class EmployeeSRPDemo {
    public static void main(String[] args) {
        Employee employee = new Employee(1, "John Doe", 50000, 10);
        SalaryCalculator salaryCalculator = new SalaryCalculator();
        EmployeeReport employeeReport = new EmployeeReport();
        EmployeeRepository employeeRepository = new EmployeeRepository();

        int salary = salaryCalculator.calculateSalary(employee);

        System.out.println("Employee ID: " + employee.getId());
        System.out.println("Employee Name: " + employee.getName());

        // Below code is voilating SRP as Employee class is doing multiple things -
        // calculating salary, generating report, and saving to database.
        // System.out.println("Employee Salary: " + employee.calculateSalary());
        // System.out.println(employee.generateReport());
        // employee.saveToDatabase();

        // LEARNING: Now, each responsibility is handled by separate classes, adhering
        // to SRP.
        System.out.println("Employee Salary: " + salary);
        System.out.println(employeeReport.generateReport(employee, salary));
        employeeRepository.saveToDatabase(employee);
    }
}
