package com.lld.oops;

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

    @Override
    public String toString() {
        return "Employee Name: " + name + ", Employee ID: " + id + ", Employee Salary: " + salary;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Employee employee = (Employee) obj;
        return id == employee.id && Objects.equals(name, employee.name);
    }

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

        // LEARNING: The toString() method is overridden in the Employee class to
        // provide a custom string representation of the object. When we print the
        // employee objects, it will call the toString() method and display the employee
        // details in a readable format.
        // Why? Because when we print an object, Java implicitly calls the toString()
        // method of that object to get its string representation. If we do not override
        // the toString() method, it will return a default string that includes the
        // class name and the object's hash code, which is not very informative. By
        // overriding the toString() method, we can provide a more meaningful and
        // human-readable representation of the object, which is especially useful for
        // debugging and logging purposes. In this case, when we print emp1 and emp2, it
        // will display their names and IDs instead of the default string
        // representation.
        System.out.println("Employee 1: " + emp1);
        System.out.println("Employee 2: " + emp2);

        System.out.println("------------------------------------------");

        // LEARNING: The equals() method is overridden in the Employee class to
        // provide a custom equality check based on the employee's name and ID. When we
        // call emp1.equals(emp2), it will compare the name and ID of both employee
        // objects to determine if they are considered equal. In this case, since emp1
        // and emp2 have the same name and ID, the equals() method will return true,
        // indicating that they are equal based on our custom definition of equality. If
        // we had not overridden the equals() method, it would have used the default
        // implementation from the Object class, which checks for reference equality
        // (i.e., whether emp1 and emp2 refer to the same object in memory), and it
        // would have returned false since emp1 and emp2 are different objects. By
        // overriding the equals() method, we can define our own criteria for equality,
        // which is more meaningful for our specific use case.
        System.out.println("Are emp1 and emp2 equal? " + emp1.equals(emp2));

        Set<Employee> employeeSet = new HashSet<>();
        employeeSet.add(emp1);
        employeeSet.add(emp2);

        // LEARNING: The hashCode() method is overridden in the Employee class to
        // provide a custom hash code based on the employee's name and ID. When we add
        // emp1 and emp2 to the HashSet, it will use the hashCode() method to determine
        // the bucket in which to store the employee objects. Since emp1 and emp2 have
        // the same name and ID, they will generate the same hash code, which means they
        // will be stored in the same bucket. The HashSet will then use the equals()
        // method to check if emp1 and emp2 are considered equal. Since our equals()
        // method considers them equal, the HashSet will not add emp2 to the set, as it
        // already contains an equal object (emp1). Therefore, the size of the
        // employeeSet will be 1, indicating that only one unique employee object is
        // stored in the set, even though we attempted to add two different objects
        // (emp1 and emp2) that are considered equal based on our custom equality
        // definition.
        System.out.println("Number of unique employees in the set: " + employeeSet.size());

        // LEARNING: Objects.equals(a, b) is null-safe — it handles the case where
        // either argument is null without throwing NullPointerException.
        // Internally it checks: (a == b) || (a != null && a.equals(b))

        // WHY: If you use a.equals(b) directly and 'a' is null, Java throws NPE
        // because you're calling a method on a null reference. In real systems,
        // fields like name can legitimately be null (e.g. data from a DB before
        // being populated). Always use Objects.equals() when comparing fields
        // inside your own equals() override.
        Employee emp3 = new Employee(null, 101, 55000);
        System.out.println("is Emp1 equal to Emp3? " + emp1.equals(emp3));
        System.out.println("is Emp3 equal to Emp1? " + emp3.equals(emp1));

    }
}
