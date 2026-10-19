package com.lld.problems.discountcouponengine.code.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.lld.problems.discountcouponengine.code.exceptions.CouponAlreadyExistsException;

public class Result {
    private double originalAmount;
    private double finalAmount;
    private List<Coupon> appliedCoupons;
    private List<Coupon> skippedCoupons;

    public Result(double originalAmount) {
        this.originalAmount = originalAmount;
        this.finalAmount = originalAmount;
        this.appliedCoupons = new ArrayList<>();
        this.skippedCoupons = new ArrayList<>();
    }

    public void addAppliedCoupon(Coupon coupon) {
        if (this.appliedCoupons.contains(coupon)) {
            throw new CouponAlreadyExistsException(
                    "[Error Add Applied Coupon] Coupon is already applied.");
        }

        this.appliedCoupons.add(coupon);
    }

    public void addSkippedCoupon(Coupon coupon) {
        if (this.skippedCoupons.contains(coupon)) {
            throw new CouponAlreadyExistsException(
                    "[Error Add Skipped Coupon] Coupon is already skipped.");
        }

        this.skippedCoupons.add(coupon);
    }

    public void setFinalAmount(double amount) {
        this.finalAmount = amount;
    }

    public double getOriginalAmount() {
        return this.originalAmount;
    }

    public double getFinalAmount() {
        return this.finalAmount;
    }

    public double getSavedAmount() {
        return this.originalAmount - this.finalAmount;
    }

    public List<Coupon> getAppliedCoupons() {
        return Collections.unmodifiableList(this.appliedCoupons);
    }

    public List<Coupon> getSkippedCoupons() {
        return Collections.unmodifiableList(this.skippedCoupons);
    }
}
