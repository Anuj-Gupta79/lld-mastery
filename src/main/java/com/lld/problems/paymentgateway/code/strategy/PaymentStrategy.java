package com.lld.problems.paymentgateway.code.strategy;

import com.lld.problems.paymentgateway.code.constants.PaymentStatus;
import com.lld.problems.paymentgateway.code.models.Transaction;

public interface PaymentStrategy {
    public PaymentStatus pay(Transaction transaction);
}
