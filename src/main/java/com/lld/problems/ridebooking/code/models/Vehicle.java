package com.lld.problems.ridebooking.code.models;

import java.util.UUID;

import com.lld.problems.ridebooking.code.constants.VehicleType;

public class Vehicle {
    private String vehicleId;
    private String vehicleNumber;
    private VehicleType vehicleType;

    public Vehicle(String number, VehicleType type) {
        this.vehicleId = UUID.randomUUID().toString();
        this.vehicleNumber = number;
        this.vehicleType = type;
    }

    public String getVehicleId() {
        return this.vehicleId;
    }

    public String getVehicleNumber() {
        return this.vehicleNumber;
    }

    public VehicleType getVehicleType() {
        return this.vehicleType;
    }
}
