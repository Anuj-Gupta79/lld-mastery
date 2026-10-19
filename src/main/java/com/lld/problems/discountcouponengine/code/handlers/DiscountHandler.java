package com.lld.problems.discountcouponengine.code.handlers;

import java.util.Objects;

import com.lld.problems.discountcouponengine.code.models.Coupon;
import com.lld.problems.discountcouponengine.code.models.Result;
import com.lld.problems.discountcouponengine.code.strategy.DiscountStrategy;

public class DiscountHandler {
    private Coupon coupon;
    private DiscountStrategy strategy;
    private DiscountHandler next;

    public DiscountHandler(Coupon coupon, DiscountStrategy strategy) {
        this.coupon = coupon;
        this.strategy = strategy;
    }

    public void handle(double runningPrice, Result result) {
        double discountedAmount = strategy.apply(runningPrice, coupon);
        double outgoingPrice = runningPrice;

        if (discountedAmount > runningPrice) {
            result.addSkippedCoupon(coupon);
        } else {
            result.addAppliedCoupon(coupon);
            outgoingPrice = runningPrice - discountedAmount;
        }

        if (Objects.isNull(next)) {
            result.setFinalAmount(outgoingPrice);
        } else {
            this.next.handle(outgoingPrice, result);
        }

    }

    public void setNext(DiscountHandler nextHandler) {
        this.next = nextHandler;
    }
}
