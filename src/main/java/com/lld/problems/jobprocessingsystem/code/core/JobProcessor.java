package com.lld.problems.jobprocessingsystem.code.core;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.lld.problems.jobprocessingsystem.code.constants.JobPriority;
import com.lld.problems.jobprocessingsystem.code.constants.JobStatus;
import com.lld.problems.jobprocessingsystem.code.exceptions.InvalidClockException;
import com.lld.problems.jobprocessingsystem.code.exceptions.InvalidJobException;
import com.lld.problems.jobprocessingsystem.code.exceptions.JobNotFoundException;
import com.lld.problems.jobprocessingsystem.code.models.Job;
import com.lld.problems.jobprocessingsystem.code.models.JobKey;
import com.lld.problems.jobprocessingsystem.code.models.JobTask;

public class JobProcessor {
    private static final int WORKER_COUNT = 10;
    private final Map<String, Job> jobs;
    private final Map<JobKey, String> activeKeys;
    private final Map<String, ClientLane> lanes;
    private final ReadyQueue readyQueue;
    private final List<Thread> workThreads;
    private final ReentrantLock lock;
    private final Condition jobReady;
    private final Clock clock;
    private long nextSequence;
    private boolean stopped;

    public JobProcessor(Clock clock) {
        if (Objects.isNull(clock)) {
            throw new InvalidClockException("[Error Job Processor Initialization] Clock can not be null!");
        }

        this.jobs = new HashMap<>();
        this.activeKeys = new HashMap<>();
        this.lanes = new HashMap<>();
        this.readyQueue = new ReadyQueue();
        this.lock = new ReentrantLock();
        this.jobReady = this.lock.newCondition();
        this.clock = clock;
        this.nextSequence = 0;
        this.stopped = false;
        this.workThreads = new ArrayList<>();

        for (int count = 1; count <= WORKER_COUNT; count++) {
            Thread thread = new Thread(new JobWorker(this), "job-worker-" + count);
            this.workThreads.add(thread);
            thread.start();
        }
    }

    public String submit(String clientId, String content, JobTask task, JobPriority priority, Instant runAt) {
        if (Objects.isNull(clientId) || Objects.isNull(content) || Objects.isNull(task)) {
            throw new InvalidJobException("[Error Submit] clientId, content and task can not be null!");
        }

        JobPriority finalPriority = Objects.isNull(priority) ? JobPriority.NORMAL : priority;

        this.lock.lock();

        try {

            if (this.stopped) {
                throw new InvalidJobException("[Error Submit] System is stopped, no new jobs accepted!");
            }

            Instant finalRunAt = Objects.isNull(runAt) ? this.clock.instant() : runAt;

            JobKey key = new JobKey(clientId, content);
            String existingJobId = this.activeKeys.get(key);

            if (!Objects.isNull(existingJobId)) {
                return existingJobId;
            }

            Job job = new Job(clientId, content, task, finalPriority, finalRunAt, this.nextSequence++);
            this.jobs.put(job.getJobId(), job);
            this.activeKeys.put(key, job.getJobId());

            ClientLane lane = this.lanes.computeIfAbsent(clientId, id -> new ClientLane());
            boolean laneWasIdle = !lane.isBusy() && lane.isEmpty();
            lane.addLast(job);

            if (laneWasIdle) {
                this.readyQueue.add(job);
                this.jobReady.signal();
            }

            return job.getJobId();
        } finally {
            this.lock.unlock();
        }
    }

    public Job takeNextJob() {
        this.lock.lock();

        try {
            while (true) {
                if (this.stopped) {
                    return null;
                }

                Instant now = this.clock.instant();
                Job job = this.readyQueue.pollDue(now);

                if (!Objects.isNull(job)) {
                    ClientLane lane = this.lanes.get(job.getClientId());
                    lane.poll();
                    lane.setBusy(true);
                    job.updateJobStatus(JobStatus.PROCESSING);
                    this.jobReady.signal();

                    return job;
                }

                Instant nextRun = this.readyQueue.nextRun();

                if (Objects.isNull(nextRun)) {
                    this.jobReady.await();
                } else {
                    this.jobReady.awaitNanos(Duration.between(now, nextRun).toNanos());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            this.lock.unlock();
        }
    }

    public void complete(Job job, String result) {
        this.lock.lock();
        try {
            job.updateResult(result);
            job.updateJobStatus(JobStatus.COMPLETED);
            this.activeKeys.remove(new JobKey(job.getClientId(), job.getContent()));

            ClientLane lane = this.lanes.get(job.getClientId());
            lane.setBusy(false);
            this.promoteHead(lane);
        } finally {
            this.lock.unlock();
        }
    }

    public void fail(Job job, String error) {
        this.lock.lock();

        try {
            job.updateLastError(error);
            ClientLane lane = this.lanes.get(job.getClientId());
            if (job.hasRetryLeft()) {
                job.updateJobStatus(JobStatus.INITIATED);
                job.updateRetryCount();
                lane.addFirst(job);
            } else {
                job.updateJobStatus(JobStatus.FAILED);
                this.activeKeys.remove(new JobKey(job.getClientId(), job.getContent()));
                System.out.println("[Job Failed] jobId=" + job.getJobId()
                        + ", clientId=" + job.getClientId() + ", error=" + error);
            }
            lane.setBusy(false);
            promoteHead(lane);
        } finally {
            this.lock.unlock();
        }
    }

    public boolean cancel(String jobId) {
        this.lock.lock();

        try {
            Job job = this.jobs.get(jobId);

            if (Objects.isNull(job)) {
                throw new JobNotFoundException("[Error Cancel Job] No Job has been present with jobId: " + jobId);
            }

            if (!Objects.equals(job.getStatus(), JobStatus.INITIATED)) {
                return false;
            }

            ClientLane lane = this.lanes.get(job.getClientId());
            boolean wasInReadyQueue = this.readyQueue.remove(job);
            lane.remove(job);

            job.updateJobStatus(JobStatus.CANCELLED);
            this.activeKeys.remove(new JobKey(job.getClientId(), job.getContent()));

            if (wasInReadyQueue) {
                promoteHead(lane);
            }

            return true;
        } finally {
            this.lock.unlock();
        }
    }

    public Job getJob(String jobId) {
        this.lock.lock();

        try {
            Job job = this.jobs.get(jobId);

            if (Objects.isNull(job)) {
                throw new JobNotFoundException("[Error Get Job] No Job has been present with jobId: " + jobId);
            }

            return job;
        } finally {
            this.lock.unlock();
        }

    }

    public void shutDown() {
        this.lock.lock();

        try {
            this.stopped = true;
            this.jobReady.signalAll();
        } finally {
            this.lock.unlock();
        }

        for (Thread thread : this.workThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void promoteHead(ClientLane lane) {
        if (lane.isBusy() || lane.isEmpty()) {
            return;
        }

        this.readyQueue.add(lane.peek());
        this.jobReady.signal();
    }
}
