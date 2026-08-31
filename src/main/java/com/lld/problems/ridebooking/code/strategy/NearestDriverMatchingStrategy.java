package com.lld.problems.ridebooking.code.strategy;

import java.util.List;

import com.lld.problems.ridebooking.code.constants.VehicleType;
import com.lld.problems.ridebooking.code.models.Driver;
import com.lld.problems.ridebooking.code.models.Location;

public class NearestDriverMatchingStrategy implements DriverMatchingStrategy {
    // WHY: unitless degree-space threshold, not meters — distance() is a raw
    // Euclidean
    // approximation (~111km per degree of lat/long), so this roughly represents
    // ~2km
    private static final double MAX_MATCH_RADIUS = 0.02;

    @Override
    public List<Driver> findDriver(VehicleType vehicleType, Location pickLocation, List<Driver> drivers) {
        return drivers.stream()
                .filter(driver -> driver.getVehicle().getVehicleType() == vehicleType
                        && driver.isAvailable())
                .filter(driver -> distance(pickLocation, driver
                        .getLocation()) <= MAX_MATCH_RADIUS)
                .toList();
    }

    private double distance(Location a, Location b) {
        // WHY: Euclidean approx sufficient for LLD demo, real systems use
        // haversine/road-network distance
        double dx = a.getLatitude() - b.getLatitude();
        double dy = a.getLongitude() - b.getLongitude();
        return Math.sqrt(dx * dx + dy * dy);
    }

}