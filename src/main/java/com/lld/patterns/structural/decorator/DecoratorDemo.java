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

// LEARNING: Abstract decorator implements the interface and holds a reference
// to another TextFormatter.
// WHY: The wrapped reference keeps the chain intact — each decorator delegates
// to the one below it.
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

public class DecoratorDemo {
    public static void main(String[] args) {
        // LEARNING: Decorators wrap inward — outermost runs first, delegates down,
        // result bubbles back up.
        TextFormatter pipeline = new ExclaimDecorator(
                new TrimDecorator(
                        new UpperCaseDecorator(
                                new PlainText())));

        System.out.println(pipeline.format("  hello world  "));
    }
}