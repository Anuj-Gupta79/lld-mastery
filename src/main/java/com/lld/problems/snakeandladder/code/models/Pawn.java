package com.lld.problems.snakeandladder.code.models;

public class Pawn {
    private int currentPosition;

    public Pawn() {
        this.currentPosition = 0;
    }

    public void updatePosition(int position) {
        this.currentPosition = position;
    }

    public int getCurrentPosition() {
        return this.currentPosition;
    }
}
