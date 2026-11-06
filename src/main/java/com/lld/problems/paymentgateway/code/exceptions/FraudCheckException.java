package com.lld.problems.paymentgateway.code.exceptions;

public class FraudCheckException extends RuntimeException {
    public FraudCheckException(String message){
        super(message);
    }
}
