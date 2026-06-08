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

    // LEARNING: Employee now represents only employee data.
    // WHY: Changes to salary, reporting, or persistence should not force changes
    // here.

    // Earlier SRP violation:
    // calculateSalary()
    // generateReport()
    // saveToDatabase()
}

class EmployeeReport {

    // LEARNING: Responsible only for report generation.
    public String generateReport(Employee employee, int salary) {
        return "Employee Report: " + employee.getName() + " (ID: " + employee.getId() + ") - Pay: " + salary;
    }
}

class EmployeeRepository {

    // LEARNING: Responsible only for persistence operations.
    public void saveToDatabase(Employee employee) {
        System.out.println("Saving employee " + employee.getName() + " to the database.");
    }
}

class SalaryCalculator {

    // LEARNING: Responsible only for salary calculation.
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

        // WHY: Employee should not calculate salary, generate reports,
        // and save itself to the database.

        System.out.println("Employee Salary: " + salary);
        System.out.println(employeeReport.generateReport(employee, salary));
        employeeRepository.saveToDatabase(employee);
    }
}