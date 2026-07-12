package com.lld.problems.elevator.code.state;

import com.lld.problems.elevator.code.constants.Direction;

public class MovingDownState implements ElevatorState {

    @Override
    public boolean shouldServiceNow(Direction direction) {
        return direction == null || direction == Direction.DOWN;
    }
    
    @Override
    public Direction getDirection() {
        return Direction.DOWN;
    }

    @Override
    public boolean isIdle() {
        return false;
    }
}
