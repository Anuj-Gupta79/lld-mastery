package com.lld.problems.library.code.models;

public class LibraryContext {
    private int expirationDays;
    private double latePenaltyFee;
    private double damagePenaltyFee;
    private double passCreationFee;
    private int maxBooksAllowed;

    public LibraryContext(Builder builder) {
        this.expirationDays = builder.expirationDays;
        this.damagePenaltyFee = builder.damagePenaltyFee;
        this.latePenaltyFee = builder.latePenaltyFee;
        this.passCreationFee = builder.passCreationFee;
        this.maxBooksAllowed = builder.maxBooksAllowed;
    }

    public int getExpirationDays() {
        return this.expirationDays;
    }

    public double getLatePenaltyFee() {
        return this.latePenaltyFee;
    }

    public double getDamagePenaltyFee() {
        return this.damagePenaltyFee;
    }

    public double getPassCreationFee() {
        return this.passCreationFee;
    }

    public int getMaxBookAllowed() {
        return this.maxBooksAllowed;
    }

    public static class Builder {
        private int expirationDays;
        private double latePenaltyFee;
        private double damagePenaltyFee;
        private double passCreationFee;
        private int maxBooksAllowed;

        public LibraryContext build() {
            return new LibraryContext(this);
        }

        public Builder setExpirationDays(int days) {
            this.expirationDays = days;
            return this;
        }

        public Builder setLatePenaltyFee(double fee) {
            this.latePenaltyFee = fee;
            return this;
        }

        public Builder setDamagePenaltyFee(double fee) {
            this.damagePenaltyFee = fee;
            return this;
        }

        public Builder setPassCreationFee(double fee) {
            this.passCreationFee = fee;
            return this;
        }

        public Builder setMaxBooksAllowed(int count) {
            this.maxBooksAllowed = count;
            return this;
        }
    }
}
