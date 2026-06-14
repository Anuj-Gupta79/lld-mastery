package com.lld.problems.parkinglot.code.factory;

import com.lld.problems.parkinglot.code.constants.VehicleType;
import com.lld.problems.parkinglot.code.models.vehicle.Bike;
import com.lld.problems.parkinglot.code.models.vehicle.Car;
import com.lld.problems.parkinglot.code.models.vehicle.Truck;
import com.lld.problems.parkinglot.code.models.vehicle.Vehicle;

public class VehicleFactory {
    public Vehicle createVehicle(VehicleType vehicleType, String plateNumber, String ownerName) {
        switch (vehicleType) {
            case BIKE:
                return new Bike(plateNumber, ownerName);
            case CAR:
                return new Car(plateNumber, ownerName);
            case TRUCK:
                return new Truck(plateNumber, ownerName);
            default:
                throw new IllegalArgumentException("Invalid Vehicle Type");
        }
    }
}
