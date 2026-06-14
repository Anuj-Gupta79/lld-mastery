package com.lld.problems.parkinglot.code.models.vehicle;

import com.lld.problems.parkinglot.code.constants.SlotType;

public abstract class Vehicle {
    private String platNumber;
    private String ownerName;

    public Vehicle(String platNumber, String ownerName) {
        this.platNumber = platNumber;
        this.ownerName = ownerName;
    }

    public String getPlatNumber() {
        return this.platNumber;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    abstract public SlotType getRequiredSlotType();
}
