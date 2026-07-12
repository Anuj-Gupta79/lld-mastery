package com.lld.problems.elevator.code.strategy;

import java.util.List;

import com.lld.problems.elevator.code.constants.Direction;
import com.lld.problems.elevator.code.models.Button;

public interface NextStopStrategy {
    public List<Button> getNextStops(Direction direction, List<Button> serviceableStops);
}
