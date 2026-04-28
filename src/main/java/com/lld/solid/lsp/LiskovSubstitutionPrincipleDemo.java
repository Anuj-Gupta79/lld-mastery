package com.lld.solid.lsp;

class ShapeViolation {
    public double area() {
        return 0;
    }
}

class RectangleViolation extends ShapeViolation {
    private double width;
    private double height;

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public double area() {
        return width * height;
    }
}

// Violation: Why? Because SquareViolation does not adhere to the expected
// behavior of RectangleViolation. It overrides the setWidth and setHeight
// methods in a way that breaks the expected behavior of a rectangle, which can
// lead to incorrect area calculations when treated as a RectangleViolation.
class SquareViolation extends RectangleViolation {
    private double side;

    @Override
    public void setWidth(double width) {
        this.side = width;
    }

    @Override
    public void setHeight(double height) {
        this.side = height;
    }

    @Override
    public double area() {
        return side * side;
    }
}

// Fix: To adhere to the Liskov Substitution Principle, we can create a common
// abstract class or interface for shapes that defines the area method, and then
// have both RectangleFix and SquareFix implement this interface without
// overriding methods in a way that breaks expected behavior.
abstract class ShapeFix {
    abstract double area();
}

class RectangleFix extends ShapeFix {
    private double width;
    private double height;

    RectangleFix(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public double area() {
        return width * height;
    }
}

class SquareFix extends ShapeFix {
    private double side;

    SquareFix(double side) {
        this.side = side;
    }

    @Override
    public double area() {
        return side * side;
    }
}

public class LiskovSubstitutionPrincipleDemo {

    static void testRectangleArea(RectangleViolation rectangle) {
        rectangle.setWidth(10);
        rectangle.setHeight(5);
        System.out.println("Rectangle Area: " + rectangle.area());
    }

    static void demonstrateViolation() {
        RectangleViolation rectangle = new RectangleViolation();
        testRectangleArea(rectangle);

        // LEARNING: This will cause unexpected behavior because SquareViolation does
        // not adhere to the expected behavior of RectangleViolation.
        // WHY: LSP requires subclasses to be substitutable for their parent without
        // changing program correctness. Square breaks Rectangle's contract silently.
        SquareViolation square = new SquareViolation();
        testRectangleArea(square);

    }

    static void demonstrateFix() {
        RectangleFix rectangle = new RectangleFix(10, 5);
        System.out.println("Rectangle Area: " + rectangle.area());

        // LEARNING: This works correctly because SquareFix adheres to the expected
        // behavior of ShapeFix.
        SquareFix square = new SquareFix(5);
        System.out.println("Square Area: " + square.area());
    }

    public static void main(String[] args) {
        System.out.println("Demonstrating Liskov Substitution Principle Violation:");
        demonstrateViolation();

        System.out.println("\nDemonstrating Liskov Substitution Principle Fix:");
        demonstrateFix();
    }
}
