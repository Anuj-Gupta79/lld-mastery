package com.lld.problems.ratelimiter.code.models;

import com.lld.problems.ratelimiter.code.exceptions.InvalidRuleException;

public class LimitRule {
    private final int maxRequests;
    private final int windowSeconds;

    public LimitRule(int maxRequests, int windowSeconds) {
        if (maxRequests <= 0) {
            throw new InvalidRuleException("[Error Rule Creation] No rule is allowed with maxRequests <= 0!");
        }

        if (windowSeconds <= 0) {
            throw new InvalidRuleException("[Error Rule Creation] No rule is allowed with windowSeconds <= 0!");
        }

        this.maxRequests = maxRequests;
        this.windowSeconds = windowSeconds;
    }

    public int getMaxRequests() {
        return this.maxRequests;
    }

    public int getWindowSeconds() {
        return this.windowSeconds;
    }
}
