package com.lld.problems.parkinglot.code;

import java.util.Map;
import java.util.Objects;

import com.lld.problems.parkinglot.code.constants.SlotType;
import com.lld.problems.parkinglot.code.constants.VehicleType;
import com.lld.problems.parkinglot.code.models.Ticket;

public class Main {
    public static void main(String[] args) {
        Map<SlotType, Integer> configs = Map.of(SlotType.SMALL, 2, SlotType.MEDIUM, 2, SlotType.LARGE, 1);
        ParkingLot parkingLot = new ParkingLot(configs);

        Ticket ticketA1B2 = parkingLot.entry(VehicleType.BIKE, "A1B2", "John");
        Ticket ticketA2B2 = parkingLot.entry(VehicleType.BIKE, "A2B2", "Johnny");
        Ticket ticketZ1B2 = parkingLot.entry(VehicleType.TRUCK, "Z1B2", "Will");
        Ticket ticketZ2B2 = parkingLot.entry(VehicleType.BIKE, "Z2B2", "Tale");

        if (!Objects.isNull(ticketA1B2)) {
            parkingLot.exit(ticketA1B2);
        }

        if (!Objects.isNull(ticketA2B2)) {
            parkingLot.exit(ticketA2B2);
        }

        if (!Objects.isNull(ticketZ1B2)) {
            parkingLot.exit(ticketZ1B2);
        }

        if (!Objects.isNull(ticketZ2B2)) {
            parkingLot.exit(ticketZ2B2);
        }
    }
}
