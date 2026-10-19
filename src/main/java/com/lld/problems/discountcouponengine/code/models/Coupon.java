package com.lld.problems.discountcouponengine.code.models;

import java.util.Objects;
import java.util.UUID;

import com.lld.problems.discountcouponengine.code.constants.DiscountType;
import com.lld.problems.discountcouponengine.code.exceptions.InvalidCouponException;

public class Coupon {
    private String couponId;
    private String name;
    private DiscountType discountType;
    private double value;
    private Double cap;
    private double minCartValue;

    public Coupon(String name, DiscountType discountType, double value, Double cap, double minCartValue) {
        if (isValidCoupon(discountType, cap, value, minCartValue)) {
            this.couponId = UUID.randomUUID().toString();
            this.name = name;
            this.discountType = discountType;
            this.value = value;
            this.cap = cap;
            this.minCartValue = minCartValue;
        } else {
            throw new InvalidCouponException("[Error Coupon Creation] Coupon is not valid");
        }

    }

    public String getCouponId() {
        return this.couponId;
    }

    public DiscountType getDiscountType() {
        return this.discountType;
    }

    public String getName() {
        return this.name;
    }

    public double getValue() {
        return this.value;
    }

    public Double getCap() {
        return this.cap;
    }

    public double getMinCartValue() {
        return this.minCartValue;
    }

    private static boolean isValidCoupon(DiscountType discountType, Double cap, double value, double minCartValue) {
        if (Objects.isNull(discountType) || value < 0 || minCartValue < 0) {
            return false;
        }

        switch (discountType) {
            case FLAT:
                return true;
            case PERCENTAGE:
                return value <= 100;
            case PERCENTAGE_WITH_CAP:
                return !Objects.isNull(cap) && cap > 0 && value <= 100;
            default:
                return false;
        }
    }
}
