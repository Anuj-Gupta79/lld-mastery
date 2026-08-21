package com.lld.problems.bookmyshow.code.services;

import java.util.Map;
import java.util.UUID;

import com.lld.problems.bookmyshow.code.constants.BookingStatus;
import com.lld.problems.bookmyshow.code.exceptions.PaymentFailureException;
import com.lld.problems.bookmyshow.code.exceptions.ShowSeatsNotAvailableException;
import com.lld.problems.bookmyshow.code.models.Booking;
import com.lld.problems.bookmyshow.code.models.Show;
import com.lld.problems.bookmyshow.code.models.ShowSeat;
import com.lld.problems.bookmyshow.code.models.Ticket;
import com.lld.problems.bookmyshow.code.models.User;

public class BookingService {
    private PaymentService paymentService;

    public BookingService() {
        this.paymentService = new PaymentService();
    }

    public Ticket bookShow(User user, Show show, Map<String, ShowSeat> requestedSeats) {
        Booking booking = new Booking(UUID.randomUUID().toString(), show, requestedSeats, user);

        if (!isAllShowSeatsAvailable(requestedSeats)) {
            throw new ShowSeatsNotAvailableException(
                    "[Error Booking] Seat is not available currently, please choose other one!");
        }

        for (ShowSeat showSeat : requestedSeats.values()) {
            showSeat.lock();
            booking.updatePrice(showSeat.getPrice());
        }

        if (!this.paymentService.processPayment(booking.getPrice())) {
            releaseLock(requestedSeats);
            booking.updateStatus(BookingStatus.FAILED);
            throw new PaymentFailureException("[Error Payment Processing] Payment has been failed!");
        } else {
            show.addObserver(user);
            booking.updateStatus(BookingStatus.CONFIRMED);
        }

        return new Ticket(booking);
    }

    private boolean isAllShowSeatsAvailable(Map<String, ShowSeat> requestedSeat) {
        for (ShowSeat showSeat : requestedSeat.values()) {
            if (!showSeat.isAvailable()) {
                return false;
            }
        }

        return true;
    }

    private void releaseLock(Map<String, ShowSeat> requestedSeats) {
        for (ShowSeat showSeat : requestedSeats.values()) {
            showSeat.release();
        }
    }
}
