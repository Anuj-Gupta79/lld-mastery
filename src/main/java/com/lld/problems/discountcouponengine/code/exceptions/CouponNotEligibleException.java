package com.lld.problems.discountcouponengine.code.exceptions;

public class CouponNotEligibleException extends RuntimeException {
    public CouponNotEligibleException(String message) {
        super(message);
    }
}
