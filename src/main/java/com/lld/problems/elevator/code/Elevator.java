package com.lld.problems.elevator.code;

import java.util.ArrayList;
import java.util.List;

import com.lld.problems.elevator.code.constants.Direction;
import com.lld.problems.elevator.code.models.Button;
import com.lld.problems.elevator.code.models.Door;
import com.lld.problems.elevator.code.models.Floor;
import com.lld.problems.elevator.code.state.ElevatorState;
import com.lld.problems.elevator.code.state.IdleState;
import com.lld.problems.elevator.code.state.MovingDownState;
import com.lld.problems.elevator.code.state.MovingUpState;
import com.lld.problems.elevator.code.strategy.NextStopStrategy;

public class Elevator {
    private Floor currFloor;
    private ElevatorState currState;
    private NextStopStrategy strategy;
    private Door door;
    private List<Button> pendingStops;
    private List<Button> deferredStops;

    public Elevator(NextStopStrategy strategy) {
        this.strategy = strategy;
        this.door = new Door();
        this.pendingStops = new ArrayList<>();
        this.deferredStops = new ArrayList<>();
        this.currState = new IdleState();
        this.currFloor = new Floor(0);
    }

    public void addFloorRequest(Button button) {
        pendingStops.add(button);
        scheduleNextStop();
    }

    public void scheduleNextStop() {
        List<Button> deferred = new ArrayList<>();
        List<Button> serviceable = new ArrayList<>();

        for (Button button : pendingStops) {
            if (currState.shouldServiceNow(button.getDirection())) {
                serviceable.add(button);
            } else {
                deferred.add(button);
            }
        }

        if (currState.isIdle() && !serviceable.isEmpty()) {
            Button nearest = serviceable.get(0);
            int nearestDist = Math.abs(nearest.getFloor().getFloorNumber() - currFloor.getFloorNumber());
            for (Button b : serviceable) {
                int dist = Math.abs(b.getFloor().getFloorNumber() - currFloor.getFloorNumber());
                if (dist < nearestDist) {
                    nearest = b;
                    nearestDist = dist;
                }
            }
            int nearestFloor = nearest.getFloor().getFloorNumber();
            int curr = currFloor.getFloorNumber();
            if (nearestFloor > curr)
                currState = new MovingUpState();
            else if (nearestFloor < curr)
                currState = new MovingDownState();
        }

        pendingStops = currState.isIdle()
                ? serviceable
                : strategy.getNextStops(currState.getDirection(), serviceable);

        this.deferredStops.addAll(deferred);
    }

    public void moveOneStep() {

        int nextFloorNumber = this.currFloor.getFloorNumber();

        if (!this.currState.isIdle()) {
            if (this.currState.getDirection() == Direction.DOWN) {
                nextFloorNumber--;
            } else {
                nextFloorNumber++;
            }
        }

        this.currFloor = new Floor(nextFloorNumber);
        Button stop = findFirstStop(nextFloorNumber);

        System.out.println("Elevator is at " + this.currFloor.getFloorNumber() + " current direction is "
                + this.currState.getDirection());
        if (stop != null) {
            this.door.open();
            this.door.close();
            this.pendingStops.remove(stop);
        }

        if (!hasPendingStops()) {
            currState = new IdleState();
            this.pendingStops = this.deferredStops;
            this.deferredStops = new ArrayList<>();
            scheduleNextStop();
        }

    }

    private Button findFirstStop(int nextFloorNumber) {
        for (Button button : this.pendingStops) {
            if (button.getFloor().getFloorNumber() == nextFloorNumber) {
                return button;
            }
        }
        return null;
    }

    public Floor getCurrFloor() {
        return this.currFloor;
    }

    public ElevatorState getCurrState() {
        return this.currState;
    }

    public Door getDoor() {
        return this.door;
    }

    public List<Button> getPendingStops() {
        return this.pendingStops;
    }

    public boolean hasPendingStops() {
        return !this.pendingStops.isEmpty();
    }

    public List<Button> getDeferredStops() {
        return this.deferredStops;
    }

}
