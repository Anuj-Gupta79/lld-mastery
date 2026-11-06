package com.lld.problems.paymentgateway.code.strategy;

import java.util.List;

import com.lld.problems.paymentgateway.code.constants.PaymentStatus;
import com.lld.problems.paymentgateway.code.models.PaymentProvider;
import com.lld.problems.paymentgateway.code.models.Transaction;

public abstract class ProviderBasedStrategy implements PaymentStrategy {
    private final List<PaymentProvider> providers;

    protected ProviderBasedStrategy(List<PaymentProvider> providers) {
        this.providers = providers;
    }

    @Override
    public PaymentStatus pay(Transaction transaction) {
        for (PaymentProvider provider : this.providers) {
            if (provider.isActive()) {
                return provider.charge(transaction.getAmount());
            }
        }

        transaction.updateFailureReason("No active provider!");
        return PaymentStatus.FAILED;
    }

    public List<PaymentProvider> getProviders() {
        return this.providers;
    }
}
