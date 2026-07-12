package com.lld.problems.elevator.code.strategy;

import java.util.Comparator;
import java.util.List;

import com.lld.problems.elevator.code.constants.Direction;
import com.lld.problems.elevator.code.models.Button;

public class ScanStrategy implements NextStopStrategy {

    @Override
    public List<Button> getNextStops(Direction direction, List<Button> serviceableStops) {
        if (direction == Direction.UP) {
            serviceableStops.sort(Comparator.comparing((Button button) -> button.getFloor().getFloorNumber()));
        } else {
            serviceableStops
                    .sort(Comparator.comparing((Button button) -> button.getFloor().getFloorNumber()).reversed());
        }
        return serviceableStops;
    }
}
