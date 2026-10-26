package com.lld.problems.ratelimiter.code;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import com.lld.problems.ratelimiter.code.constants.CallerType;
import com.lld.problems.ratelimiter.code.core.RateLimiter;
import com.lld.problems.ratelimiter.code.models.LimitRule;
import com.lld.problems.ratelimiter.code.models.Response;

public class Main {

    // A clock that only moves when we say so.
    static class MutableClock extends Clock {
        private volatile Instant current;

        MutableClock(String start) {
            this.current = Instant.parse(start);
        }

        void advanceSeconds(long seconds) {
            this.current = this.current.plusSeconds(seconds);
        }

        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return this.current;
        }
    }

    static void hit(String label, RateLimiter limiter, String id, CallerType type) {
        Response r = limiter.process(id, type);
        System.out.println(label + " -> "
                + (r.isAllowed() ? "ALLOWED" : "REJECTED retry=" + r.getRetryAfterSeconds()));
    }

    static void expectError(String label, Runnable action) {
        try {
            action.run();
            System.out.println(label + " -> NO ERROR");
        } catch (RuntimeException e) {
            System.out.println(label + " -> " + e.getClass().getSimpleName());
        }
    }

    public static void main(String[] args) throws Exception {
        // S1: basic limit and window slide. FREE = 3 per 10 s
        MutableClock c1 = new MutableClock("2026-01-01T12:00:00Z");
        RateLimiter l1 = new RateLimiter(c1);
        l1.updateRule(CallerType.FREE, new LimitRule(3, 10));
        hit("S1 t=0", l1, "u1", CallerType.FREE);
        c1.advanceSeconds(1);
        hit("S1 t=1", l1, "u1", CallerType.FREE);
        c1.advanceSeconds(1);
        hit("S1 t=2", l1, "u1", CallerType.FREE);
        c1.advanceSeconds(1);
        hit("S1 t=3", l1, "u1", CallerType.FREE);
        c1.advanceSeconds(7);
        hit("S1 t=10 first", l1, "u1", CallerType.FREE);
        hit("S1 t=10 second", l1, "u1", CallerType.FREE);

        // S2: the boundary burst case. FREE = 3 per 10 s
        MutableClock c2 = new MutableClock("2026-01-01T12:04:59Z");
        RateLimiter l2 = new RateLimiter(c2);
        l2.updateRule(CallerType.FREE, new LimitRule(3, 10));
        for (int i = 1; i <= 3; i++) {
            hit("S2 12:04:59 #" + i, l2, "u2", CallerType.FREE);
        }
        c2.advanceSeconds(2);
        for (int i = 1; i <= 3; i++) {
            hit("S2 12:05:01 #" + i, l2, "u2", CallerType.FREE);
        }
        c2.advanceSeconds(8);
        hit("S2 12:05:09", l2, "u2", CallerType.FREE);

        // S3: callers are separate. FREE = 3 per 10 s
        MutableClock c3 = new MutableClock("2026-01-01T12:00:00Z");
        RateLimiter l3 = new RateLimiter(c3);
        l3.updateRule(CallerType.FREE, new LimitRule(3, 10));
        for (int i = 1; i <= 4; i++) {
            hit("S3 u3 #" + i, l3, "u3", CallerType.FREE);
        }
        hit("S3 u4 #1", l3, "u4", CallerType.FREE);

        // S4: different limit per type. PAID = 5 per 10 s
        MutableClock c4 = new MutableClock("2026-01-01T12:00:00Z");
        RateLimiter l4 = new RateLimiter(c4);
        l4.updateRule(CallerType.PAID, new LimitRule(5, 10));
        for (int i = 1; i <= 6; i++) {
            hit("S4 #" + i, l4, "u5", CallerType.PAID);
        }

        // S5: admin changes the rule while running
        MutableClock c5 = new MutableClock("2026-01-01T12:00:00Z");
        RateLimiter l5 = new RateLimiter(c5);
        l5.updateRule(CallerType.FREE, new LimitRule(3, 10));
        for (int i = 1; i <= 4; i++) {
            hit("S5 before update #" + i, l5, "u6", CallerType.FREE);
        }
        l5.updateRule(CallerType.FREE, new LimitRule(5, 10));
        for (int i = 1; i <= 3; i++) {
            hit("S5 after update #" + i, l5, "u6", CallerType.FREE);
        }

        // S6: no rule set for the type, so the default rule (10 per 5 s) is used
        MutableClock c6 = new MutableClock("2026-01-01T12:00:00Z");
        RateLimiter l6 = new RateLimiter(c6);
        for (int i = 1; i <= 11; i++) {
            hit("S6 #" + i, l6, "u7", CallerType.PAID);
        }

        // S7: the cleaner must not remove an active caller. FREE = 3 per 10 s
        MutableClock c7 = new MutableClock("2026-01-01T12:00:00Z");
        RateLimiter l7 = new RateLimiter(c7);
        l7.updateRule(CallerType.FREE, new LimitRule(3, 10));
        for (int i = 1; i <= 3; i++) {
            hit("S7 t=0 #" + i, l7, "u8", CallerType.FREE);
        }
        c7.advanceSeconds(5);
        l7.removeIdleCallers();
        hit("S7 t=5 after cleaner", l7, "u8", CallerType.FREE);
        c7.advanceSeconds(6);
        l7.removeIdleCallers();
        hit("S7 t=11 after cleaner", l7, "u8", CallerType.FREE);

        // S8: 20 threads, same caller, same moment. PAID = 5 per 10 s
        MutableClock c8 = new MutableClock("2026-01-01T12:00:00Z");
        RateLimiter l8 = new RateLimiter(c8);
        l8.updateRule(CallerType.PAID, new LimitRule(5, 10));
        ExecutorService pool = Executors.newFixedThreadPool(20);
        CountDownLatch go = new CountDownLatch(1);
        AtomicInteger allowed = new AtomicInteger();
        AtomicInteger rejected = new AtomicInteger();
        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            futures.add(pool.submit(() -> {
                try {
                    go.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                if (l8.process("u9", CallerType.PAID).isAllowed()) {
                    allowed.incrementAndGet();
                } else {
                    rejected.incrementAndGet();
                }
            }));
        }
        go.countDown();
        for (Future<?> f : futures) {
            f.get();
        }
        pool.shutdown();
        System.out.println("S8 allowed=" + allowed.get() + " rejected=" + rejected.get());

        // S9: invalid input
        RateLimiter l9 = new RateLimiter(new MutableClock("2026-01-01T12:00:00Z"));
        expectError("S9 null callerId", () -> l9.process(null, CallerType.FREE));
        expectError("S9 null callerType", () -> l9.process("x", null));
        expectError("S9 maxRequests 0", () -> new LimitRule(0, 10));
        expectError("S9 windowSeconds 0", () -> new LimitRule(3, 0));
        expectError("S9 null clock", () -> new RateLimiter(null));
    }
}