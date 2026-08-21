package com.lld.problems.bookmyshow.code.models;

import java.time.LocalDateTime;
import java.util.Map;

import com.lld.problems.bookmyshow.code.constants.BookingStatus;

public class Booking {
    private String bookingId;
    private Show show;
    private Map<String, ShowSeat> showSeats;
    private User user;
    private BookingStatus status;
    private LocalDateTime bookingTime;
    private double price;

    public Booking(String id, Show show, Map<String, ShowSeat> showSeats, User user) {
        this.bookingId = id;
        this.show = show;
        this.showSeats = showSeats;
        this.user = user;
        this.status = BookingStatus.PENDING;
        this.bookingTime = LocalDateTime.now();
        this.price = 0.0;
    }

    public void updatePrice(double price) {
        this.price += price;
    }

    public void updateStatus(BookingStatus updatedStatus) {
        this.status = updatedStatus;
    }

    public String getBookingId() {
        return this.bookingId;
    }

    public Show getShow() {
        return this.show;
    }

    public User getUser() {
        return this.user;
    }

    public BookingStatus getBookingStatus() {
        return this.status;
    }

    public LocalDateTime getBookingTime() {
        return this.bookingTime;
    }

    public Map<String, ShowSeat> getShowSeats() {
        return this.showSeats;
    }

    public double getPrice() {
        return this.price;
    }
}
