package com.lld.problems.ridebooking.code.strategy;

import java.util.List;

import com.lld.problems.ridebooking.code.constants.VehicleType;
import com.lld.problems.ridebooking.code.models.Driver;
import com.lld.problems.ridebooking.code.models.Location;

public interface DriverMatchingStrategy {

    public List<Driver> findDriver(VehicleType vehicleType, Location pickLocation, List<Driver> drivers);
}
