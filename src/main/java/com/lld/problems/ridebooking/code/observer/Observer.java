package com.lld.problems.ridebooking.code.observer;

import com.lld.problems.ridebooking.code.models.Ride;

public interface Observer {
    public void update(Ride ride);
}
