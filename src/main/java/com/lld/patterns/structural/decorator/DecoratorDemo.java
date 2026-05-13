package com.lld.patterns.structural.decorator;

interface TextFormatter {
    String format(String text);
}

class PlainText implements TextFormatter {
    @Override
    public String format(String text) {
        return text;
    }
}

// LEARNING: The TextDecorator class implements the TextFormatter interface and contains a reference to a TextFormatter object.
// Why? This will keep the decorator in chain, else chain will break and we won't be able to apply multiple decorators in sequence.
abstract class TextDecorator implements TextFormatter {
    protected TextFormatter decoratedText;

    public TextDecorator(TextFormatter decoratedText) {
        this.decoratedText = decoratedText;
    }

    @Override
    public String format(String text) {
        return decoratedText.format(text);
    }
}

class UpperCaseDecorator extends TextDecorator {
    public UpperCaseDecorator(TextFormatter decoratedText) {
        super(decoratedText);
    }

    @Override
    public String format(String text) {
        return super.format(text).toUpperCase();
    }
}

class TrimDecorator extends TextDecorator {
    public TrimDecorator(TextFormatter decoratedText) {
        super(decoratedText);
    }

    @Override
    public String format(String text) {
        return super.format(text).trim();
    }
}

class ExclaimDecorator extends TextDecorator {
    public ExclaimDecorator(TextFormatter decoratedText) {
        super(decoratedText);
    }

    @Override
    public String format(String text) {
        return super.format(text) + "!";
    }
}

// LEARNING: Decorator pattern allows us to add behavior to objects dynamically without affecting other objects of the same class.
// WHY? It promotes code reusability and flexibility by allowing us to create new functionality by combining existing decorators without modifying the original classes.
public class DecoratorDemo {
    public static void main(String[] args) {
        // LEARNING: Order of decorators matters as they are applied in sequence.
        TextFormatter plainText = new PlainText();
        TextFormatter upperCaseText = new UpperCaseDecorator(plainText);
        TextFormatter trimmedText = new TrimDecorator(upperCaseText);
        TextFormatter exclaimText = new ExclaimDecorator(trimmedText);

        String result = exclaimText.format("  hello world  ");
        System.out.println(result);
    }
}
