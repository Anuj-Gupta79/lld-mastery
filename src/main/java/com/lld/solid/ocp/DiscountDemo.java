package com.lld.solid.ocp;

public class DiscountDemo {

    public static void main(String[] args) {
        DiscountCalculator discount = new DiscountCalculator();
        System.out.println("Seasonal Discount: " + discount.applyDiscount(100, new SeasonalDiscount()));
        System.out.println("Loyalty Discount: " + discount.applyDiscount(100, new LoyaltyDiscount()));
        System.out.println("Clearance Discount: " + discount.applyDiscount(100, new ClearanceDiscount()));
        System.out.println("No Discount: " + discount.applyDiscount(100, new NoDiscount()));
    }
}
