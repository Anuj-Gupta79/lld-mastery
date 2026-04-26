package com.lld.solid.ocp;

public class NoDiscount implements DiscountStrategy {

    @Override
    public double apply(double price) {
        return price;
    }
    
}
