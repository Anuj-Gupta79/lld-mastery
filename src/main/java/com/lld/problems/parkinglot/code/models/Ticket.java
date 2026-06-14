package com.lld.problems.parkinglot.code.models;

import java.time.LocalDateTime;

import com.lld.problems.parkinglot.code.models.parkingslot.ParkingSlot;
import com.lld.problems.parkinglot.code.models.vehicle.Vehicle;

public class Ticket {
    private ParkingSlot slot;
    private Vehicle vehicle;
    private LocalDateTime entryTime;

    public Ticket(ParkingSlot slot, Vehicle vehicle) {
        this.slot = slot;
        this.vehicle = vehicle;
        this.entryTime = LocalDateTime.now();
    }

    public ParkingSlot getSlot() {
        return this.slot;
    }

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public LocalDateTime getEntryTime() {
        return this.entryTime;
    }
}
