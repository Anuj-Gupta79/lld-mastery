package com.lld.problems.ridebooking.code;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.lld.problems.ridebooking.code.Core.RideBooking;
import com.lld.problems.ridebooking.code.constants.VehicleType;
import com.lld.problems.ridebooking.code.exceptions.DriverAlreadyExistException;
import com.lld.problems.ridebooking.code.exceptions.InvalidCancelException;
import com.lld.problems.ridebooking.code.exceptions.InvalidCompleteException;
import com.lld.problems.ridebooking.code.exceptions.InvalidRatingException;
import com.lld.problems.ridebooking.code.exceptions.InvalidStartException;
import com.lld.problems.ridebooking.code.exceptions.NoDriverAvailableException;
import com.lld.problems.ridebooking.code.exceptions.RiderNotFoundException;
import com.lld.problems.ridebooking.code.models.Admin;
import com.lld.problems.ridebooking.code.models.Driver;
import com.lld.problems.ridebooking.code.models.Location;
import com.lld.problems.ridebooking.code.models.Ride;
import com.lld.problems.ridebooking.code.models.Rider;
import com.lld.problems.ridebooking.code.models.Vehicle;
import com.lld.problems.ridebooking.code.observer.Observer;

public class Main {

    private static class TestDriverResponder implements Observer {
        private final Driver driver;
        private final boolean willAccept;

        TestDriverResponder(Driver driver, boolean willAccept) {
            this.driver = driver;
            this.willAccept = willAccept;
        }

