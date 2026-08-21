package com.lld.problems.bookmyshow.code.dto;

import com.lld.problems.bookmyshow.code.constants.SeatType;

public class SeatUpdateRequest {
    private final Integer row;
    private final Integer seatNumber;
    private final SeatType seatType;

    private SeatUpdateRequest(Builder builder) {
        this.row = builder.row;
        this.seatNumber = builder.seatNumber;
        this.seatType = builder.seatType;
    }

    public static class Builder {
        private Integer row;
        private Integer seatNumber;
        private SeatType seatType;

        public SeatUpdateRequest build() {
            return new SeatUpdateRequest(this);
        }

        public Builder setRow(int row) {
            this.row = row;
            return this;
        }

        public Builder setSeatNumber(int seatNumber) {
            this.seatNumber = seatNumber;
            return this;
        }

        public Builder setSeatType(SeatType seatType) {
            this.seatType = seatType;
            return this;
        }
    }

    public Integer getRow() {
        return this.row;
    }

    public Integer getSeatNumber() {
        return this.seatNumber;
    }

    public SeatType getSeatType() {
        return this.seatType;
    }
}