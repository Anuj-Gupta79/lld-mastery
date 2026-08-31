package com.lld.problems.ridebooking.code.Core;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lld.problems.ridebooking.code.constants.DriverStatus;
import com.lld.problems.ridebooking.code.constants.RideStatus;
import com.lld.problems.ridebooking.code.constants.VehicleType;
import com.lld.problems.ridebooking.code.exceptions.DriverAlreadyExistException;
import com.lld.problems.ridebooking.code.exceptions.InvalidCancelException;
import com.lld.problems.ridebooking.code.exceptions.InvalidCompleteException;
import com.lld.problems.ridebooking.code.exceptions.InvalidRatingException;
import com.lld.problems.ridebooking.code.exceptions.InvalidStartException;
import com.lld.problems.ridebooking.code.exceptions.NoDriverAvailableException;
import com.lld.problems.ridebooking.code.exceptions.RideNotFoundException;
import com.lld.problems.ridebooking.code.exceptions.RiderAlreadyExistException;
import com.lld.problems.ridebooking.code.exceptions.RiderNotFoundException;
import com.lld.problems.ridebooking.code.models.Driver;
import com.lld.problems.ridebooking.code.models.Location;
import com.lld.problems.ridebooking.code.models.Ride;
import com.lld.problems.ridebooking.code.models.Rider;
import com.lld.problems.ridebooking.code.services.PaymentService;
import com.lld.problems.ridebooking.code.strategy.DriverMatchingStrategy;
import com.lld.problems.ridebooking.code.strategy.NearestDriverMatchingStrategy;

public class RideBooking {
    private Map<String, Rider> riders;
    private Map<String, Driver> drivers;
    private Map<String, Ride> rides;
    private Map<VehicleType, Double> basePrice;
    private double surgeMultiplier;
    private DriverMatchingStrategy strategy;
    private PaymentService paymentService;

    public RideBooking(Map<VehicleType, Double> basePrice, double multiplier) {
        this.riders = new HashMap<>();
        this.drivers = new HashMap<>();
        this.rides = new HashMap<>();
        this.basePrice = basePrice;
        this.surgeMultiplier = multiplier;
        this.paymentService = new PaymentService();
        this.strategy = new NearestDriverMatchingStrategy();
    }

    public void registerDriver(Driver driver) {
        if (this.drivers.containsKey(driver.getDriverId())) {
            throw new DriverAlreadyExistException(
                    "[Error Register Driver] Requested driver is already exist in the system!");
        }

        this.drivers.put(driver.getDriverId(), driver);
    }

    public void registerRider(Rider rider) {
        if (this.riders.containsKey(rider.getRiderId())) {
            throw new RiderAlreadyExistException(
                    "[Error Register Rider] Requested rider is already exist in the system!");
        }

        this.riders.put(rider.getRiderId(), rider);
    }

    public Ride processRider(Location pickLocation, Location dropLocation, Rider rider, VehicleType vehicleType) {
        if (!this.riders.containsKey(rider.getRiderId())) {
            throw new RiderNotFoundException("[Error Process Ride] Requested rider is not exist in the system!");
        }

        Ride ride = new Ride(vehicleType, rider, pickLocation, dropLocation,
                getPrice(pickLocation, dropLocation, vehicleType));

        List<Driver> drivers = this.strategy.findDriver(vehicleType, pickLocation, getDrivers());

        if (drivers.isEmpty()) {
            throw new NoDriverAvailableException("[Error Process Ride] No driver available for type " + vehicleType);
        }

        for (Driver driver : drivers) {
            ride.addObserver(driver);
        }

        ride.notifyObservers();
        
        this.rides.put(ride.getRideId(), ride);

        return ride;
    }

    public void startRide(String rideId) {
        if (!this.rides.containsKey(rideId)) {
            throw new RideNotFoundException("[Error Start Ride] Requested ride is not present in the system!");
        }

        Ride ride = this.rides.get(rideId);

        if (ride.getStatus() != RideStatus.MATCHED) {
            throw new InvalidStartException("[Error Start Ride] Ride status is not match to start the ride!");
        }

        ride.setPickTime(LocalDateTime.now());
        ride.getDriver().updateLocation(ride.getPickLocation());
        ride.transitTo(RideStatus.STARTED);
    }

