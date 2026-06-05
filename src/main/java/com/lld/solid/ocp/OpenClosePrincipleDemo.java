package com.lld.solid.ocp;

interface DiscountStrategy {
    double apply(double price);
}

class SeasonalDiscount implements DiscountStrategy {

    @Override
    public double apply(double price) {
        return price - (0.2 * price); // 20% discount
    }
}

class ClearanceDiscount implements DiscountStrategy {

    @Override
    public double apply(double price) {
        return price - (0.5 * price); // 50% discount
    }
}

class LoyaltyDiscount implements DiscountStrategy {

    @Override
    public double apply(double price) {
        return price - (0.1 * price); // 10% discount
    }

}

class NoDiscount implements DiscountStrategy {

    @Override
    public double apply(double price) {
        return price;
    }

}

class DiscountCalculator {
    public double applyDiscount(double price, DiscountStrategy strategy) {
        return strategy.apply(price);
    }
}

public class OpenClosePrincipleDemo {
    public static void main(String[] args) {
        DiscountCalculator discount = new DiscountCalculator();
        System.out.println("Seasonal Discount: " + discount.applyDiscount(100, new SeasonalDiscount()));
        System.out.println("Loyalty Discount: " + discount.applyDiscount(100, new LoyaltyDiscount()));
        System.out.println("Clearance Discount: " + discount.applyDiscount(100, new ClearanceDiscount()));
        System.out.println("No Discount: " + discount.applyDiscount(100, new NoDiscount()));
    }
}
