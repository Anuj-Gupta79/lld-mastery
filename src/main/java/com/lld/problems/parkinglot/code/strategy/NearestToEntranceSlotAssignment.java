package com.lld.problems.parkinglot.code.strategy;

import java.util.List;
import java.util.Map;

import com.lld.problems.parkinglot.code.constants.SlotType;
import com.lld.problems.parkinglot.code.models.parkingslot.ParkingSlot;
import com.lld.problems.parkinglot.code.models.vehicle.Vehicle;

public class NearestToEntranceSlotAssignment implements SlotAssignment {

    @Override
    public ParkingSlot assign(Vehicle vehicle, Map<SlotType, List<ParkingSlot>> slotsMap) {
        SlotType requiredSlot = vehicle.getRequiredSlotType();
        List<ParkingSlot> slotList = slotsMap.get(requiredSlot);

        // WHY: lower slotNumber = closer to entrance (assumed linear layout)
        int minSlotNumber = Integer.MAX_VALUE;
        ParkingSlot availableSlot = null;
        for (ParkingSlot slot : slotList) {
            if (minSlotNumber > slot.getSlotNumber() && !slot.isOccupied()) {
                minSlotNumber = slot.getSlotNumber();
                availableSlot = slot;
            }
        }

        if (minSlotNumber != Integer.MAX_VALUE) {
            return availableSlot;
        }

        throw new IllegalStateException("No free slots available");
    }
}