        @Override
        public void update(Ride ride) {
            if (willAccept) {
                ride.tryAccept(driver);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Map<VehicleType, Double> basePrice = new HashMap<>();
        basePrice.put(VehicleType.MINI, 10.0);
        basePrice.put(VehicleType.SEDAN, 15.0);
        basePrice.put(VehicleType.SUV, 20.0);

        RideBooking rideBooking = new RideBooking(basePrice, 1.0);
        Admin admin = new Admin("Platform Admin", rideBooking);

        Rider aman = new Rider("Aman");
        Rider priya = new Rider("Priya");
        rideBooking.registerRider(aman);
        rideBooking.registerRider(priya);

        Vehicle v1 = new Vehicle("KA-01-1234", VehicleType.SEDAN);
        Driver ravi = new Driver("Ravi", "LIC001", v1, new Location(12.90, 77.60));

        Vehicle v2 = new Vehicle("KA-01-5678", VehicleType.SEDAN);
        Driver suresh = new Driver("Suresh", "LIC002", v2, new Location(13.50, 78.20)); // permanently out of radius

        Vehicle v3 = new Vehicle("KA-01-9999", VehicleType.MINI);
        Driver kiran = new Driver("Kiran", "LIC003", v3, new Location(12.91, 77.61));

        // Second SEDAN driver near Aman's pickup area — needed because Ravi drifts
        // to amanDrop's location after ride1 completes, and would otherwise be out
        // of MAX_MATCH_RADIUS for ride3, which is requested near amanPickup again.
        Vehicle v4 = new Vehicle("KA-01-4321", VehicleType.SEDAN);
        Driver vivek = new Driver("Vivek", "LIC004", v4, new Location(12.90, 77.60));

        rideBooking.registerDriver(ravi);
        rideBooking.registerDriver(suresh);
        rideBooking.registerDriver(kiran);
        rideBooking.registerDriver(vivek);

        System.out.println("===== Happy Path: Aman requests a SEDAN =====");
        Location amanPickup = new Location(12.90, 77.60);
        Location amanDrop = new Location(12.95, 77.65);

        Ride ride1 = rideBooking.processRider(amanPickup, amanDrop, aman, VehicleType.SEDAN);
        // NOTE: broadcast matching no longer ranks by distance — it only filters by
        // radius. Ravi and Vivek are both eligible SEDAN candidates within radius,
        // and since production Driver.update() always accepts, whichever one is
        // first in the underlying HashMap-backed candidate list wins. Not
        // deterministic by design at this scale — asserting "matched, not null"
        // rather than naming a specific driver.
        System.out.println("Matched driver: " + ride1.getDriver().getDriverName()
                + " (expected: Ravi or Vivek — both eligible SEDAN drivers within radius)");
        System.out.println("Ride status: " + ride1.getStatus() + " (expected: MATCHED)");
        System.out.println("Estimated price: " + ride1.getEstimatedPrice());
        System.out.println("Matched driver status: " + ride1.getDriver().getDriverStatus() + " (expected: ON_TRIP)");

        rideBooking.startRide(ride1.getRideId());
        System.out.println("Ride status after start: " + ride1.getStatus() + " (expected: STARTED)");
        System.out.println("Pick time recorded: " + (ride1.getPickTime() != null));

        rideBooking.completeRide(ride1.getRideId(), 5.0);
        System.out.println("Ride status after complete: " + ride1.getStatus() + " (expected: COMPLETED)");
        System.out.println("Tip: " + ride1.getTip() + " (expected: 5.0)");
        System.out.println("Matched driver status after complete: " + ride1.getDriver().getDriverStatus()
                + " (expected: AVAILABLE)");

        rideBooking.rateDriver(ride1.getRideId(), 5.0);
        rideBooking.rateRider(ride1.getRideId(), 4.0);
        System.out.println("Matched driver's rating: " + ride1.getDriver().getRating() + " (expected: 5.0)");
        System.out.println("Aman's rating: " + aman.getRating() + " (expected: 4.0)");

        System.out.println();
        System.out.println("===== Cancellation: Priya requests then cancels before pickup =====");
        Location priyaPickup = new Location(12.91, 77.61);
        Location priyaDrop = new Location(12.96, 77.66);

        Ride ride2 = rideBooking.processRider(priyaPickup, priyaDrop, priya, VehicleType.MINI);
        System.out.println("Matched driver: " + ride2.getDriver().getDriverName() + " (expected: Kiran — only MINI)");
        System.out.println("Kiran status: " + kiran.getDriverStatus() + " (expected: ON_TRIP)");

        rideBooking.cancelRide(ride2.getRideId());
        System.out.println("Ride status after cancel: " + ride2.getStatus() + " (expected: CANCELLED)");
        System.out.println("Kiran status after cancel: " + kiran.getDriverStatus() + " (expected: AVAILABLE)");

        System.out.println();
        System.out.println("===== Failure: cancel after ride started should fail =====");
        Ride ride3 = rideBooking.processRider(amanPickup, amanDrop, aman, VehicleType.SEDAN);
        rideBooking.startRide(ride3.getRideId());
        try {
            rideBooking.cancelRide(ride3.getRideId());
            System.out.println("ERROR: should not reach here");
        } catch (InvalidCancelException e) {
            System.out.println("Correctly rejected: " + e.getMessage());
        }
        rideBooking.completeRide(ride3.getRideId());

        System.out.println();
        System.out.println("===== Failure: start a ride that's not MATCHED =====");
        try {
            rideBooking.startRide(ride3.getRideId());
            System.out.println("ERROR: should not reach here");
        } catch (InvalidStartException e) {
            System.out.println("Correctly rejected: " + e.getMessage());
        }

        System.out.println();
        System.out.println("===== Failure: complete a ride that's not STARTED =====");
        try {
            rideBooking.completeRide(ride3.getRideId());
            System.out.println("ERROR: should not reach here");
        } catch (InvalidCompleteException e) {
            System.out.println("Correctly rejected: " + e.getMessage());
        }

        System.out.println();
        System.out.println("===== Failure: rate a ride that's not COMPLETED =====");
        Ride ride4 = rideBooking.processRider(priyaPickup, priyaDrop, priya, VehicleType.MINI);
        try {
            rideBooking.rateDriver(ride4.getRideId(), 5.0);
            System.out.println("ERROR: should not reach here");
        } catch (InvalidRatingException e) {
            System.out.println("Correctly rejected: " + e.getMessage());
        }
        rideBooking.cancelRide(ride4.getRideId());

        System.out.println();
        System.out.println("===== Failure: no driver available for requested type =====");
        try {
            rideBooking.processRider(amanPickup, amanDrop, aman, VehicleType.SUV);
            System.out.println("ERROR: should not reach here");
        } catch (NoDriverAvailableException e) {
            System.out.println("Correctly rejected: " + e.getMessage());
        }

        System.out.println();
        System.out.println("===== Failure: unregistered rider requests a ride =====");
        Rider ghost = new Rider("Ghost");
        try {
            rideBooking.processRider(amanPickup, amanDrop, ghost, VehicleType.MINI);
            System.out.println("ERROR: should not reach here");
        } catch (RiderNotFoundException e) {
            System.out.println("Correctly rejected: " + e.getMessage());
        }

        System.out.println();
        System.out.println("===== Failure: duplicate driver registration =====");
        try {
            rideBooking.registerDriver(ravi);
            System.out.println("ERROR: should not reach here");
        } catch (DriverAlreadyExistException e) {
            System.out.println("Correctly rejected: " + e.getMessage());
        }

        System.out.println();
        System.out.println("===== Admin: price and surge update =====");
        System.out.println("Base price MINI before update: " + rideBooking.getBasePrice().get(VehicleType.MINI));
        admin.updatePrice(VehicleType.MINI, 12.0);
        System.out.println("Base price MINI after update: " + rideBooking.getBasePrice().get(VehicleType.MINI)
                + " (expected: 12.0)");

        admin.updateSurgeMultiplier(1.5);
        System.out.println("Surge multiplier: " + rideBooking.getSurgeMultiplier() + " (expected: 1.5)");

        Ride ride5 = rideBooking.processRider(priyaPickup, priyaDrop, priya, VehicleType.MINI);
        System.out.println("Estimated price with surge applied: " + ride5.getEstimatedPrice()
                + " (should reflect updated base price * 1.5 surge)");
        rideBooking.cancelRide(ride5.getRideId());

        // ---------------------------------------------------------------
        System.out.println();
        System.out.println("===== Concurrency: two drivers race to accept the same ride =====");

        Driver raceDriverA = new Driver("Deepak", "LIC010", new Vehicle("KA-02-0001", VehicleType.MINI),
                new Location(12.90, 77.60));
        Driver raceDriverB = new Driver("Nitin", "LIC011", new Vehicle("KA-02-0002", VehicleType.MINI),
                new Location(12.90, 77.60));
        Driver raceDriverC = new Driver("Farah", "LIC012", new Vehicle("KA-02-0003", VehicleType.MINI),
                new Location(12.90, 77.60));

        Ride raceRide = new Ride(VehicleType.MINI, aman, amanPickup, amanDrop, 25.0);

        List<Observer> responders = List.of(
                new TestDriverResponder(raceDriverA, true),
                new TestDriverResponder(raceDriverB, true),
                new TestDriverResponder(raceDriverC, false));

        ExecutorService executor = Executors.newFixedThreadPool(3);
        for (Observer responder : responders) {
            executor.submit(() -> responder.update(raceRide));
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("Ride status: " + raceRide.getStatus() + " (expected: MATCHED)");
        System.out.println("Assigned driver: "
                + (raceRide.getDriver() != null ? raceRide.getDriver().getDriverName() : "null")
                + " (expected: Deepak or Nitin, exactly one of them)");
        System.out.println("Deepak status: " + raceDriverA.getDriverStatus());
        System.out.println("Nitin status: " + raceDriverB.getDriverStatus());
        System.out.println("Farah status: " + raceDriverC.getDriverStatus() + " (expected: AVAILABLE — she rejected)");

        long onTripCount = List.of(raceDriverA, raceDriverB, raceDriverC).stream()
                .filter(d -> d.getDriverStatus().name().equals("ON_TRIP"))
                .count();
        System.out.println("Number of drivers ON_TRIP: " + onTripCount + " (expected: exactly 1 — proves lock worked)");
    }
}