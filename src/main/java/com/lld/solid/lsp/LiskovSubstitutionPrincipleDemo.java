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

// LEARNING: A subclass must preserve the expectations established by its
// parent.
// WHY: If replacing the parent with the child changes behaviour, LSP is
// violated.
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

// LEARNING: Model abstractions around common behaviour, not forced inheritance.
// WHY: Square and Rectangle both have area(), but neither should pretend to be
// the other.
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

        // LEARNING: SquareViolation cannot safely replace RectangleViolation.
        // WHY: Rectangle expects width and height to vary independently.
        SquareViolation square = new SquareViolation();
        testRectangleArea(square);
    }

    static void demonstrateFix() {
        RectangleFix rectangle = new RectangleFix(10, 5);
        System.out.println("Rectangle Area: " + rectangle.area());

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