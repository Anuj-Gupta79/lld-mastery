package com.lld.problems.bookmyshow.code.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.lld.problems.bookmyshow.code.dto.VenueUpdateRequest;
import com.lld.problems.bookmyshow.code.exceptions.BookingNotFoundException;
import com.lld.problems.bookmyshow.code.exceptions.ShowNotFoundException;
import com.lld.problems.bookmyshow.code.exceptions.ShowSeatsNotAvailableException;
import com.lld.problems.bookmyshow.code.exceptions.UserAlreadyExistException;
import com.lld.problems.bookmyshow.code.exceptions.UserNotFoundException;
import com.lld.problems.bookmyshow.code.exceptions.VendorAlreadyExistException;
import com.lld.problems.bookmyshow.code.exceptions.VendorNotFoundException;
import com.lld.problems.bookmyshow.code.exceptions.VenueAlreadyExistException;
import com.lld.problems.bookmyshow.code.exceptions.VenueNotFoundException;
import com.lld.problems.bookmyshow.code.models.Booking;
import com.lld.problems.bookmyshow.code.models.Show;
import com.lld.problems.bookmyshow.code.models.ShowSeat;
import com.lld.problems.bookmyshow.code.models.Ticket;
import com.lld.problems.bookmyshow.code.models.User;
import com.lld.problems.bookmyshow.code.models.Vendor;
import com.lld.problems.bookmyshow.code.models.Venue;
import com.lld.problems.bookmyshow.code.services.BookingService;
import com.lld.problems.bookmyshow.code.services.CancellationService;

public class BookMyShow {
    private Map<String, Venue> venues;
    private Map<String, User> users;
    private Map<String, Booking> bookings;
    private Map<String, Vendor> vendors;
    private BookingService bookingService;
    private CancellationService cancellationService;

    public BookMyShow() {
        this.venues = new HashMap<>();
        this.users = new HashMap<>();
        this.bookings = new HashMap<>();
        this.vendors = new HashMap<>();
        this.bookingService = new BookingService();
        this.cancellationService = new CancellationService();
    }

    public void registerUser(User user) {
        if (this.users.containsKey(user.getUserId())) {
            throw new UserAlreadyExistException("[Error Register User] Requested user already exist in the system!");
        }

        this.users.put(user.getUserId(), user);
    }

    public void deleteUser(String userId) {
        if (!this.users.containsKey(userId)) {
            throw new UserNotFoundException("[Error Delete User] Requested user is not exist in the system!");
        }

        this.users.get(userId).deactivate();
    }

    public void registerVendor(Vendor vendor) {
        if (this.vendors.containsKey(vendor.getVendorId())) {
            throw new VendorAlreadyExistException(
                    "[Error Register Vendor] Requested vendor is already present in the system!");
        }

        this.vendors.put(vendor.getVendorId(), vendor);
    }

    public void addVenue(String vendorId, Venue venue) {
        if (!this.vendors.containsKey(vendorId)) {
            throw new VendorNotFoundException("[Error Add Venue] Requested vendor does not exist in the system!");
        }

        if (this.venues.containsKey(venue.getVenueId())) {
            throw new VenueAlreadyExistException("[Error Add Venue] Requested venue is already exist in the system!");
        }

        this.vendors.get(vendorId).addVenue(venue);
        this.venues.put(venue.getVenueId(), venue);
    }

    public void updateVenue(String vendorId, String venueId, VenueUpdateRequest request) {
        if (!this.vendors.containsKey(vendorId)) {
            throw new VendorNotFoundException("[Error Update Venue] Requested vendor does not exist in the system!");
        }

        this.vendors.get(vendorId).updateVenue(venueId, request);
    }

    public Ticket bookShow(String userId, String showId, String venueId, List<String> seatIds) {
        if (!this.users.containsKey(userId)) {
            throw new UserNotFoundException("[Error Book Show] User is not exist in the system!");
        }

        User user = this.users.get(userId);

        if (!this.venues.containsKey(venueId)) {
            throw new VenueNotFoundException("[Error Book Show] Venue is not exist in the system!");
        }

        if (Objects.isNull(this.venues.get(venueId).getShow(showId))) {
            throw new ShowNotFoundException("[Error Book Show] Show is not present in the system!");
        }

        Show show = this.venues.get(venueId).getShow(showId);
        Map<String, ShowSeat> requestedSeats = show.getShowSeats(seatIds);

        if (seatIds.size() != requestedSeats.size()) {
            throw new ShowSeatsNotAvailableException("[Error Book Show] Show Seat is not available!");
        }

        Ticket ticket = bookingService.bookShow(user, show, requestedSeats);

        this.bookings.put(ticket.getBooking().getBookingId(), ticket.getBooking());

        return ticket;
    }

    public void cancelBooking(String bookingId) {
        if (!this.bookings.containsKey(bookingId)) {
            throw new BookingNotFoundException("[Error Cancel Booking] Requested booking is not exist in the System!");
        }

        Booking booking = this.bookings.get(bookingId);

        this.cancellationService.cancelBooking(booking);
    }

    public List<Show> searchShow(String query) {
        List<Show> shows = new ArrayList<>();
        for (Venue venue : this.venues.values()) {
            for (Show show : venue.getShows()) {
                if (show.getName().toLowerCase().contains(query.toLowerCase())) {
                    shows.add(show);
                }
            }
        }

        return shows;
    }
}
