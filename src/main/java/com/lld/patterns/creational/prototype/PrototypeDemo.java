package com.lld.patterns.creational.prototype;

import java.util.ArrayList;
import java.util.List;

// LEARNING: The Prototype pattern allows you to create new objects by copying existing ones, which can be more efficient than creating new instances from scratch, especially when the object creation process is complex or resource-intensive.
// Why use Prototype pattern?
// 1. Performance: Cloning an existing object can be faster than creating a new one, especially if the object is complex and requires a lot of setup.
// 2. Flexibility: It allows you to create new objects at runtime without knowing their exact class, as long as they implement the Cloneable interface.
// 3. Avoiding Subclassing: It can be used to avoid subclassing when you want to create objects that are similar but not identical to existing ones.
// LEARNING: We have use Cloneable interface to implement the prototype pattern.
// Java provide two types of cloning: shallow copy and deep copy. Shallow copy creates a new object but copies the references of the original object's fields, while deep copy creates a new object and also creates new instances of the fields, ensuring that changes to the cloned object do not affect the original object.
class StudentTemplate implements Cloneable {

    private String id;
    private String name;
    private String school;
    private String grade;
    private List<String> subjects;

    StudentTemplate(String id, String name, String grade, String school, List<String> subjects) {
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.school = school;
        this.subjects = subjects;
    }

    // LEARNING: We have implemented deep copy in the clone method to ensure that
    // the subjects list is not shared between the original and cloned objects.
    // Generally when object has mutable fields, we need to create a deep copy to
    // avoid sharing references.
    // Why? Because if we share references, changes to the mutable fields in the
    // cloned object will affect the original object, which can lead to unintended
    // side effects and bugs in the application.
    @Override
    public StudentTemplate clone() {
        try {
            StudentTemplate clone = (StudentTemplate) super.clone();
            // Deep copy of mutable fields
            clone.subjects = new ArrayList<>(this.subjects);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public String getName() {
        return name;
    }

    public String getGrade() {
        return grade;
    }

    public String getSchool() {
        return school;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "StudentTemplate{id='" + id + "', name='" + name + "', grade='" + grade + "', school='" + school
                + "', subjects=" + subjects + "}";
    }
}

public class PrototypeDemo {

    public static void main(String[] args) {
        // LEARNING: We have created a base student template and then cloned it to
        // create a new student with some modifications. This demonstrates how the
        // Prototype pattern allows us to create new objects by copying existing ones,
        // which can be more efficient than creating new instances from scratch,
        // especially when the object creation process is complex or resource-intensive.
        StudentTemplate originalStudent = new StudentTemplate("1", "Alice", "10th Grade", "Springfield High",
                List.of("Math", "Science", "History"));

        // Clone the original student
        StudentTemplate clonedStudent = originalStudent.clone();
        clonedStudent.setId("2");
        clonedStudent.setName("Bob");
        // LEARNING: We have modified the subjects list in the cloned student to
        // demonstrate that it does not affect the original student, which confirms that
        // we have implemented a deep copy in the clone method.
        clonedStudent.getSubjects().add("Art");

        System.out.println("Original Student: " + originalStudent);
        System.out.println("Cloned Student: " + clonedStudent);
    }
}
