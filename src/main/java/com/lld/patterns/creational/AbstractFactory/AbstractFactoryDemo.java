package com.lld.patterns.creational.AbstractFactory;

interface Button {
    void render();
}

interface Checkbox {
    void render();
}

// LEARNING: Abstract Factory defines creation methods for a family of related
// products.
// WHY: Caller gets a whole consistent family (Button + Checkbox) without
// knowing concrete classes.
interface UIComponentFactory {
    Button createButton();

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

public class AbstractFactoryDemo {

    // LEARNING: Client only depends on UIComponentFactory interface, never on
    // WindowsUIFactory/MacOSUIFactory.
    // WHY: Swapping the entire product family (OS theme) requires zero client code
    // change.
    static void renderUI(UIComponentFactory factory) {
        Button button = factory.createButton();
        Checkbox checkbox = factory.createCheckbox();
        button.render();
        checkbox.render();
    }

    public static void main(String[] args) {
        System.out.println("Rendering Windows UI:");
        renderUI(new WindowsUIFactory());
        System.out.println("\nRendering MacOS UI:");
        renderUI(new MacOSUIFactory());
    }
}