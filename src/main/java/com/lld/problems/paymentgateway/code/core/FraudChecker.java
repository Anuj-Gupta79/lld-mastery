package com.lld.problems.paymentgateway.code.core;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lld.problems.paymentgateway.code.exceptions.FraudCheckException;

public class FraudChecker {
    private static final long MAX_AMOUNT = 50000;
    private static final int MAX_REQUESTS = 3;
    private final Map<String, Deque<LocalDateTime>> recentRequests = new ConcurrentHashMap<>();

    public void check(String customerId, long amount) {

        if (amount > MAX_AMOUNT) {
            throw new FraudCheckException("[Error Fraud Check] Amount is above the limit!");
        }

        LocalDateTime now = LocalDateTime.now();

        Deque<LocalDateTime> times = this.recentRequests.computeIfAbsent(customerId, id -> new ArrayDeque<>());

        synchronized (times) {
            while (!times.isEmpty() && times.peekFirst().isBefore(now.minusMinutes(1))) {
                times.pollFirst();
            }

            if (times.size() >= MAX_REQUESTS) {
                throw new FraudCheckException("[Error Fraud Check] Too many requests in one minute!");
            }

            times.addLast(now);
        }
    }
}
