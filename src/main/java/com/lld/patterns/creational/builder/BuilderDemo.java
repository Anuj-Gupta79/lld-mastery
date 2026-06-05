package com.lld.patterns.creational.builder;

// LEARNING: Builder separates object construction from representation.
// WHY: Avoids telescoping constructors; enables immutable objects with optional fields.
class Student {

    private final String id;
    private final String name;
    private final String email;
    private final String mobileNumber1;
    private final String mobileNumber2;
    private final String mobileNumber3;

    // LEARNING: Private constructor forces creation only through Builder.
    private Student(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.email = builder.email;
        this.mobileNumber1 = builder.mobileNumber1;
        this.mobileNumber2 = builder.mobileNumber2;
        this.mobileNumber3 = builder.mobileNumber3;
    }

    // LEARNING: Static nested class so Builder can be used without a Student
    // instance.
    static class Builder {
        private String id;
        private String name;
        private String email;
        private String mobileNumber1;
        private String mobileNumber2;
        private String mobileNumber3;

        // LEARNING: Required fields go in Builder constructor; optional fields get
        // setters.
        public Builder(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public Student build() {
            return new Student(this);
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setMobileNumber1(String mobileNumber1) {
            this.mobileNumber1 = mobileNumber1;
            return this;
        }

        public Builder setMobileNumber2(String mobileNumber2) {
            this.mobileNumber2 = mobileNumber2;
            return this;
        }

        public Builder setMobileNumber3(String mobileNumber3) {
            this.mobileNumber3 = mobileNumber3;
            return this;
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNumber1() {
        return mobileNumber1;
    }

    public String getMobileNumber2() {
        return mobileNumber2;
    }

    public String getMobileNumber3() {
        return mobileNumber3;
    }

    @Override
    public String toString() {
        return "Student{id='" + id + "', name='" + name + "', email='" + email + "', mobileNumber1='" + mobileNumber1
                + "', mobileNumber2='" + mobileNumber2 + "', mobileNumber3='" + mobileNumber3 + "'}";
    }
}

public class BuilderDemo {
    public static void main(String[] args) {
        Student student1 = new Student.Builder("1", "John").build();
        Student student2 = new Student.Builder("2", "Jane").setEmail("jane@example.com").build();
        Student student3 = new Student.Builder("3", "Doe").setEmail("doe@example.com").setMobileNumber1("1234567890")
                .build();

        System.out.println("Student 1: " + student1);
        System.out.println("Student 2: " + student2);
        System.out.println("Student 3: " + student3);
    }
}