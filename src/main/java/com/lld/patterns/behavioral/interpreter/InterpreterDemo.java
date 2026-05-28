package com.lld.patterns.behavioral.interpreter;

import java.util.Map;

class Context {
    private Map<String, Object> variables;

    public Context(Map<String, Object> variables) {
        this.variables = variables;
    }

    public Object getValue(String name) {
        return variables.get(name);
    }
}

// LEARNING: Interpreter pattern says that you can represent a grammar as a
// class hierarchy and use it to interpret sentences in that grammar.
interface Expression {
    boolean interpret(Context context);
}

class GreaterThanExpression implements Expression {
    private String key;
    private int threshold;

    public GreaterThanExpression(String key, int threshold) {
        this.key = key;
        this.threshold = threshold;
    }

    @Override
    public boolean interpret(Context context) {
        // LEARNING: context.getValue returns Object, so we need to cast it to Integer
        // before comparing with threshold.
        return (Integer) context.getValue(key) > threshold;
    }
}

class EqualToExpression implements Expression {
    private String key;
    private Object value;

    public EqualToExpression(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean interpret(Context context) {
        return context.getValue(key).equals(value);
    }
}

class AndExpression implements Expression {
    private Expression expr1;
    private Expression expr2;

    // LEARNING: AndExpression holds two expression so that any expression including
    // or/and can be represented as a tree of expressions.
    public AndExpression(Expression expr1, Expression expr2) {
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public boolean interpret(Context context) {
        return expr1.interpret(context) && expr2.interpret(context);
    }
}

class OrExpression implements Expression {
    private Expression expr1;
    private Expression expr2;

    // LEARNING: OrExpression holds two expression so that any expression including
    // or/and can be represented as a tree of expressions.
    public OrExpression(Expression expr1, Expression expr2) {
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public boolean interpret(Context context) {
        return expr1.interpret(context) || expr2.interpret(context);
    }
}

public class InterpreterDemo {
    public static void main(String[] args) {
        Context context1 = new Context(Map.of("age", 25, "country", "IN", "isPremium", true, "orderCount", 7));

        Expression ageGreaterThan20 = new GreaterThanExpression("age", 20);
        Expression countryIsIn = new EqualToExpression("country", "IN");
        Expression isPremiumUser = new EqualToExpression("isPremium", true);
        Expression orderCountGreaterThan5 = new GreaterThanExpression("orderCount", 5);

        Expression andExpression = new AndExpression(ageGreaterThan20, countryIsIn);
        Expression orExpression = new OrExpression(isPremiumUser, orderCountGreaterThan5);
        boolean andResult = andExpression.interpret(context1);
        boolean orResult = orExpression.interpret(context1);
        System.out.println("Does the context satisfy the AND expression? " + andResult);
        System.out.println("Does the context satisfy the OR expression? " + orResult);

        Context context2 = new Context(Map.of("age", 18, "country", "US", "isPremium", false, "orderCount", 3));
        boolean andResult2 = andExpression.interpret(context2);
        boolean orResult2 = orExpression.interpret(context2);
        System.out.println("Does the context satisfy the AND expression? " + andResult2);
        System.out.println("Does the context satisfy the OR expression? " + orResult2);
    }
}
