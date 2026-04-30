package com.lld.solid.srp;

// SRP says: "A class should have only one reason to change." In this example, we have an Employee class that is responsible for multiple things - calculating salary, generating reports, and saving to a database. This violates SRP because if we need to change the way salary is calculated, we would also risk breaking the report generation or database saving functionality. By separating these responsibilities into different classes (SalaryCalculator, EmployeeReport, EmployeeRepository), we adhere to SRP and make our code more maintainable and easier to understand.    
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
