package com.lld.problems.ratelimiter.code.models;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.locks.ReentrantLock;

public class CallerState {
    private final String callerId;
    private final Deque<Instant> timestamps;
    private final ReentrantLock lock;

    public CallerState(String id) {
        this.callerId = id;
        this.timestamps = new ArrayDeque<>();
        this.lock = new ReentrantLock();
    }

    public String getCallerId() {
        return this.callerId;
    }

    public void addTimeStamp(Instant instant) {
        this.timestamps.add(instant);
    }

    public void removeOlderThan(Instant boundary) {
        while (!this.timestamps.isEmpty() && !this.timestamps.peekFirst().isAfter(boundary)) {
            this.timestamps.removeFirst();
        }
    }

    public ReentrantLock getLock() {
        return this.lock;
    }

    public int getCount() {
        return this.timestamps.size();
    }

    public Instant getOldest() {
        return this.timestamps.peekFirst();
    }

    public Instant getNewest(){
        return this.timestamps.peekLast();
    }
}
