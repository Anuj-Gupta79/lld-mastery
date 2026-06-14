package com.lld.problems.parkinglot.code.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

import com.lld.problems.parkinglot.code.constants.SlotType;
import com.lld.problems.parkinglot.code.models.Receipt;
import com.lld.problems.parkinglot.code.models.Ticket;

public class Payment {
    private Map<SlotType, Double> priceMap;

    public Payment() {
        priceMap = Map.of(SlotType.SMALL, 2.0, SlotType.MEDIUM, 5.0, SlotType.LARGE, 10.0);
    }

    public Receipt calculateFee(Ticket ticket) {
    
        // WHY: minimum 1-hour billing, even if actual duration is under an hour
        long hours = Math.max(1, getTimeTaken(ticket.getEntryTime()));
        double pricePerHour = priceMap.get(ticket.getVehicle().getRequiredSlotType());

        return new Receipt(pricePerHour * hours, ticket);
    }

    public void processPayment(Receipt receipt) {
        receipt.updatePaymentStatus(true);
    }

    private long getTimeTaken(LocalDateTime entryTime) {
        LocalDateTime currTime = LocalDateTime.now();

        Duration duration = Duration.between(entryTime, currTime);

        return duration.toHours();
    }
}