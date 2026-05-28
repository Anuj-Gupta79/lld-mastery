package com.lld.patterns.behavioral.visitor;

import java.util.List;

interface IncomeSource {
    void accept(Visitor visitor);
}

class SalaryIncome implements IncomeSource {
    private double amount;

    public SalaryIncome(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public void accept(Visitor visitor) {
        // LEARNING: If we call it outside of this class, we would have to check the type of source and cast it to SalaryIncome before calling visit, which is not ideal.
        visitor.visit(this);
    }
}

class RentalIncome implements IncomeSource {
    private double amount;

    public RentalIncome(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

class FreelanceIncome implements IncomeSource {
    private double amount;

    public FreelanceIncome(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

interface Visitor {
    // LEARNING: have 3 visit methods, one for each type of IncomeSource.
    // Why: This allows Visitor to perform different operations based on the type of
    // IncomeSource it is visiting.
    void visit(SalaryIncome source);

    void visit(RentalIncome source);

    void visit(FreelanceIncome source);
}

class TaxCalculatorVisitor implements Visitor {
    private double totalTax;

    // LEARNING: Each source has different requirement,
    // Why: Passing them individually resolve the problem of having to check type of
    // source.
    @Override
    public void visit(SalaryIncome source) {
        totalTax += source.getAmount() * 0.2;
    }

    @Override
    public void visit(RentalIncome source) {
        totalTax += source.getAmount() * 0.15;
    }

    @Override
    public void visit(FreelanceIncome source) {
        totalTax += source.getAmount() * 0.25;
    }

    public double getTotalTax() {
        return totalTax;
    }
}

class IncomeSummaryVisitor implements Visitor {
    @Override
    public void visit(SalaryIncome source) {
        System.out.println("Salary Income: " + source.getAmount());
    }

    @Override
    public void visit(RentalIncome source) {
        System.out.println("Rental Income: " + source.getAmount());
    }

    @Override
    public void visit(FreelanceIncome source) {
        System.out.println("Freelance Income: " + source.getAmount());
    }
}

public class VisitorDemo {
    public static void main(String[] args) {
        List<IncomeSource> incomeSources = List.of(
                new SalaryIncome(50000),
                new RentalIncome(20000),
                new FreelanceIncome(15000));

        TaxCalculatorVisitor taxCalculator = new TaxCalculatorVisitor();
        IncomeSummaryVisitor incomeSummary = new IncomeSummaryVisitor();

        for (IncomeSource source : incomeSources) {
            source.accept(taxCalculator);
            source.accept(incomeSummary);
        }

        System.out.println("Total Tax: " + taxCalculator.getTotalTax());
    }
}
