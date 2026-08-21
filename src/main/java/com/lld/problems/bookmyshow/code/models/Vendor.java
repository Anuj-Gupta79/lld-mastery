package com.lld.problems.bookmyshow.code.models;

import java.util.HashMap;
import java.util.Map;

import com.lld.problems.bookmyshow.code.dto.ShowUpdateRequest;
import com.lld.problems.bookmyshow.code.dto.VenueUpdateRequest;
import com.lld.problems.bookmyshow.code.exceptions.VenueAlreadyExistException;
import com.lld.problems.bookmyshow.code.exceptions.VenueNotFoundException;

public class Vendor {
    private String vendorId;
    private String name;
    private Map<String, Venue> venues;

    public Vendor(String id, String name) {
        this.vendorId = id;
        this.name = name;
        this.venues = new HashMap<>();
    }

    public void addVenue(Venue venue) {
        if (this.venues.containsKey(venue.getVenueId())) {
            throw new VenueAlreadyExistException("[Error Add Venue] This venue is already present!");
        }

        this.venues.put(venue.getVenueId(), venue);
    }

    public void removeVenue(String venueId) {
        if (this.venues.containsKey(venueId)) {
            this.venues.remove(venueId);
            return;
        }

        throw new VenueNotFoundException("[Error Remove Venue] Venue not found!");
    }

    public void updateVenue(String venueId, VenueUpdateRequest request) {
        if (this.venues.containsKey(venueId)) {
            this.venues.get(venueId).updateVenueDetails(request);
            return;
        }

        throw new VenueNotFoundException("[Error Update Venue] Venue not found!");
    }

    public void addShow(String venueId, Show show) {
        if (this.venues.containsKey(venueId)) {
            this.venues.get(venueId).addShow(show);
            show.generateShowSeats();
            return;
        }

        throw new VenueNotFoundException("[Error Add Show] Venue not found!");
    }

    public void removeShow(String venueId, String showId) {
        if (this.venues.containsKey(venueId)) {
            this.venues.get(venueId).removeShow(showId);
            return;
        }

        throw new VenueNotFoundException("[Error Remove Show] Venue not found!");
    }

    public void updateShow(String venueId, String showId, ShowUpdateRequest request) {
        if (this.venues.containsKey(venueId)) {
            this.venues.get(venueId).updateShowDetails(showId, request);
            return;
        }

        throw new VenueNotFoundException("[Error Update Show] Venue not found!");
    }

    public String getVendorId() {
        return this.vendorId;
    }

    public String getName() {
        return this.name;
    }
}
