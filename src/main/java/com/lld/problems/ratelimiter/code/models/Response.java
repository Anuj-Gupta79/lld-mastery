package com.lld.problems.ratelimiter.code.models;

public class Response {
    private final boolean allowed;
    private final String message;
    private final Long retryAfterSeconds;

    private Response(boolean allowed, String message, Long retryAfterSeconds) {
        this.allowed = allowed;
        this.message = message;
        this.retryAfterSeconds = retryAfterSeconds;
    }

    public static Response allowed() {
        return new Response(true, "Status: 200, Success", null);
    }

    public static Response rejected(String message, long retryAfterSeconds) {
        return new Response(false, message, retryAfterSeconds);
    }

    public boolean isAllowed() {
        return this.allowed;
    }

    public String getMessage() {
        return this.message;
    }

    public Long getRetryAfterSeconds() {
        return this.retryAfterSeconds;
    }
}
