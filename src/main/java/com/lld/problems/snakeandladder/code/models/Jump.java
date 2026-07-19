package com.lld.problems.snakeandladder.code.models;

public class Jump {
    private int start;
    private int end;

    public Jump(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public boolean isSnake() {
        return this.start > this.end;
    }

    public boolean isLadder() {
        return this.end > this.start;
    }

    public int getStart() {
        return this.start;
    }

    public int getEnd() {
        return this.end;
    }
}
