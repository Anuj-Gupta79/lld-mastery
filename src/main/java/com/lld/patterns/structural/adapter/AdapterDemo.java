package com.lld.patterns.structural.adapter;

// Target interface
interface PaymentProcessor {
    void pay(double amount);
}

// Adaptee class
class BankApi {
    public void makeTransaction(double amount) {
        System.out.println("Bank processing: ₹" + amount);
    }
}

// Adapter class
class PaymentAdapter implements PaymentProcessor {
    // The adapter holds a reference to the adaptee (BankApi) and implements the
    // target interface (PaymentProcessor). It translates the pay method call into a
    // call to the makeTransaction method of the BankApi, allowing the client code
    // to use the PaymentProcessor interface while still utilizing the functionality
    // of the BankApi. This way, the Adapter pattern enables the integration of
    // incompatible interfaces without modifying the existing code of the BankApi,
    // adhering to the principle of being open for extension but closed for
    // modification
    private BankApi bankApi;

    public PaymentAdapter(BankApi bankApi) {
        this.bankApi = bankApi;
    }

    @Override
    public void pay(double amount) {
        bankApi.makeTransaction(amount);
    }
}

// LEARNING: The Adapter pattern allows incompatible interfaces to work
// together. In this example, the PaymentAdapter class adapts the BankApi to the
// PaymentProcessor interface, enabling the client code to use the pay method
// without needing to know about the underlying BankApi implementation.
// When the main method is executed, it creates an instance of PaymentAdapter
// and calls the pay method, which internally calls the BankApi's pay method to
// process the payment. This demonstrates how the Adapter pattern helps in
// integrating different systems or components that have incompatible
// interfaces, allowing them to work together seamlessly.
public class AdapterDemo {
    public static void main(String[] args) {
        BankApi bankApi = new BankApi();
        PaymentProcessor paymentProcessor = new PaymentAdapter(bankApi);
        paymentProcessor.pay(1000);
    }
}