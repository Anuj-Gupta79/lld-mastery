package com.lld.problems.ratelimiter.code.core;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.lld.problems.ratelimiter.code.constants.CallerType;
import com.lld.problems.ratelimiter.code.exceptions.InvalidCallerStateException;
import com.lld.problems.ratelimiter.code.exceptions.InvalidCallerTypeException;
import com.lld.problems.ratelimiter.code.exceptions.InvalidClockException;
import com.lld.problems.ratelimiter.code.exceptions.InvalidRuleException;
import com.lld.problems.ratelimiter.code.models.CallerState;
import com.lld.problems.ratelimiter.code.models.LimitRule;
import com.lld.problems.ratelimiter.code.models.Response;

public class RateLimiter {
    private final ConcurrentHashMap<String, CallerState> callers;
    private final ConcurrentHashMap<CallerType, LimitRule> rules;
    private final LimitRule defaultRule;
    private final Clock clock;

    public RateLimiter(Clock clock) {
        if (Objects.isNull(clock)) {
            throw new InvalidClockException("[Error Rate Limiter Constructor] Null clock is not allowed!");
        }

        this.callers = new ConcurrentHashMap<>();
        this.rules = new ConcurrentHashMap<>();
        this.defaultRule = new LimitRule(10, 5);
        this.clock = clock;
    }

    public Response process(String callerId, CallerType callerType) {
        if (Objects.isNull(callerType)) {
            throw new InvalidCallerTypeException("[Error Process] Null CallerType is not allowed!");
        }

        if (Objects.isNull(callerId)) {
            throw new InvalidCallerStateException("[Error Process] Null CallerId is not allowed!");
        }

        CallerState state = this.callers.computeIfAbsent(callerId, id -> new CallerState(id));
        LimitRule rule = this.rules.getOrDefault(callerType, this.defaultRule);

        state.getLock().lock();
        try {
            Instant now = this.clock.instant();
            Instant boundary = now.minusSeconds(rule.getWindowSeconds());
            state.removeOlderThan(boundary);
            if (state.getCount() >= rule.getMaxRequests()) {
                Instant oldest = state.getOldest();
                long retryAfterSeconds = Duration.between(now, oldest.plusSeconds(rule.getWindowSeconds())).toSeconds();
                return Response.rejected("Status: 429, Too Many Requests", retryAfterSeconds);
            }
            state.addTimeStamp(now);
            return Response.allowed();
        } finally {
            state.getLock().unlock();
        }
    }

    public void updateRule(CallerType callerType, LimitRule rule) {
        if (Objects.isNull(callerType)) {
            throw new InvalidCallerTypeException("[Error Update Rule] Null Caller type is not allowed!");
        }

        if (Objects.isNull(rule)) {
            throw new InvalidRuleException("[Error Update Rule] Null rule is not allowed!");
        }

        this.rules.put(callerType, rule);
    }

    public void removeIdleCallers() {
        Instant now = this.clock.instant();

        long maxWindowSeconds = this.defaultRule.getWindowSeconds();
        for (LimitRule rule : this.rules.values()) {
            maxWindowSeconds = Math.max(maxWindowSeconds, rule.getWindowSeconds());
        }
        Instant boundary = now.minusSeconds(maxWindowSeconds);

        for (CallerState state : this.callers.values()) {
            state.getLock().lock();
            try {
                Instant newest = state.getNewest();
                if (newest != null && !newest.isAfter(boundary)) {
                    this.callers.remove(state.getCallerId(), state);
                }
            } finally {
                state.getLock().unlock();
            }
        }
    }
}
