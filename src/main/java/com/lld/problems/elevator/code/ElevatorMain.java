package com.lld.problems.elevator.code;

import com.lld.problems.elevator.code.constants.Direction;
import com.lld.problems.elevator.code.models.Button;
import com.lld.problems.elevator.code.models.Floor;
import com.lld.problems.elevator.code.strategy.ScanStrategy;

public class ElevatorMain {
    public static void main(String[] args) {
        Elevator elevator = new Elevator(new ScanStrategy());

        elevator.addFloorRequest(new Button(new Floor(8), Direction.UP));
        elevator.moveOneStep();
        elevator.moveOneStep();

        elevator.addFloorRequest(new Button(new Floor(5), Direction.UP)); // same-direction, mid-route
        elevator.moveOneStep();

        elevator.addFloorRequest(new Button(new Floor(2), Direction.DOWN)); // opposite-direction, should defer
        elevator.addFloorRequest(new Button(new Floor(10), Direction.UP));

        while (elevator.hasPendingStops()) {
            elevator.moveOneStep();
        }

        System.out.println("Remaining deferred: " + elevator.getDeferredStops().size());

        while (elevator.hasPendingStops() || !elevator.getDeferredStops().isEmpty()) {
            if (!elevator.hasPendingStops() && !elevator.getDeferredStops().isEmpty()) {
                elevator.scheduleNextStop();
            }
            elevator.moveOneStep();
        }
    }
}