package com.lld.solid.ocp;

public class LoyaltyDiscount implements DiscountStrategy {

    @Override
    public double apply(double price) {
        return price - (0.1 * price); // 10% discount
    }
    
}
