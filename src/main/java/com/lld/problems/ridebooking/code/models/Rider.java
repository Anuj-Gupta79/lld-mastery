package com.lld.problems.ridebooking.code.models;

import java.util.UUID;

public class Rider {
    private String riderId;
    private String name;
    private double rating;
    private int numberOfRatings;

    public Rider(String name) {
        this.riderId = UUID.randomUUID().toString();
        this.name = name;
        this.rating = 0.0;
        this.numberOfRatings = 0;
    }

    public String getRiderId() {
        return this.riderId;
    }

    public String getName() {
        return this.name;
    }

    public double getRating() {
        return this.rating;
    }

    public void updateRating(double rating) {
        double totalRating = this.numberOfRatings * this.rating + rating;
        this.numberOfRatings += 1;

        this.rating = totalRating / numberOfRatings;

    }
}
