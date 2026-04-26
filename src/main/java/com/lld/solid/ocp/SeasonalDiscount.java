package com.lld.solid.ocp;

public class SeasonalDiscount implements DiscountStrategy {

    @Override
    public double apply(double price) {
        return price - (0.2 * price); // 20% discount
    }
}