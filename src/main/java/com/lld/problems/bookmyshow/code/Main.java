package com.lld.problems.bookmyshow.code;

import java.time.LocalDateTime;
import java.util.List;

import com.lld.problems.bookmyshow.code.constants.SeatType;
import com.lld.problems.bookmyshow.code.constants.ShowType;
import com.lld.problems.bookmyshow.code.core.BookMyShow;
import com.lld.problems.bookmyshow.code.dto.ShowUpdateRequest;
import com.lld.problems.bookmyshow.code.exceptions.ShowSeatsNotAvailableException;
import com.lld.problems.bookmyshow.code.models.Booking;
import com.lld.problems.bookmyshow.code.models.Seat;
import com.lld.problems.bookmyshow.code.models.Show;
import com.lld.problems.bookmyshow.code.models.Ticket;
import com.lld.problems.bookmyshow.code.models.User;
import com.lld.problems.bookmyshow.code.models.Venue;
import com.lld.problems.bookmyshow.code.models.Vendor;

public class Main {

    public static void main(String[] args) {
        BookMyShow bookMyShow = new BookMyShow();

        // ---------- Vendor registers with BookMyShow, then sets up Venue, Seats, Show
        // ----------
        Vendor vendor = new Vendor("vendor-1", "PVR Cinemas");
        bookMyShow.registerVendor(vendor);

        Venue venue = new Venue("venue-1", "PVR Forum Mall", "Koramangala, Bengaluru");
        bookMyShow.addVenue(vendor.getVendorId(), venue);

        Seat seatA1 = new Seat("seat-A1", 1, 1, SeatType.REGULAR);
        Seat seatA2 = new Seat("seat-A2", 1, 2, SeatType.PREMIUM);
        Seat seatA3 = new Seat("seat-A3", 1, 3, SeatType.VIP);
        venue.addSeat(seatA1);
        venue.addSeat(seatA2);
        venue.addSeat(seatA3);

        Show show = new Show(
                "show-1",
                "Avengers: Endgame",
                ShowType.MOVIE,
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(5).plusHours(3),
                venue,
                200.0 // base price set by vendor
        );
        vendor.addShow("venue-1", show); // internally generates ShowSeats with type-based pricing

        System.out.println("===== Show Seats Generated (with pricing) =====");
        show.getShowSeats().forEach(showSeat -> System.out.println(
                "ShowSeatId=" + showSeat.getShowSeatId()
                        + ", seatType=" + showSeat.getSeat().getSeatType()
                        + ", price=" + showSeat.getPrice()));

        // ---------- Users register ----------
        User alice = new User("user-1", "Alice", "alice@example.com");
        User bob = new User("user-2", "Bob", "bob@example.com");
        bookMyShow.registerUser(alice);
        bookMyShow.registerUser(bob);

        // Resolve the actual ShowSeat ids generated (ids are UUIDs, unknown ahead of
        // time)
        String showSeatIdForA1 = show.getShowSeats().stream()
                .filter(ss -> ss.getSeat().getSeatId().equals("seat-A1"))
                .findFirst().get().getShowSeatId();
        String showSeatIdForA2 = show.getShowSeats().stream()
                .filter(ss -> ss.getSeat().getSeatId().equals("seat-A2"))
                .findFirst().get().getShowSeatId();

        // ---------- Happy Path: Alice books seat A1 ----------
        System.out.println("\n===== Happy Path: Alice books seat A1 =====");
        Ticket aliceTicket = bookMyShow.bookShow(
                alice.getUserId(), show.getShowId(), venue.getVenueId(), List.of(showSeatIdForA1));
        System.out.println(aliceTicket);
        System.out.println("Booking status=" + aliceTicket.getBooking().getBookingStatus()
                + ", price=" + aliceTicket.getBooking().getPrice());

        // ---------- Failure Path: Bob tries to book the same seat A1 (already booked)
        // ----------
        System.out.println("\n===== Failure Path: Bob tries booking the same seat A1 =====");
        try {
            bookMyShow.bookShow(bob.getUserId(), show.getShowId(), venue.getVenueId(), List.of(showSeatIdForA1));
        } catch (ShowSeatsNotAvailableException e) {
            System.out.println("Booking failed as expected: " + e.getMessage());
        }

        // ---------- Bob joins the waitlist for seat A1's show since it's unavailable
        // ----------
        System.out.println("\n===== Bob joins the waitlist =====");
        show.joinWaitList(bob);
        System.out.println("Bob added to waitlist.");

        // ---------- Bob books a different seat (A2) successfully, becomes an observer
        // ----------
        System.out.println("\n===== Bob books seat A2 instead =====");
        Ticket bobTicket = bookMyShow.bookShow(
                bob.getUserId(), show.getShowId(), venue.getVenueId(), List.of(showSeatIdForA2));
        System.out.println(bobTicket);

        // ---------- Vendor updates show details -> notifies observers (Alice, Bob)
        // ----------
        System.out.println("\n===== Vendor updates show timing (notifies observers) =====");
        ShowUpdateRequest updateRequest = new ShowUpdateRequest.Builder()
                .setStartTime(LocalDateTime.now().plusDays(6))
                .setEndTime(LocalDateTime.now().plusDays(6).plusHours(3))
                .build();
        vendor.updateShow(venue.getVenueId(), show.getShowId(), updateRequest);

        // ---------- Alice cancels her booking (>=2 days before show -> 80% refund)
        // ----------
        System.out.println("\n===== Alice cancels her booking (refund + waitlist notify) =====");
        Booking aliceBooking = aliceTicket.getBooking();
        bookMyShow.cancelBooking(aliceBooking.getBookingId());
        System.out.println("Alice's booking status after cancellation=" + aliceBooking.getBookingStatus());
        System.out.println("(Bob should have received a waitlist notification above, seat A1 is free again)");

        // ---------- Verify seat A1 is available again ----------
        System.out.println("\n===== Seat A1 availability after cancellation =====");
        boolean isA1Available = show.getShowSeats(List.of(showSeatIdForA1))
                .get(showSeatIdForA1)
                .isAvailable();
        System.out.println("Seat A1 available=" + isA1Available);

        // ---------- Delete Bob (soft delete) ----------
        System.out.println("\n===== Bob deletes his account (soft delete) =====");
        bookMyShow.deleteUser(bob.getUserId());
        System.out.println("Bob active=" + bob.isActive());

        // ---------- Search shows ----------
        System.out.println("\n===== Search shows by 'avengers' =====");
        List<Show> results = bookMyShow.searchShow("avengers");
        results.forEach(s -> System.out.println("Found show: " + s.getName()));
    }
}