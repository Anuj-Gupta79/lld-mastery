package com.lld.problems.jobprocessingsystem.code.core;

import java.time.Instant;
import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;

import com.lld.problems.jobprocessingsystem.code.models.Job;

// Not thread-safe on its own. JobProcessor's lock protects every call.
public class ReadyQueue {
    private final PriorityQueue<Job> delayed;
    private final PriorityQueue<Job> ready;

    public ReadyQueue() {
        this.delayed = new PriorityQueue<>(
                Comparator.comparing(Job::getRunAt));

        this.ready = new PriorityQueue<>(
                Comparator
                        .comparingInt(
                                (Job job) -> job.getPriority().getValue())
                        .reversed()
                        .thenComparingLong(Job::getSequence));
    }

    public void add(Job job) {
        this.delayed.add(job);
    }

    public boolean remove(Job job) {
        return this.delayed.remove(job) || this.ready.remove(job);
    }

    public Job pollDue(Instant now) {
        while (!this.delayed.isEmpty() && !this.delayed.peek().getRunAt().isAfter(now)) {
            this.ready.add(this.delayed.poll());
        }
        return this.ready.poll();
    }

    public Instant nextRun() {
        Job next = this.delayed.peek();
        return Objects.isNull(next) ? null : next.getRunAt();
    }
}
