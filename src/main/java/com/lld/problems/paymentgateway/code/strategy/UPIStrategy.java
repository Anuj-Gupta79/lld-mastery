package com.lld.problems.paymentgateway.code.strategy;

import java.util.List;

import com.lld.problems.paymentgateway.code.models.PaymentProvider;

public class UPIStrategy extends ProviderBasedStrategy {

    public UPIStrategy(List<PaymentProvider> providers) {
        super(providers);
    }
}
