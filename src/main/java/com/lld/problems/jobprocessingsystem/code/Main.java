package com.lld.problems.jobprocessingsystem.code;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.lld.problems.jobprocessingsystem.code.constants.JobPriority;
import com.lld.problems.jobprocessingsystem.code.constants.JobStatus;
import com.lld.problems.jobprocessingsystem.code.core.JobProcessor;
import com.lld.problems.jobprocessingsystem.code.exceptions.InvalidJobException;
import com.lld.problems.jobprocessingsystem.code.models.Job;
import com.lld.problems.jobprocessingsystem.code.models.JobTask;

public class Main {
    private static final List<String> EVENTS = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) throws Exception {
        s1BasicCompletion();
        s2RetryThenSuccess();
        s3FailAfterRetries();
        s4Duplicate();
        s5ClientOrderAndParallel();
        s6Priority();
        s7Scheduled();
        s8Cancel();
        s9Shutdown();
        System.out.println("== done ==");
    }

    private static void s1BasicCompletion() throws Exception {
        System.out.println("== S1: basic completion ==");
        JobProcessor processor = new JobProcessor(Clock.systemUTC());

        TestTask task = ok("A");
        String id = processor.submit("c1", "job-A", task, null, null);
        waitForStatus(processor, id, JobStatus.COMPLETED);

        Job job = processor.getJob(id);
        System.out.println("status=" + job.getStatus() + ", priority=" + job.getPriority()
                + ", result=" + job.getResult() + ", retryCount=" + job.getRetryCount()
                + ", executeCount=" + task.executeCount.get());
        processor.shutDown();
    }

    private static void s2RetryThenSuccess() throws Exception {
        System.out.println("== S2: fail twice, then success ==");
        JobProcessor processor = new JobProcessor(Clock.systemUTC());

        TestTask task = failing("R", 2);
        String id = processor.submit("c2", "job-R", task, null, null);
        waitForStatus(processor, id, JobStatus.COMPLETED);

        Job job = processor.getJob(id);
        System.out.println("status=" + job.getStatus() + ", retryCount=" + job.getRetryCount()
                + ", executeCount=" + task.executeCount.get() + ", undoCount=" + task.undoCount.get()
                + ", lastError=" + job.getLastError());
        processor.shutDown();
    }

    private static void s3FailAfterRetries() throws Exception {
        System.out.println("== S3: fail after all retries ==");
        JobProcessor processor = new JobProcessor(Clock.systemUTC());

        TestTask task = failing("F", 99);
        String id = processor.submit("c3", "job-F", task, null, null);
        waitForStatus(processor, id, JobStatus.FAILED);

        Job job = processor.getJob(id);
        System.out.println("status=" + job.getStatus() + ", retryCount=" + job.getRetryCount()
                + ", executeCount=" + task.executeCount.get() + ", undoCount=" + task.undoCount.get()
                + ", lastError=" + job.getLastError());

        String newId = processor.submit("c3", "job-F", ok("F2"), null, null);
        System.out.println("resubmitted after failure, new id differs=" + !newId.equals(id));
        waitForStatus(processor, newId, JobStatus.COMPLETED);
        processor.shutDown();
    }

    private static void s4Duplicate() throws Exception {
        System.out.println("== S4: duplicate jobs ==");
        JobProcessor processor = new JobProcessor(Clock.systemUTC());

        CountDownLatch started = new CountDownLatch(1);
        CountDownLatch gate = new CountDownLatch(1);
        String id1 = processor.submit("c4", "job-D", gated("D", started, gate), null, null);
        started.await(2, TimeUnit.SECONDS);

        TestTask copy = ok("D-copy");
        String id2 = processor.submit("c4", "job-D", copy, null, null);
        String id3 = processor.submit("c5", "job-D", ok("D-other"), null, null);
        System.out.println("sameIdWhileActive=" + id2.equals(id1));
        System.out.println("sameIdOtherClient=" + id3.equals(id1));

        gate.countDown();
        waitForStatus(processor, id1, JobStatus.COMPLETED);
        String id4 = processor.submit("c4", "job-D", ok("D-again"), null, null);
        System.out.println("sameIdAfterDone=" + id4.equals(id1));
        System.out.println("duplicateExecuteCount=" + copy.executeCount.get());
        waitForStatus(processor, id4, JobStatus.COMPLETED);
        processor.shutDown();
    }

    private static void s5ClientOrderAndParallel() throws Exception {
        System.out.println("== S5: same client in order, different clients in parallel ==");
        JobProcessor processor = new JobProcessor(Clock.systemUTC());

        EVENTS.clear();
        processor.submit("c6", "s5-1", slow("1", 100), null, null);
        processor.submit("c6", "s5-2", slow("2", 100), null, null);
        String last = processor.submit("c6", "s5-3", slow("3", 100), null, null);
        waitForStatus(processor, last, JobStatus.COMPLETED);
        System.out.println("order=" + new ArrayList<>(EVENTS));

        CountDownLatch both = new CountDownLatch(2);
        String p1 = processor.submit("c7", "s5-p", gated("P1", both, both), null, null);
        String p2 = processor.submit("c8", "s5-p", gated("P2", both, both), null, null);
        waitForStatus(processor, p1, JobStatus.COMPLETED);
        waitForStatus(processor, p2, JobStatus.COMPLETED);
        System.out.println("c7=" + status(processor, p1) + ", c8=" + status(processor, p2));
        processor.shutDown();
    }

    private static void s6Priority() throws Exception {
        System.out.println("== S6: priority, then oldest first ==");
        JobProcessor processor = new JobProcessor(Clock.systemUTC());

        EVENTS.clear();
        CountDownLatch blockersStarted = new CountDownLatch(10);
        List<CountDownLatch> gates = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CountDownLatch gate = new CountDownLatch(1);
            gates.add(gate);
            processor.submit("blk-" + i, "blocker", gated("blk-" + i, blockersStarted, gate), null, null);
        }
        blockersStarted.await(2, TimeUnit.SECONDS);

        String low = processor.submit("pc-1", "low", ok("low"), JobPriority.LOW, null);
        String normal = processor.submit("pc-2", "normal", ok("normal"), JobPriority.NORMAL, null);
        String high1 = processor.submit("pc-3", "high-1", ok("high-1"), JobPriority.HIGH, null);
        String high2 = processor.submit("pc-4", "high-2", ok("high-2"), JobPriority.HIGH, null);

        String[] expectedOrder = { high1, high2, normal, low };
        for (int i = 0; i < expectedOrder.length; i++) {
            gates.get(i).countDown();
            waitForStatus(processor, expectedOrder[i], JobStatus.COMPLETED);
        }
        for (CountDownLatch gate : gates) {
            gate.countDown();
        }

        List<String> starts = new ArrayList<>(EVENTS).stream()
                .filter(e -> e.startsWith("start:") && !e.contains("blk"))
                .toList();
        System.out.println("start order=" + starts);
        processor.shutDown();
    }

    private static void s7Scheduled() throws Exception {
        System.out.println("== S7: scheduled job ==");
        JobProcessor processor = new JobProcessor(Clock.systemUTC());

        Instant runAt = Instant.now().plusMillis(500);
        TestTask later = ok("later");
        String laterId = processor.submit("c9", "later", later, null, runAt);
        String nowId = processor.submit("c10", "now", ok("now"), null, null);

        waitForStatus(processor, nowId, JobStatus.COMPLETED);
        System.out.println("immediate job status=" + status(processor, nowId)
                + ", scheduled job status=" + status(processor, laterId));

        waitForStatus(processor, laterId, JobStatus.COMPLETED);
        System.out.println("scheduled job status=" + status(processor, laterId)
                + ", startedOnTime=" + !later.startedAt.isBefore(runAt));
        processor.shutDown();
    }

    private static void s8Cancel() throws Exception {
        System.out.println("== S8: cancel ==");
        JobProcessor processor = new JobProcessor(Clock.systemUTC());

        CountDownLatch started = new CountDownLatch(1);
        CountDownLatch gate = new CountDownLatch(1);
        String j1 = processor.submit("c11", "j1", gated("j1", started, gate), null, null);
        started.await(2, TimeUnit.SECONDS);
        TestTask t2 = ok("j2");
        String j2 = processor.submit("c11", "j2", t2, null, null);
        String j3 = processor.submit("c11", "j3", ok("j3"), null, null);

        System.out.println("cancel running job=" + processor.cancel(j1));
        System.out.println("cancel waiting job=" + processor.cancel(j2));
        System.out.println("cancel same job again=" + processor.cancel(j2));
        gate.countDown();
        waitForStatus(processor, j3, JobStatus.COMPLETED);
        System.out.println("final: j1=" + status(processor, j1) + ", j2=" + status(processor, j2)
                + ", j3=" + status(processor, j3) + ", j2ExecuteCount=" + t2.executeCount.get());

        String k1 = processor.submit("c12", "k1", ok("k1"), null, Instant.now().plusSeconds(5));
        String k2 = processor.submit("c12", "k2", ok("k2"), null, null);
        Thread.sleep(200);
        System.out.println("k2 before cancel=" + status(processor, k2));
        System.out.println("cancel scheduled k1=" + processor.cancel(k1));
        waitForStatus(processor, k2, JobStatus.COMPLETED);
        System.out.println("final: k1=" + status(processor, k1) + ", k2=" + status(processor, k2));
        processor.shutDown();
    }

    private static void s9Shutdown() throws Exception {
        System.out.println("== S9: shutdown ==");
        JobProcessor processor = new JobProcessor(Clock.systemUTC());

        CountDownLatch started = new CountDownLatch(1);
        CountDownLatch gate = new CountDownLatch(1);
        String r1 = processor.submit("c13", "r1", gated("r1", started, gate), null, null);
        started.await(2, TimeUnit.SECONDS);
        TestTask t2 = ok("r2");
        String r2 = processor.submit("c13", "r2", t2, null, null);

        Thread stopper = new Thread(processor::shutDown);
        stopper.start();
        Thread.sleep(300);
        System.out.println("shutdown still waiting=" + stopper.isAlive());

        try {
            processor.submit("c14", "r3", ok("r3"), null, null);
            System.out.println("submit after shutdown=accepted");
        } catch (InvalidJobException e) {
            System.out.println("submit after shutdown=rejected");
        }

        gate.countDown();
        stopper.join(3000);
        System.out.println("shutdown finished=" + !stopper.isAlive());
        System.out.println("final: r1=" + status(processor, r1) + ", r2=" + status(processor, r2)
                + ", r2ExecuteCount=" + t2.executeCount.get());
    }

    private static boolean waitForStatus(JobProcessor processor, String jobId, JobStatus expected)
            throws InterruptedException {
        long end = System.currentTimeMillis() + 10_000;
        while (System.currentTimeMillis() < end) {
            if (processor.getJob(jobId).getStatus() == expected) {
                return true;
            }
            Thread.sleep(10);
        }
        return false;
    }

    private static String status(JobProcessor processor, String jobId) {
        return processor.getJob(jobId).getStatus().toString();
    }

    private static TestTask ok(String name) {
        return new TestTask(name, 0, 0, null, null);
    }

    private static TestTask slow(String name, long sleepMs) {
        return new TestTask(name, 0, sleepMs, null, null);
    }

    private static TestTask failing(String name, int failTimes) {
        return new TestTask(name, failTimes, 0, null, null);
    }

    private static TestTask gated(String name, CountDownLatch started, CountDownLatch gate) {
        return new TestTask(name, 0, 0, started, gate);
    }

    private static class TestTask implements JobTask {
        private final String name;
        private final int failTimes;
        private final long sleepMs;
        private final CountDownLatch started;
        private final CountDownLatch gate;
        final AtomicInteger executeCount = new AtomicInteger();
        final AtomicInteger undoCount = new AtomicInteger();
        volatile Instant startedAt;

        TestTask(String name, int failTimes, long sleepMs, CountDownLatch started, CountDownLatch gate) {
            this.name = name;
            this.failTimes = failTimes;
            this.sleepMs = sleepMs;
            this.started = started;
            this.gate = gate;
        }

        @Override
        public String execute() {
            int attempt = this.executeCount.incrementAndGet();
            this.startedAt = Instant.now();
            EVENTS.add("start:" + this.name);

            if (this.started != null) {
                this.started.countDown();
            }
            if (this.gate != null) {
                try {
                    if (!this.gate.await(5, TimeUnit.SECONDS)) {
                        throw new IllegalStateException("gate timeout in " + this.name);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("interrupted in " + this.name);
                }
            }
            if (this.sleepMs > 0) {
                try {
                    Thread.sleep(this.sleepMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("interrupted in " + this.name);
                }
            }
            if (attempt <= this.failTimes) {
                throw new IllegalStateException("fail-" + this.name + "-" + attempt);
            }

            EVENTS.add("end:" + this.name);
            return "done-" + this.name;
        }

        @Override
        public void undo() {
            this.undoCount.incrementAndGet();
        }
    }
}