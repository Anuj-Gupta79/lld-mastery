package com.lld.problems.parkinglot.code.models;

public class Receipt {
    private double fee;
    private Ticket ticket;
    private boolean paymentStatus;

    public Receipt(double fee, Ticket ticket) {
        this.fee = fee;
        this.ticket = ticket;
        this.paymentStatus = false;
    }

    public void updatePaymentStatus(boolean state) {
        this.paymentStatus = state;
    }

    public boolean getPaymentStatus() {
        return this.paymentStatus;
    }

    public double getFee() {
        return this.fee;
    }

    public Ticket getTicket() {
        return this.ticket;
    }
}
