package com.lld.patterns.behavioral.strategy;

// LEARNING: interface gives context a generic type — new strategies plug in without changing TextProcessor
interface FormattingStrategy {
    String format(String content);
}

// Concrete Strategy
class UpperCaseStrategy implements FormattingStrategy {

    @Override
    public String format(String content) {
        return content.toUpperCase();
    }
}

// Concrete Strategy
class LowerCaseStrategy implements FormattingStrategy {

    @Override
    public String format(String content) {
        return content.toLowerCase();
    }
}

// Concrete Strategy
class TitleCaseStrategy implements FormattingStrategy {

    @Override
    public String format(String content) {
        if (content.isBlank()) {
            return content;
        }

        String[] words = content.toLowerCase().split("\\s+");
        StringBuilder sb = new StringBuilder();

        for (String word : words) {
            sb.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }

        return sb.toString().trim();

    }
}

// Context
class TextProcessor {
    private FormattingStrategy strategy;

    // LEARNING: Need to provide initial strategy to avoid any exception and
    // breakdown
    public TextProcessor(FormattingStrategy strategy) {
        this.strategy = strategy;
    }

    public String process(String content) {
        return this.strategy.format(content);
    }

    // LEARNING: Setter provides facility to switch strategy runtime.
    public void setStrategy(FormattingStrategy strategy) {
        this.strategy = strategy;
    }
}

public class StrategyDemo {
    public static void main(String[] args) {
        TextProcessor textProcessor = new TextProcessor(new TitleCaseStrategy());

        String formattedString1 = textProcessor.process("hi! this is the default strategy text");
        System.out.println(formattedString1);

        textProcessor.setStrategy(new UpperCaseStrategy());
        String formattedString2 = textProcessor.process("Strategy has been switched from default to upper case.");
        System.out.println(formattedString2);

        textProcessor.setStrategy(new LowerCaseStrategy());
        String formattedString3 = textProcessor.process("Strategy has been switched from upper case to LowerCase");
        System.out.println(formattedString3);
    }
}
