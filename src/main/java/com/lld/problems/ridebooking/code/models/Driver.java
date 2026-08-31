package com.lld.problems.ridebooking.code.models;

import java.util.UUID;

import com.lld.problems.ridebooking.code.constants.DriverStatus;
import com.lld.problems.ridebooking.code.observer.Observer;

public class Driver implements Observer {
    private String driverId;
    private String driverName;
    private String licenseNumber;
    private Vehicle vehicle;
    private DriverStatus status;
    private double rating;
    private Location location;
    private int numberOfRatings;

    public Driver(String name, String licenseNumber, Vehicle vehicle, Location location) {
        this.driverId = UUID.randomUUID().toString();
        this.driverName = name;
        this.licenseNumber = licenseNumber;
        this.vehicle = vehicle;
        this.status = DriverStatus.AVAILABLE;
        this.rating = 0.0;
        this.location = location;
        this.numberOfRatings = 0;
    }

    public String getDriverId() {
        return this.driverId;
    }

    public String getDriverName() {
        return this.driverName;
    }

    public String getLicenseNumber() {
        return this.licenseNumber;
    }

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public DriverStatus getDriverStatus() {
        return this.status;
    }

    public double getRating() {
        return this.rating;
    }

    public Location getLocation() {
        return this.location;
    }

    public boolean isAvailable() {
        return this.status == DriverStatus.AVAILABLE;
    }

    public void updateLocation(Location location) {
        this.location = location;
    }

    public void updateStatus(DriverStatus status) {
        this.status = status;
    }

    public void updateRating(double rating) {
        double totalRating = this.numberOfRatings * this.rating + rating;
        this.numberOfRatings += 1;

        this.rating = totalRating / numberOfRatings;
    }

    @Override
    public void update(Ride ride) {
        ride.tryAccept(this);
    }

}
