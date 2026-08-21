package com.lld.problems.bookmyshow.code.models;

import java.util.Objects;

import com.lld.problems.bookmyshow.code.constants.SeatType;
import com.lld.problems.bookmyshow.code.dto.SeatUpdateRequest;

public class Seat {
    private String seatId;
    private int row;
    private int seatNumber;
    private SeatType seatType;

    public Seat(String id, int row, int seatNumber, SeatType seatType) {
        this.seatId = id;
        this.row = row;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
    }

    public void updateSeatDetails(SeatUpdateRequest request) {
        if (Objects.nonNull(request.getRow())) {
            this.row = request.getRow();
        }

        if (Objects.nonNull(request.getSeatNumber())) {
            this.seatNumber = request.getSeatNumber();
        }

        if (Objects.nonNull(request.getSeatType())) {
            this.seatType = request.getSeatType();
        }
    }

    public String getSeatId() {
        return this.seatId;
    }

    public int getRow() {
        return this.row;
    }

    public int getSeatNumber() {
        return this.seatNumber;
    }

    public SeatType getSeatType() {
        return this.seatType;
    }
}
