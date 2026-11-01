package com.lld.problems.jobprocessingsystem.code.constants;

public enum JobPriority {
    LOW(1),
    NORMAL(2),
    HIGH(3);

    private final int value;

    JobPriority(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
