package com.lld.solid.ocp;

public class ClearanceDiscount implements DiscountStrategy {

    @Override
    public double apply(double price) {
        return price - (0.5 * price); // 50% discount
    }
}