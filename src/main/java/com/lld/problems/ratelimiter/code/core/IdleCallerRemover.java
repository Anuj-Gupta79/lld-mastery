package com.lld.problems.ratelimiter.code.core;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class IdleCallerRemover {
    private final RateLimiter limiter;
    private final long intervalSeconds;
    private final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    public IdleCallerRemover(RateLimiter limiter, long intervalSeconds) {
        this.limiter = limiter;
        this.intervalSeconds = intervalSeconds;
    }

    public void start() {
        this.service.scheduleAtFixedRate(() -> this.limiter.removeIdleCallers(), intervalSeconds, intervalSeconds,
                TimeUnit.SECONDS);
    }

    public void stop() {
        this.service.shutdown();
    }
}
