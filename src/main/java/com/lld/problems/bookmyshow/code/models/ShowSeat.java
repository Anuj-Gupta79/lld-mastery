package com.lld.problems.bookmyshow.code.models;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import com.lld.problems.bookmyshow.code.constants.BookMyShowConstants;

public class ShowSeat {
    private String showSeatId;
    private Seat seat;
    private boolean available;
    private LocalDateTime lockedAt;
    private double price;

    public ShowSeat(String id, Seat seat, double price) {
        this.showSeatId = id;
        this.seat = seat;
        this.price = price;
        this.available = true;
    }

    public String getShowSeatId() {
        return this.showSeatId;
    }

    public void lock() {
        this.available = false;
        this.lockedAt = LocalDateTime.now();
    }

    public boolean isExpiredLock() {
        return (Objects.isNull(lockedAt) || (ChronoUnit.MINUTES.between(this.lockedAt,
                LocalDateTime.now()) > BookMyShowConstants.LOCK_EXPIRATION_MINUTES));
    }

    public void release() {
        this.available = true;
        this.lockedAt = null;
    }

    public boolean isAvailable() {
        if (this.available) {
            return this.available;
        }

        if (isExpiredLock()) {
            release();
            return true;
        }

        return false;
    }

    public Seat getSeat() {
        return this.seat;
    }

    public double getPrice() {
        return this.price;
    }
}
