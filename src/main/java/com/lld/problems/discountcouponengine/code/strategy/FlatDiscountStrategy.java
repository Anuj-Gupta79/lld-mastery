package com.lld.problems.discountcouponengine.code.strategy;

import com.lld.problems.discountcouponengine.code.models.Coupon;

public class FlatDiscountStrategy implements DiscountStrategy {

    @Override
    public double apply(double runningPrice, Coupon coupon) {
        return coupon.getValue();
    }

}
