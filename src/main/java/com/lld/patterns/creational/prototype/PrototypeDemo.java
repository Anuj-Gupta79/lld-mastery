package com.lld.patterns.creational.prototype;

import java.util.ArrayList;
import java.util.List;

// LEARNING: Prototype creates new objects by cloning an existing instance instead of constructing from scratch.
// WHY: Useful when object setup is expensive or when many objects share most of their state.
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

    // LEARNING: Deep copy of mutable fields — new ArrayList ensures clone's
    // subjects list is independent.
    // WHY: Shallow copy would share the same list reference; mutation in clone
    // would affect original.
    @Override
    public StudentTemplate clone() {
        try {
            StudentTemplate clone = (StudentTemplate) super.clone();
            clone.subjects = new ArrayList<>(this.subjects);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public String getId() {
        return id;
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
        StudentTemplate originalStudent = new StudentTemplate("1", "Alice", "10th Grade", "Springfield High",
                List.of("Math", "Science", "History"));

        // LEARNING: Clone copies shared state; only differing fields are overwritten.
        StudentTemplate clonedStudent = originalStudent.clone();
        clonedStudent.setId("2");
        clonedStudent.setName("Bob");
        clonedStudent.getSubjects().add("Art");

        System.out.println("Original Student: " + originalStudent);
        System.out.println("Cloned Student: " + clonedStudent);
    }
}