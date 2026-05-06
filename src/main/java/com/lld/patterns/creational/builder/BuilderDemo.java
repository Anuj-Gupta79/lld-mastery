package com.lld.patterns.creational.builder;

// LEARNING: Builder Design Pattern is a creational design pattern that allows for the step-by-step construction of complex objects. It separates the construction of an object from its representation, allowing the same construction process to create different representations. The Builder pattern is particularly useful when an object has many optional parameters or when the construction process involves multiple steps.
// Why use Builder Design Pattern?
// 1. It provides a clear and fluent API for constructing complex objects.
// 2. It allows for the creation of immutable objects, as the builder can enforce immutability by only providing setter methods in the builder and not in the final object.
// 3. It helps to avoid the telescoping constructor anti-pattern, where a class has multiple constructors with different combinations of parameters, which can be confusing and error-prone.
// 4. It promotes separation of concerns by separating the construction logic from the representation of the object, making the code easier to maintain and understand.
class Student {
    // LEARNING: We have define attributes.
    // Why? Because we want to make the Student class immutable. Once a
    // Student object is created, its state cannot be changed. This is achieved by
    // declaring all attributes as final and providing only getter methods (if
    // needed) without any setter methods. The Builder pattern allows us to set the
    // values of these attributes during the construction process, but once the
    // Student object is built, its state remains unchanged.
    private final String id;
    private final String name;
    private final String email;
    private final String mobileNumber1;
    private final String mobileNumber2;
    private final String mobileNumber3;

    // LEARNING: The constructor is private to prevent direct instantiation of the
    // Student class. This ensures that the only way to create a Student object is
    // through the Builder, which enforces the immutability and allows for a
    // controlled construction process.
    private Student(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.email = builder.email;
        this.mobileNumber1 = builder.mobileNumber1;
        this.mobileNumber2 = builder.mobileNumber2;
        this.mobileNumber3 = builder.mobileNumber3;
    }

    // LEARNING: The Builder class is a static.
    // Why? Because it allows us to create instances of the Builder without needing
    // an instance of the Student class. This is important because the Builder is
    // responsible for constructing the Student object, and we want to be able to
    // use it independently of any existing Student instances. By making the Builder
    // class static, we can easily create a new Builder instance and use it to build
    // a Student object without any dependencies on the Student class itself.
    static class Builder {
        private String id;
        private String name;
        private String email;
        private String mobileNumber1;
        private String mobileNumber2;
        private String mobileNumber3;

        // LEARNING: The Builder constructor takes the required parameters (id and name)
        // and initializes them. This ensures that any Student object created using the
        // Builder will have these required attributes set, while the optional
        // attributes can be set using the setter methods provided in the Builder.
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
