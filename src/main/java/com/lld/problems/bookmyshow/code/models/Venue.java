package com.lld.problems.bookmyshow.code.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.lld.problems.bookmyshow.code.dto.SeatUpdateRequest;
import com.lld.problems.bookmyshow.code.dto.ShowUpdateRequest;
import com.lld.problems.bookmyshow.code.dto.VenueUpdateRequest;
import com.lld.problems.bookmyshow.code.exceptions.SeatAlreadyExistException;
import com.lld.problems.bookmyshow.code.exceptions.SeatNoFoundException;
import com.lld.problems.bookmyshow.code.exceptions.ShowAlreadyExistException;
import com.lld.problems.bookmyshow.code.exceptions.ShowNotFoundException;

public class Venue {
    private String venueId;
    private String name;
    private String address;
    private Map<String, Seat> seats;
    private Map<String, Show> shows;

    public Venue(String id, String name, String address) {
        this.venueId = id;
        this.name = name;
        this.address = address;
        this.seats = new HashMap<>();
        this.shows = new HashMap<>();
    }

    public void addSeat(Seat seat) {
        if (this.seats.containsKey(seat.getSeatId())) {
            throw new SeatAlreadyExistException(
                    "[Error Add Seat] Cannot add seat as Seat is already exist with seatId " + seat.getSeatId());
        }

        this.seats.put(seat.getSeatId(), seat);
    }

    public void removeSeat(String seatId) {
        if (this.seats.containsKey(seatId)) {
            this.seats.remove(seatId);
            return;
        }

        throw new SeatNoFoundException("[Error Remove Seat] Seat not exist with seatId " + seatId);
    }

    public void updateSeat(String seatId, SeatUpdateRequest request) {
        if (this.seats.containsKey(seatId)) {
            this.seats.get(seatId).updateSeatDetails(request);
            return;
        }

        throw new SeatNoFoundException("[Error Update Seat] Seat not exist with seatId " + seatId);
    }

    public List<Seat> getSeats() {
        return this.seats.values().stream().toList();
    }

    public List<Show> getShows() {
        return this.shows.values().stream().toList();
    }

    public Show getShow(String showId) {
        return this.shows.get(showId);
    }

    public void addShow(Show show) {
        if (this.shows.containsKey(show.getShowId())) {
            throw new ShowAlreadyExistException("[Error Add Show] Show already exist!");
        }

        this.shows.put(show.getShowId(), show);
    }

    public void removeShow(String showId) {
        if (!this.shows.containsKey(showId)) {
            throw new ShowNotFoundException("[Error Remove Show] Show is not present!");
        }

        this.shows.remove(showId);
    }

    public void updateShowDetails(String showId, ShowUpdateRequest request) {
        if (this.shows.containsKey(showId)) {
            this.shows.get(showId).updateShowDetails(request);
            return;
        }

        throw new ShowNotFoundException("[Error Update Show] Show is not present!");
    }

    public void updateVenueDetails(VenueUpdateRequest request) {
        if (Objects.nonNull(request.getName())) {
            this.name = request.getName();
        }

        if (Objects.nonNull(request.getAddress())) {
            this.address = request.getAddress();
        }
    }

    public String getVenueId() {
        return this.venueId;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }
}
