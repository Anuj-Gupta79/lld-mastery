package com.lld.problems.discountcouponengine.code.strategy;

import com.lld.problems.discountcouponengine.code.models.Coupon;

public class PercentageWithCapDiscountStrategy implements DiscountStrategy {

    @Override
    public double apply(double runningPrice, Coupon coupon) {
        double discountedAmount = runningPrice * coupon.getValue() / 100;
        return Math.min(discountedAmount, coupon.getCap());
    }

}
