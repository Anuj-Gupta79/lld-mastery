package com.lld.problems.paymentgateway.code.core;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.lld.problems.paymentgateway.code.constants.PaymentMethod;
import com.lld.problems.paymentgateway.code.constants.PaymentStatus;
import com.lld.problems.paymentgateway.code.constants.RefundReason;
import com.lld.problems.paymentgateway.code.exceptions.FraudCheckException;
import com.lld.problems.paymentgateway.code.exceptions.InsufficientBalanceException;
import com.lld.problems.paymentgateway.code.exceptions.TransactionNotFoundException;
import com.lld.problems.paymentgateway.code.models.Customer;
import com.lld.problems.paymentgateway.code.models.Merchant;
import com.lld.problems.paymentgateway.code.models.PaymentProvider;
import com.lld.problems.paymentgateway.code.models.Response;
import com.lld.problems.paymentgateway.code.models.Transaction;
import com.lld.problems.paymentgateway.code.models.TransactionKey;
import com.lld.problems.paymentgateway.code.strategy.CardStrategy;
import com.lld.problems.paymentgateway.code.strategy.PaymentStrategy;
import com.lld.problems.paymentgateway.code.strategy.UPIStrategy;

public class PaymentGateway {
    private final ConcurrentHashMap<String, Transaction> transactions;
    private final ConcurrentHashMap<TransactionKey, String> transactionKeys;
    private final Map<PaymentMethod, PaymentStrategy> strategies;
    private final FraudChecker fraudChecker;
    private final ReentrantLock lock;

    public PaymentGateway() {
        this.transactions = new ConcurrentHashMap<>();
        this.transactionKeys = new ConcurrentHashMap<>();
        this.fraudChecker = new FraudChecker();
        this.lock = new ReentrantLock();
        this.strategies = Map.of(
                PaymentMethod.CARD, getCardStrategy(getCardProviders()),
                PaymentMethod.UPI, getUPIStrategy(getUPIProviders()));
    }

    public Response process(Customer customer, Merchant merchant, String orderId, long amount, PaymentMethod method) {
        TransactionKey key = new TransactionKey(customer.getCustomerId(), orderId);
        Transaction txn;

        this.lock.lock();

        try {
            String oldTxnId = this.transactionKeys.get(key);

            if (!Objects.isNull(oldTxnId)) {
                Transaction oldTxn = this.transactions.get(oldTxnId);

                if (Objects.equals(oldTxn.getStatus(), PaymentStatus.PENDING)
                        || Objects.equals(oldTxn.getStatus(), PaymentStatus.SUCCESS)) {
                    return Response.failed(oldTxnId, "Duplicate request: payment is already " + oldTxn.getStatus());
                }

            }

            txn = new Transaction(customer, merchant, orderId, amount, method);
            this.transactionKeys.put(key, txn.getTransactionId());
            this.transactions.put(txn.getTransactionId(), txn);

        } finally {
            this.lock.unlock();
        }

        try {
            this.fraudChecker.check(customer.getCustomerId(), amount);
        } catch (FraudCheckException e) {
            return failedTransaction(txn, String.valueOf(e.getMessage()));
        }

        if (customer.getAccount().getBalance() < amount) {
            return failedTransaction(txn, "Insufficient balance");
        }

        PaymentStatus status = this.strategies.get(method).pay(txn);

        if (Objects.equals(status, PaymentStatus.FAILED)) {
            String reason = Objects.isNull(txn.getFailureReason()) ? "Provider failed the payment"
                    : txn.getFailureReason();
            return failedTransaction(txn, reason);
        }

        if (Objects.equals(status, PaymentStatus.PENDING)) {
            return Response.pending(txn.getTransactionId(), amount);
        }

        return completePayment(txn);
    }

    public Transaction getTransaction(String transactionId) {
        if (this.transactions.containsKey(transactionId)) {
            return this.transactions.get(transactionId);
        }

        throw new TransactionNotFoundException(
                "[Error Get Transaction] Transaction not found with id = " + transactionId);
    }

    public void handleWebhook(String transactionId, PaymentStatus result) {
        if (!Objects.equals(PaymentStatus.SUCCESS, result) && !Objects.equals(PaymentStatus.FAILED, result)) {
            throw new IllegalArgumentException("[Error Handle WebHook] Invalid result = " + result);
        }

        this.lock.lock();

        try {
            Transaction txn = getTransaction(transactionId);

            if (!Objects.equals(txn.getStatus(), PaymentStatus.PENDING)) {
                return;
            }

            if (Objects.equals(result, PaymentStatus.SUCCESS)) {
                completePayment(txn);
            } else {
                failedTransaction(txn, "Provider failed Payment!");
            }
        } finally {
            this.lock.unlock();
        }

    }

    public Response refund(String transactionId, RefundReason reason) {

        this.lock.lock();
        try {
            Transaction txn = getTransaction(transactionId);

            if (!Objects.equals(PaymentStatus.SUCCESS, txn.getStatus())) {
                return Response.failed(transactionId, "Cannot refund as transaction is not successful!");
            }

            boolean fullRefund = Objects.equals(reason, RefundReason.ORDER_FAILED)
                    || !LocalDateTime.now().isAfter(txn.getPaidAt().plusHours(24));

            // half of an odd amount rounds down by 1 (for example 1001 gives 500)
            long refundedAmount = fullRefund ? txn.getAmount() : txn.getAmount() / 2;

            try {
                txn.getMerchant().getAccount().debit(refundedAmount);
            } catch (InsufficientBalanceException e) {
                return Response.failed(transactionId, String.valueOf(e.getMessage()));
            }

            txn.getCustomer().getAccount().credit(refundedAmount);

            txn.updateRefundedAmount(refundedAmount);
            txn.updateRefundTime();
            txn.updateStatus(PaymentStatus.REFUNDED);

            return Response.refunded(transactionId, refundedAmount, txn.getRefundTime());
        } finally {
            this.lock.unlock();
        }

    }

    private Response completePayment(Transaction txn) {
        try {
            txn.getCustomer().getAccount().debit(txn.getAmount());
        } catch (InsufficientBalanceException e) {
            return failedTransaction(txn, String.valueOf(e.getMessage()));
        }

        txn.getMerchant().getAccount().credit(txn.getAmount());
        txn.updatePaidAt();
        txn.updateStatus(PaymentStatus.SUCCESS);

        return Response.success(txn.getTransactionId(), txn.getAmount());
    }

    private Response failedTransaction(Transaction txn, String reason) {
        txn.updateFailureReason(reason);
        txn.updateStatus(PaymentStatus.FAILED);

        return Response.failed(txn.getTransactionId(), reason);
    }

    private PaymentStrategy getCardStrategy(List<PaymentProvider> providers) {
        return new CardStrategy(providers);
    }

    private PaymentStrategy getUPIStrategy(List<PaymentProvider> providers) {
        return new UPIStrategy(providers);
    }

    private List<PaymentProvider> getCardProviders() {
        return List.of(new PaymentProvider("HDFC", PaymentStatus.SUCCESS),
                new PaymentProvider("ICICI", PaymentStatus.SUCCESS), new PaymentProvider("SBI", PaymentStatus.SUCCESS));
    }

    private List<PaymentProvider> getUPIProviders() {
        return List.of(new PaymentProvider("PhonePe", PaymentStatus.PENDING),
                new PaymentProvider("PayTm", PaymentStatus.PENDING),
                new PaymentProvider("GooglePay", PaymentStatus.PENDING));
    }
}
