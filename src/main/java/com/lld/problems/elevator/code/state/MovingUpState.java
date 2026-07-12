package com.lld.problems.elevator.code.state;

import com.lld.problems.elevator.code.constants.Direction;

public class MovingUpState implements ElevatorState {

    @Override
    public boolean shouldServiceNow(Direction direction) {
        return direction == null || direction == Direction.UP;
    }

    @Override
    public Direction getDirection() {
        return Direction.UP;
    }

    @Override
    public boolean isIdle() {
        return false;
    }
}
