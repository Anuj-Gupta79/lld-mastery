package com.lld.problems.parkinglot.code.models.vehicle;

import com.lld.problems.parkinglot.code.constants.SlotType;

public class Bike extends Vehicle {

    public Bike(String platNumber, String ownerName) {
        super(platNumber, ownerName);
    }

    @Override
    public SlotType getRequiredSlotType() {
        return SlotType.SMALL;
    }

}
