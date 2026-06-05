package com.lld.solid.srp;

class Employee {
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

class EmployeeReport {
    // LEARNING: Give EmployeeReport class the single responsibility of generating
    // reports for employees.
    public String generateReport(Employee employee, int salary) {
        return "Employee Report: " + employee.getName() + " (ID: " + employee.getId() + ") - Pay: " + salary;
    }
}

class EmployeeRepository {

    // LEARNING: Give EmployeeRepository class the single responsibility of saving
    // employee data to the database.
    public void saveToDatabase(Employee employee) {
        System.out.println("Saving employee " + employee.getName() + " to the database.");
    }
}

class SalaryCalculator {

    // LEARNING: Give SalaryCalculator class the single responsibility of
    // calculating employee salaries.
    public int calculateSalary(Employee employee) {
        return employee.getBasedSalary() + (employee.getHoursWorked() * 2000);
    }
}

public class SingleResponsibilityPrincipleDemo {
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
