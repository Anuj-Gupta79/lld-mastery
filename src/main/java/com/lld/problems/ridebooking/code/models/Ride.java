package com.lld.problems.ridebooking.code.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.lld.problems.ridebooking.code.constants.DriverStatus;
import com.lld.problems.ridebooking.code.constants.RideStatus;
import com.lld.problems.ridebooking.code.constants.VehicleType;
import com.lld.problems.ridebooking.code.observer.Observer;
import com.lld.problems.ridebooking.code.observer.Subject;

public class Ride implements Subject {
    private String rideId;
    private Driver driver;
    private Rider rider;
    private VehicleType requestedType;
    private Location pickLocation;
    private Location dropLocation;
    private LocalDateTime pickTime;
    private LocalDateTime dropTime;
    private RideStatus status;
    private double estimatedPrice;
    private Double tip;
    private Double riderToDriverRating;
    private Double driverToRiderRating;
    private List<Observer> availableDrivers;

    public Ride(VehicleType type, Rider rider, Location pickLocation, Location dropLocation, double estimatedPrice) {
        this.rideId = UUID.randomUUID().toString();
        this.requestedType = type;
        this.rider = rider;
        this.pickLocation = pickLocation;
        this.dropLocation = dropLocation;
        this.estimatedPrice = estimatedPrice;
        this.status = RideStatus.PENDING;
        this.availableDrivers = new ArrayList<>();
    }

    public void setPickTime(LocalDateTime time) {
        this.pickTime = time;
    }

    public void setDropTime(LocalDateTime time) {
        this.dropTime = time;
    }

    public void setTip(double tip) {
        this.tip = tip;
    }

    public void setRiderToDriverRating(double rating) {
        this.riderToDriverRating = rating;
    }

    public void setDriverToRiderRating(double rating) {
        this.driverToRiderRating = rating;
    }

    public void transitTo(RideStatus status) {
        this.status = status;
    }

    public String getRideId() {
        return this.rideId;
    }

    public Rider getRider() {
        return this.rider;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Driver getDriver() {
        return this.driver;
    }

    public Location getPickLocation() {
        return this.pickLocation;
    }

    public Location getDropLocation() {
        return this.dropLocation;
    }

    public double getEstimatedPrice() {
        return this.estimatedPrice;
    }

    public Double getTip() {
        return this.tip;
    }

    public RideStatus getStatus() {
        return this.status;
    }

    public LocalDateTime getPickTime() {
        return this.pickTime;
    }

    public LocalDateTime getDropTime() {
        return this.dropTime;
    }

    public Double getRiderToDriverRating() {
        return this.riderToDriverRating;
    }

    public Double getDriverToRiderRating() {
        return this.driverToRiderRating;
    }

    public VehicleType getRequestedType() {
        return this.requestedType;
    }

    public synchronized boolean tryAccept(Driver driver) {
        if (this.status != RideStatus.PENDING) {
            System.out.print("Ride has already been assigned to " + this.driver.getDriverName());
            return false;
        }

        this.driver = driver;
        this.driver.updateStatus(DriverStatus.ON_TRIP);
        transitTo(RideStatus.MATCHED);
        return true;
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : this.availableDrivers) {
            observer.update(this);
        }
    }

    @Override
    public void addObserver(Observer observer) {
        this.availableDrivers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        this.availableDrivers.remove(observer);
    }
}
