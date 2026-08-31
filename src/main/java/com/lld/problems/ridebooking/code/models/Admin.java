package com.lld.problems.ridebooking.code.models;

import java.util.UUID;

import com.lld.problems.ridebooking.code.Core.RideBooking;
import com.lld.problems.ridebooking.code.constants.VehicleType;

public class Admin {
    private String adminId;
    private String name;
    private RideBooking rideBooking;

    public Admin(String name, RideBooking rideBooking) {
        this.adminId = UUID.randomUUID().toString();
        this.name = name;
        this.rideBooking = rideBooking;
    }

    public String getAdminId() {
        return this.adminId;
    }

    public String getName() {
        return this.name;
    }

    public RideBooking getRideBooking() {
        return this.rideBooking;
    }

    public void updatePrice(VehicleType type, double price) {
        this.rideBooking.updateBasePrice(type, price);
    }

    public void updateSurgeMultiplier(double multiplier) {
        this.rideBooking.updateSurgeMultiplier(multiplier);
    }

}
