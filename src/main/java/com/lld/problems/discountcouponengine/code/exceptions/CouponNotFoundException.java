package com.lld.problems.discountcouponengine.code.exceptions;

public class CouponNotFoundException extends RuntimeException {
    public CouponNotFoundException(String message) {
        super(message);
    }
}
