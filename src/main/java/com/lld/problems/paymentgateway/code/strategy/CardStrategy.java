package com.lld.problems.paymentgateway.code.strategy;

import java.util.List;

import com.lld.problems.paymentgateway.code.models.PaymentProvider;

public class CardStrategy extends ProviderBasedStrategy {

    public CardStrategy(List<PaymentProvider> providers) {
        super(providers);
    }
}
