package com.lld.problems.parkinglot.code.strategy;

import java.util.List;
import java.util.Map;

import com.lld.problems.parkinglot.code.constants.SlotType;
import com.lld.problems.parkinglot.code.models.parkingslot.ParkingSlot;
import com.lld.problems.parkinglot.code.models.vehicle.Vehicle;

public interface SlotAssignment {
    public ParkingSlot assign(Vehicle vehicle, Map<SlotType, List<ParkingSlot>> slotsMap);
}
