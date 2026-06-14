package com.lld.problems.parkinglot.code.models.vehicle;

import com.lld.problems.parkinglot.code.constants.SlotType;

public class Truck extends Vehicle {

    public Truck(String platNumber, String ownerName) {
        super(platNumber, ownerName);
    }

    @Override
    public SlotType getRequiredSlotType() {
        return SlotType.LARGE;
    }
    
}
