package com.lld.problems.elevator.code.state;

import com.lld.problems.elevator.code.constants.Direction;

public class IdleState implements ElevatorState {

    @Override
    public boolean shouldServiceNow(Direction direction) {
        return true;
    }

    @Override
    public Direction getDirection() {
        return null;
    }

    @Override
    public boolean isIdle() {
        return true;
    }
}
