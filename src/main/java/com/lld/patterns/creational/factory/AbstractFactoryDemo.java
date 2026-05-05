package com.lld.patterns.creational.factory;

interface Button {
    void render();
}

interface Checkbox {
    void render();
}

// LEARNING: Abstract Factory interface that defines methods for creating
// related
// UI components (Button and Checkbox in this case).
// WHy: This allows for the creation of families of related objects without
// specifying their concrete classes, promoting loose coupling and scalability
// in the codebase.
interface UIComponentFactory {
    // LEARNING: createButton returns type is Button instead of a specific
    // implementation, allowing for flexibility in the types of buttons that can be
    // created without changing the client code.
    Button createButton();

    // LEARNING: createCheckbox returns type is Checkbox instead of a specific
    // implementation, allowing for flexibility in the types of checkboxes that can
    // be
    // created without changing the client code.
    Checkbox createCheckbox();
}

class WindowsUIFactory implements UIComponentFactory {
    @Override
    public Button createButton() {
        return new WindowsButton();
    }

    @Override
    public Checkbox createCheckbox() {
        return new WindowsCheckbox();
    }
}

class MacOSUIFactory implements UIComponentFactory {
    @Override
    public Button createButton() {
        return new MacOSButton();
    }

    @Override
    public Checkbox createCheckbox() {
        return new MacOSCheckbox();
    }
}

class WindowsButton implements Button {
    @Override
    public void render() {
        System.out.println("Rendering Windows button...");
    }
}

class WindowsCheckbox implements Checkbox {
    @Override
    public void render() {
        System.out.println("Rendering Windows checkbox...");
    }
}

class MacOSButton implements Button {
    @Override
    public void render() {
        System.out.println("Rendering MacOS button...");
    }
}

class MacOSCheckbox implements Checkbox {
    @Override
    public void render() {
        System.out.println("Rendering MacOS checkbox...");
    }
}

// LEARNING: Abstract Factory pattern provides an interface for creating
// families of related or dependent objects without specifying their concrete
// classes.
// WHY: This allows for greater flexibility and scalability in object creation,
// as it enables the client code to work with abstract interfaces rather than
// concrete implementations, making it easier to add new families of products
// without modifying existing code.
public class AbstractFactoryDemo {

    // LEARNING: Client code that uses the abstract factory to create UI components
    // without needing to know the specific classes being instantiated.
    // WHY: This promotes loose coupling and makes it easier to switch between
    // different families of products (e.g., Windows vs. MacOS) without changing the
    // client code.
    // Here we can see demonstraction of Liskov Substitution Principle, as we can
    // substitute one factory for another without affecting the client code.
    static void renderUI(UIComponentFactory factory) {
        Button button = factory.createButton();
        Checkbox checkbox = factory.createCheckbox();
        button.render();
        checkbox.render();
    }

    public static void main(String[] args) {
        System.out.println("Rendering Windows UI:");
        // Why we can pass different factories to the same method without changing the
        // client code? This is because both factories implement the same interface
        // (UIComponentFactory), allowing us to use them interchangeably without
        // modifying the client code, demonstrating the Liskov Substitution Principle.
        renderUI(new WindowsUIFactory());
        System.out.println("\nRendering MacOS UI:");
        renderUI(new MacOSUIFactory());
    }
}
