package com.lld.problems.elevator.code.state;

import com.lld.problems.elevator.code.constants.Direction;

public interface ElevatorState {
    public boolean shouldServiceNow(Direction direction);

    public Direction getDirection();

    public boolean isIdle();
}
