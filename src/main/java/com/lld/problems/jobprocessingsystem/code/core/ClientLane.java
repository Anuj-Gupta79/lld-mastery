package com.lld.problems.jobprocessingsystem.code.core;

import java.util.ArrayDeque;
import java.util.Deque;

import com.lld.problems.jobprocessingsystem.code.models.Job;

// Not thread-safe on its own. JobProcessor's lock protects every call.
public class ClientLane {
    private final Deque<Job> pending;
    private boolean busy;

    public ClientLane() {
        this.pending = new ArrayDeque<>();
        this.busy = false;
    }

    public void addLast(Job job) {
        this.pending.addLast(job);
    }

    public void addFirst(Job job) {
        this.pending.addFirst(job);
    }

    public Job peek() {
        return this.pending.peek();
    }

    public Job poll() {
        return this.pending.poll();
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public boolean isBusy() {
        return this.busy;
    }

    public void remove(Job job) {
        this.pending.remove(job);
    }

    public boolean isEmpty() {
        return this.pending.isEmpty();
    }
}
