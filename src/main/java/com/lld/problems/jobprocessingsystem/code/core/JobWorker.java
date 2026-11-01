package com.lld.problems.jobprocessingsystem.code.core;

import java.util.Objects;

import com.lld.problems.jobprocessingsystem.code.models.Job;

public class JobWorker implements Runnable {
    private JobProcessor processor;

    public JobWorker(JobProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void run() {
        while (true) {
            Job job = this.processor.takeNextJob();

            if (Objects.isNull(job)) {
                break;
            }

            runOne(job);
        }
    }

    private void runOne(Job job) {
        String result;

        try {
            result = job.getTask().execute();
        } catch (Exception e) {
            handleFailure(job, e);
            return;
        }

        this.processor.complete(job, result);
    }

    private void handleFailure(Job job, Exception e) {
        String error = String.valueOf(e.getMessage());

        if (job.hasRetryLeft()) {
            try {
                job.getTask().undo();
            } catch (Exception undoError) {
                error = error + " | undo failed: " + undoError.getMessage();
            }
        }

        this.processor.fail(job, error);
    }

}
