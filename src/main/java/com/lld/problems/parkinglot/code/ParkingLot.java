package com.lld.problems.parkinglot.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.lld.problems.parkinglot.code.constants.SlotType;
import com.lld.problems.parkinglot.code.constants.VehicleType;
import com.lld.problems.parkinglot.code.factory.ParkingSlotFactory;
import com.lld.problems.parkinglot.code.factory.VehicleFactory;
import com.lld.problems.parkinglot.code.models.Receipt;
import com.lld.problems.parkinglot.code.models.Ticket;
import com.lld.problems.parkinglot.code.models.parkingslot.ParkingSlot;
import com.lld.problems.parkinglot.code.models.vehicle.Vehicle;
import com.lld.problems.parkinglot.code.service.Payment;
import com.lld.problems.parkinglot.code.strategy.NearestToEntranceSlotAssignment;
import com.lld.problems.parkinglot.code.strategy.SlotAssignment;

public class ParkingLot {
    private Map<SlotType, List<ParkingSlot>> slots;
    private ParkingSlotFactory parkingSlotFactory;
    private VehicleFactory vehicleFactory;
    private SlotAssignment strategy;
    private Payment payment;

    public ParkingLot(Map<SlotType, Integer> configs) {
        this.slots = new HashMap<>();
        this.parkingSlotFactory = new ParkingSlotFactory();
        this.vehicleFactory = new VehicleFactory();
        this.payment = new Payment();
        this.strategy = new NearestToEntranceSlotAssignment();
        int slotNumber = 1;
        for (Map.Entry<SlotType, Integer> config : configs.entrySet()) {
            List<ParkingSlot> slotList = new ArrayList<>();

            for (int slot = 1; slot <= config.getValue(); slot++) {
                slotList.add(this.parkingSlotFactory.createSlot(config.getKey(), slotNumber));
                slotNumber++;
            }

            this.slots.put(config.getKey(), slotList);
        }

    }

    public Ticket entry(VehicleType vehicleType, String platNumber, String ownerName) {
        Vehicle vehicle = this.vehicleFactory.createVehicle(vehicleType, platNumber, ownerName);

        try {
            ParkingSlot assignedSlot = this.strategy.assign(vehicle, this.slots);
            assignedSlot.occupySlot();

            System.out.println("Vehicle with number plat " + vehicle.getPlatNumber() + " has been parked to slot: "
                    + assignedSlot.getSlotNumber());
            return new Ticket(assignedSlot, vehicle);
        } catch (IllegalStateException e) {
            System.out.println("There is no slot available");
            return null;
        }
    }

    public Receipt exit(Ticket ticket) {
        if (Objects.isNull(ticket)) {
            System.out.println("Invalid Ticket, No Receipt has been created");
            return null;
        }

        Receipt receipt = this.payment.calculateFee(ticket);
        ticket.getSlot().releaseSlot();
        this.payment.processPayment(receipt);
        System.out.println(
                "Payment has been successful. You can take out your vehicle: " + ticket.getVehicle().getPlatNumber());
        return receipt;
    }

    public void setStrategy(SlotAssignment strategy) {
        this.strategy = strategy;
    }
}
