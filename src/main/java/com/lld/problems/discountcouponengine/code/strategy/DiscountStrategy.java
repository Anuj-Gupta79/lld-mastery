package com.lld.problems.discountcouponengine.code.strategy;

import com.lld.problems.discountcouponengine.code.models.Coupon;

public interface DiscountStrategy {
    public double apply(double runningPrice, Coupon coupon);
}
