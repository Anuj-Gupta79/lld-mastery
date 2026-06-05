package com.lld.patterns.structural.adapter;

interface PaymentProcessor {
    void pay(double amount);
}

class BankApi {
    public void makeTransaction(double amount) {
        System.out.println("Bank processing: ₹" + amount);
    }
}

// LEARNING: Adapter wraps an incompatible class (BankApi) and exposes the
// interface the client expects (PaymentProcessor).
// WHY: Client code stays unchanged; only the adapter bridges the method name
// mismatch.
class PaymentAdapter implements PaymentProcessor {
    private BankApi bankApi;

    public PaymentAdapter(BankApi bankApi) {
        this.bankApi = bankApi;
    }

    @Override
    public void pay(double amount) {
        bankApi.makeTransaction(amount);
    }
}

public class AdapterDemo {
    public static void main(String[] args) {
        BankApi bankApi = new BankApi();
        PaymentProcessor paymentProcessor = new PaymentAdapter(bankApi);
        paymentProcessor.pay(1000);
    }
}