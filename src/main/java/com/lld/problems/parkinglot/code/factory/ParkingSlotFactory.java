package com.lld.problems.parkinglot.code.factory;

import com.lld.problems.parkinglot.code.constants.SlotType;
import com.lld.problems.parkinglot.code.models.parkingslot.LargeParkingSlot;
import com.lld.problems.parkinglot.code.models.parkingslot.MediumParkingSlot;
import com.lld.problems.parkinglot.code.models.parkingslot.ParkingSlot;
import com.lld.problems.parkinglot.code.models.parkingslot.SmallParkingSlot;

public class ParkingSlotFactory {
    public ParkingSlot createSlot(SlotType slotType, int slotNumber) {
        switch (slotType) {
            case SMALL:
                return new SmallParkingSlot(slotNumber);
            case MEDIUM:
                return new MediumParkingSlot(slotNumber);
            case LARGE:
                return new LargeParkingSlot(slotNumber);
            default:
                throw new IllegalArgumentException("Invalid Slot type");
        }
    }
}
