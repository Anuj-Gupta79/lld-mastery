package com.lld.problems.parkinglot.code.strategy;

import java.util.List;
import java.util.Map;

import com.lld.problems.parkinglot.code.constants.SlotType;
import com.lld.problems.parkinglot.code.models.parkingslot.ParkingSlot;
import com.lld.problems.parkinglot.code.models.vehicle.Vehicle;

public class NearestToExitSlotAssignment implements SlotAssignment {

    @Override
    public ParkingSlot assign(Vehicle vehicle, Map<SlotType, List<ParkingSlot>> slotsMap) {
        SlotType requiredSlot = vehicle.getRequiredSlotType();
        List<ParkingSlot> slotList = slotsMap.get(requiredSlot);

        // WHY: higher slotNumber = closer to exit (assumed linear layout)
        int maxSlotNumber = Integer.MIN_VALUE;
        ParkingSlot availableSlot = null;
        for (ParkingSlot slot : slotList) {
            if (maxSlotNumber < slot.getSlotNumber() && !slot.isOccupied()) {
                maxSlotNumber = slot.getSlotNumber();
                availableSlot = slot;
            }
        }

        if (maxSlotNumber != Integer.MIN_VALUE) {
            return availableSlot;
        }

        throw new IllegalStateException("No free slots available");

    }
}
