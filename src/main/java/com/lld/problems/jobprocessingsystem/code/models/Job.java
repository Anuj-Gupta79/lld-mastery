package com.lld.problems.jobprocessingsystem.code.models;

import java.time.Instant;
import java.util.UUID;

import com.lld.problems.jobprocessingsystem.code.constants.JobPriority;
import com.lld.problems.jobprocessingsystem.code.constants.JobStatus;

public class Job {
    private static final int MAX_RETRIES = 3;
    private final String jobId;
    private final String clientId;
    private final String content;
    private final JobTask task;
    private final JobPriority priority;
    private final Instant runAt;
    private final long sequence;
    private volatile JobStatus status;
    private volatile int retryCount;
    private volatile String result;
    private volatile String lastError;

    public Job(String clientId, String content, JobTask task, JobPriority priority, Instant runAt, long sequence) {
        this.jobId = UUID.randomUUID().toString();
        this.clientId = clientId;
        this.content = content;
        this.task = task;
        this.priority = priority;
        this.status = JobStatus.INITIATED;
        this.runAt = runAt;
        this.sequence = sequence;
        this.retryCount = 0;
    }

    public String getJobId() {
        return this.jobId;
    }

    public String getClientId() {
        return this.clientId;
    }

    public String getContent() {
        return this.content;
    }

    public JobTask getTask() {
        return this.task;
    }

    public JobPriority getPriority() {
        return this.priority;
    }

    public JobStatus getStatus() {
        return this.status;
    }

    public Instant getRunAt() {
        return this.runAt;
    }

    public long getSequence() {
        return this.sequence;
    }

    public int getRetryCount() {
        return this.retryCount;
    }

    public String getResult() {
        return this.result;
    }

    public String getLastError() {
        return this.lastError;
    }

    public boolean hasRetryLeft() {
        return this.retryCount < MAX_RETRIES;
    }

    public void updateRetryCount() {
        this.retryCount++;
    }

    public void updateResult(String result) {
        this.result = result;
    }

    public void updateLastError(String error) {
        this.lastError = error;
    }

    public void updateJobStatus(JobStatus status) {
        this.status = status;
    }
}