    public void completeRide(String rideId) {
        completeRide(rideId, 0);
    }

    public void completeRide(String rideId, double tip) {
        if (!this.rides.containsKey(rideId)) {
            throw new RideNotFoundException("[Error Complete Ride] Requested ride is not present in the system!");
        }

        Ride ride = this.rides.get(rideId);

        if (ride.getStatus() != RideStatus.STARTED) {
            throw new InvalidCompleteException(
                    "[Error Complete Ride] Ride status is not started to complete the ride!");
        }

        ride.setDropTime(LocalDateTime.now());
        ride.getDriver().updateLocation(ride.getDropLocation());
        ride.setTip(tip);

        this.paymentService.processPayment(tip + ride.getEstimatedPrice());

        ride.transitTo(RideStatus.COMPLETED);
        ride.getDriver().updateStatus(DriverStatus.AVAILABLE);
    }

    public void cancelRide(String rideId) {
        if (!this.rides.containsKey(rideId)) {
            throw new RideNotFoundException("[Error Cancel Ride] Requested ride is not present in the system!");
        }

        Ride ride = this.rides.get(rideId);

        if (ride.getStatus() != RideStatus.PENDING && ride.getStatus() != RideStatus.MATCHED) {
            throw new InvalidCancelException("[Error Cancel Ride] Ride status is not pending to cancel the ride!");
        }

        if (ride.getStatus() == RideStatus.MATCHED) {
            ride.getDriver().updateStatus(DriverStatus.AVAILABLE);
        }

        ride.transitTo(RideStatus.CANCELLED);
    }

    public void rateRider(String rideId, double rating) {
        if (!this.rides.containsKey(rideId)) {
            throw new RideNotFoundException("[Error Rate Rider] Requested ride is not present in the system!");
        }

        Ride ride = this.rides.get(rideId);

        if (ride.getStatus() != RideStatus.COMPLETED) {
            throw new InvalidRatingException("[Error Rate Rider] Ride is not yet completed!");
        }

        ride.setDriverToRiderRating(rating);
        ride.getRider().updateRating(rating);
    }

    public void rateDriver(String rideId, double rating) {
        if (!this.rides.containsKey(rideId)) {
            throw new RideNotFoundException("[Error Rate Driver] Requested ride is not present in the system!");
        }

        Ride ride = this.rides.get(rideId);

        if (ride.getStatus() != RideStatus.COMPLETED) {
            throw new InvalidRatingException("[Error Rate Driver] Ride is not yet completed!");
        }

        ride.setRiderToDriverRating(rating);
        ride.getDriver().updateRating(rating);
    }

    public void updateBasePrice(VehicleType vehicleType, double price) {
        this.basePrice.put(vehicleType, price);
    }

    public void updateSurgeMultiplier(double multiplier) {
        this.surgeMultiplier = multiplier;
    }

    public List<Rider> getRiders() {
        return this.riders.values().stream().toList();
    }

    public List<Driver> getDrivers() {
        return this.drivers.values().stream().toList();
    }

    public List<Ride> getRides() {
        return this.rides.values().stream().toList();
    }

    public Map<VehicleType, Double> getBasePrice() {
        return this.basePrice;
    }

    public double getSurgeMultiplier() {
        return this.surgeMultiplier;
    }

    public void setStrategy(DriverMatchingStrategy strategy) {
        this.strategy = strategy;
    }

    private double getPrice(Location pickLocation, Location dropLocation, VehicleType type) {
        return distance(pickLocation, dropLocation) * this.basePrice.get(type) * this.surgeMultiplier;
    }

    private double distance(Location a, Location b) {
        // WHY: Euclidean approx sufficient for LLD demo, real systems use
        // haversine/road-network distance
        double dx = a.getLatitude() - b.getLatitude();
        double dy = a.getLongitude() - b.getLongitude();
        return Math.sqrt(dx * dx + dy * dy);
    }

}
