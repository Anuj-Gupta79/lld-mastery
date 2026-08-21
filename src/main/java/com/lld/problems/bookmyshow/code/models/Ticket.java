package com.lld.problems.bookmyshow.code.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Ticket {
    private String ticketId;
    private Booking booking;
    private LocalDateTime issuedAt;

    public Ticket(Booking booking) {
        this.ticketId = UUID.randomUUID().toString();
        this.booking = booking;
        this.issuedAt = LocalDateTime.now();
    }

    public String getTicketId() {
        return this.ticketId;
    }

    public Booking getBooking() {
        return this.booking;
    }

    public LocalDateTime getIssuedAt() {
        return this.issuedAt;
    }

    public String toString() {
        return "Ticket: id=" + this.ticketId + " ,bookingId=" + this.booking.getBookingId() + " ,issuedAt="
                + this.issuedAt;
    }

}
