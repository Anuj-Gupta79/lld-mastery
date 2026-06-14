package com.lld.problems.parkinglot.code.models.parkingslot;

public abstract class ParkingSlot implements SlotState {
    private int slotNumber;
    private boolean occupied;

    public ParkingSlot(int slotNumber) {
        this.slotNumber = slotNumber;
    }

    @Override
    public boolean isOccupied() {
        return this.occupied;
    }

    public int getSlotNumber() {
        return this.slotNumber;
    }

    public void occupySlot() {
        this.occupied = true;
    }

    public void releaseSlot() {
        this.occupied = false;
    }
}
