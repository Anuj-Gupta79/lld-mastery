package com.lld.problems.elevator.code.models;

import com.lld.problems.elevator.code.constants.Direction;

public class Button {
    private Floor floor;
    private Direction direction;

    public Button(Floor floor) {
        this.floor = floor;
        this.direction = null;
    }

    public Button(Floor floor, Direction direction) {
        this.floor = floor;
        this.direction = direction;
    }

    public Floor getFloor() {
        return this.floor;
    }

    public Direction getDirection() {
        return this.direction;
    }
}
