package com.lld.problems.paymentgateway.code;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import com.lld.problems.paymentgateway.code.constants.PaymentMethod;
import com.lld.problems.paymentgateway.code.constants.PaymentStatus;
import com.lld.problems.paymentgateway.code.constants.RefundReason;
import com.lld.problems.paymentgateway.code.core.PaymentGateway;
import com.lld.problems.paymentgateway.code.exceptions.TransactionNotFoundException;
import com.lld.problems.paymentgateway.code.models.Account;
import com.lld.problems.paymentgateway.code.models.Customer;
import com.lld.problems.paymentgateway.code.models.Merchant;
import com.lld.problems.paymentgateway.code.models.Response;

public class Main {
    private static int wrong = 0;

    public static void main(String[] args) throws InterruptedException {
        PaymentGateway gateway = new PaymentGateway();

        System.out.println("--- S1 card success ---");
        Customer c1 = newCustomer(10000);
        Merchant m1 = newMerchant();
        Response r1 = gateway.process(c1, m1, "O1", 2000, PaymentMethod.CARD);
        expect("S1 status", r1.getStatus(), PaymentStatus.SUCCESS);
        expect("S1 customer", c1.getAccount().getBalance(), 8000L);
        expect("S1 merchant", m1.getAccount().getBalance(), 2000L);

        System.out.println("--- S2 duplicate ---");
        Response r2 = gateway.process(c1, m1, "O1", 2000, PaymentMethod.CARD);
        expect("S2 status", r2.getStatus(), PaymentStatus.FAILED);
        expect("S2 message", r2.getMessage(), "Duplicate request: payment is already SUCCESS");
        expect("S2 same id", r2.getTransactionId(), r1.getTransactionId());
        expect("S2 customer", c1.getAccount().getBalance(), 8000L);
        expect("S2 merchant", m1.getAccount().getBalance(), 2000L);

        System.out.println("--- S3 insufficient balance, then retry ---");
        Customer c2 = newCustomer(500);
        Merchant m2 = newMerchant();
        Response a = gateway.process(c2, m2, "O2", 1000, PaymentMethod.CARD);
        expect("S3 first message", a.getMessage(), "Insufficient balance");
        expect("S3 txn status", gateway.getTransaction(a.getTransactionId()).getStatus(), PaymentStatus.FAILED);
        Response b = gateway.process(c2, m2, "O2", 1000, PaymentMethod.CARD);
        expect("S3 retry message", b.getMessage(), "Insufficient balance");
        expect("S3 ids differ", !a.getTransactionId().equals(b.getTransactionId()), true);
        expect("S3 balance", c2.getAccount().getBalance(), 500L);

        System.out.println("--- S4 amount above limit ---");
        Customer c3 = newCustomer(100000);
        Merchant m3 = newMerchant();
        Response r4 = gateway.process(c3, m3, "O3", 60000, PaymentMethod.CARD);
        expect("S4 status", r4.getStatus(), PaymentStatus.FAILED);
        expect("S4 message", r4.getMessage(), "[Error Fraud Check] Amount is above the limit!");
        expect("S4 balance", c3.getAccount().getBalance(), 100000L);

        System.out.println("--- S5 too many requests ---");
        Customer c4 = newCustomer(100000);
        Merchant m4 = newMerchant();
        PaymentStatus[] expected5 = { PaymentStatus.SUCCESS, PaymentStatus.SUCCESS, PaymentStatus.SUCCESS,
                PaymentStatus.FAILED };
        for (int i = 0; i < 4; i++) {
            Response r = gateway.process(c4, m4, "A" + i, 100, PaymentMethod.CARD);
            expect("S5 request " + (i + 1), r.getStatus(), expected5[i]);
            if (i == 3) {
                expect("S5 message", r.getMessage().startsWith("[Error Fraud Check]"), true);
            }
        }
        expect("S5 customer", c4.getAccount().getBalance(), 99700L);
        expect("S5 merchant", m4.getAccount().getBalance(), 300L);

        System.out.println("--- S6 UPI pending, webhook, webhook again ---");
        Customer c5 = newCustomer(5000);
        Merchant m5 = newMerchant();
        Response p = gateway.process(c5, m5, "U1", 1000, PaymentMethod.UPI);
        expect("S6 status", p.getStatus(), PaymentStatus.PENDING);
        expect("S6 customer", c5.getAccount().getBalance(), 5000L);
        expect("S6 merchant", m5.getAccount().getBalance(), 0L);
        Response pd = gateway.process(c5, m5, "U1", 1000, PaymentMethod.UPI);
        expect("S6 duplicate message", pd.getMessage(), "Duplicate request: payment is already PENDING");
        gateway.handleWebhook(p.getTransactionId(), PaymentStatus.SUCCESS);
        expect("S6 after webhook", gateway.getTransaction(p.getTransactionId()).getStatus(), PaymentStatus.SUCCESS);
        expect("S6 customer", c5.getAccount().getBalance(), 4000L);
        expect("S6 merchant", m5.getAccount().getBalance(), 1000L);
        gateway.handleWebhook(p.getTransactionId(), PaymentStatus.SUCCESS);
        expect("S6 webhook twice customer", c5.getAccount().getBalance(), 4000L);
        expect("S6 webhook twice merchant", m5.getAccount().getBalance(), 1000L);

        System.out.println("--- S7 UPI webhook failed, retry ---");
        Customer c6 = newCustomer(5000);
        Merchant m6 = newMerchant();
        Response u2 = gateway.process(c6, m6, "U2", 1000, PaymentMethod.UPI);
        expect("S7 first", u2.getStatus(), PaymentStatus.PENDING);
        gateway.handleWebhook(u2.getTransactionId(), PaymentStatus.FAILED);
        expect("S7 status", gateway.getTransaction(u2.getTransactionId()).getStatus(), PaymentStatus.FAILED);
        expect("S7 reason", gateway.getTransaction(u2.getTransactionId()).getFailureReason(),
                "Provider failed Payment!");
        expect("S7 customer", c6.getAccount().getBalance(), 5000L);
        expect("S7 merchant", m6.getAccount().getBalance(), 0L);
        Response retry = gateway.process(c6, m6, "U2", 1000, PaymentMethod.UPI);
        expect("S7 retry status", retry.getStatus(), PaymentStatus.PENDING);
        expect("S7 retry new id", !retry.getTransactionId().equals(u2.getTransactionId()), true);
        boolean invalid = false;
        try {
            gateway.handleWebhook(retry.getTransactionId(), PaymentStatus.PENDING);
        } catch (IllegalArgumentException e) {
            invalid = true;
        }
        expect("S7 invalid webhook", invalid, true);

        System.out.println("--- S8 refunds ---");
        Customer c7 = newCustomer(10000);
        Merchant m7 = newMerchant();
        Response pay1 = gateway.process(c7, m7, "R1", 1000, PaymentMethod.CARD);
        expect("S8 paid", pay1.getStatus(), PaymentStatus.SUCCESS);
        Response rf1 = gateway.refund(pay1.getTransactionId(), RefundReason.ORDER_FAILED);
        expect("S8 status", rf1.getStatus(), PaymentStatus.REFUNDED);
        expect("S8 refund amount", rf1.getRefundAmount(), 1000L);
        expect("S8 refund time set", rf1.getRefundTime() != null, true);
        expect("S8 txn status", gateway.getTransaction(pay1.getTransactionId()).getStatus(), PaymentStatus.REFUNDED);
        expect("S8 customer", c7.getAccount().getBalance(), 10000L);
        expect("S8 merchant", m7.getAccount().getBalance(), 0L);
        Response rf1b = gateway.refund(pay1.getTransactionId(), RefundReason.ORDER_FAILED);
        expect("S8 second refund", rf1b.getMessage(), "Cannot refund as transaction is not successful!");
        expect("S8 customer after second", c7.getAccount().getBalance(), 10000L);
        Response pay2 = gateway.process(c7, m7, "R2", 1001, PaymentMethod.CARD);
        Response rf2 = gateway.refund(pay2.getTransactionId(), RefundReason.CUSTOMER_CANCEL);
        expect("S8 cancel within 24h", rf2.getRefundAmount(), 1001L);
        expect("S8 customer", c7.getAccount().getBalance(), 10000L);
        expect("S8 merchant", m7.getAccount().getBalance(), 0L);

        System.out.println("--- S9 merchant cannot pay the refund ---");
        Customer c8 = newCustomer(10000);
        Merchant m8 = newMerchant();
        Response pay3 = gateway.process(c8, m8, "R3", 1000, PaymentMethod.CARD);
        m8.getAccount().debit(1000); // merchant spent the money
        Response rf3 = gateway.refund(pay3.getTransactionId(), RefundReason.CUSTOMER_CANCEL);
        expect("S9 message", rf3.getMessage(), "[Error Debit] Insufficient Balance!");
        expect("S9 txn status", gateway.getTransaction(pay3.getTransactionId()).getStatus(), PaymentStatus.SUCCESS);
        expect("S9 customer", c8.getAccount().getBalance(), 9000L);

        System.out.println("--- S10 five threads, same order ---");
        Customer c9 = newCustomer(10000);
        Merchant m9 = newMerchant();
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failedCount = new AtomicInteger();
        CountDownLatch start = new CountDownLatch(1);
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Thread t = new Thread(() -> {
                try {
                    start.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                Response res = gateway.process(c9, m9, "C1", 1000, PaymentMethod.CARD);
                if (res.getStatus() == PaymentStatus.SUCCESS) {
                    successCount.incrementAndGet();
                } else {
                    failedCount.incrementAndGet();
                }
            }, "pay-thread-" + i);
            threads.add(t);
            t.start();
        }
        start.countDown();
        for (Thread t : threads) {
            t.join();
        }
        expect("S10 success count", successCount.get(), 1);
        expect("S10 failed count", failedCount.get(), 4);
        expect("S10 customer", c9.getAccount().getBalance(), 9000L);
        expect("S10 merchant", m9.getAccount().getBalance(), 1000L);

        System.out.println("--- S11 unknown id ---");
        boolean notFound = false;
        try {
            gateway.getTransaction("no-such-id");
        } catch (TransactionNotFoundException e) {
            notFound = true;
        }
        expect("S11 not found", notFound, true);

        System.out.println("\nWrong lines: " + wrong);
    }

    private static Customer newCustomer(long balance) {
        return new Customer(new Account("ACC-" + System.nanoTime(), balance));
    }

    private static Merchant newMerchant() {
        return new Merchant(new Account("MACC-" + System.nanoTime(), 0));
    }

    private static void expect(String label, Object actual, Object expected) {
        boolean ok = Objects.equals(actual, expected);
        if (!ok) {
            wrong++;
        }
        System.out.println(
                (ok ? "OK    " : "WRONG ") + label + " -> " + actual + (ok ? "" : " (expected " + expected + ")"));
    }
}