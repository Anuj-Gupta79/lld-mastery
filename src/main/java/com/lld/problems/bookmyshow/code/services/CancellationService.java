package com.lld.problems.bookmyshow.code.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import com.lld.problems.bookmyshow.code.constants.BookingStatus;
import com.lld.problems.bookmyshow.code.models.Booking;
import com.lld.problems.bookmyshow.code.models.Notification;
import com.lld.problems.bookmyshow.code.models.ShowSeat;

public class CancellationService {
    public void cancelBooking(Booking booking) {
        double refundAmount = getRefundAmount(booking.getShow().getStarTime(),
                booking.getPrice());

        for (ShowSeat showSeat : booking.getShowSeats().values()) {
            showSeat.release();
        }

        booking.getUser().update(new Notification(UUID.randomUUID().toString(),
                "Refund amount after cancellation=" + refundAmount, booking.getUser()));

        booking.updateStatus(BookingStatus.CANCELLED);

        booking.getShow()
                .notifyWaitList("Show Seat has been free for the show.");
    }

    private double getRefundAmount(LocalDateTime showStartTime, double price) {
        if (ChronoUnit.DAYS.between(LocalDateTime.now(), showStartTime) >= 2) {
            return price - price * 0.2;
        }
        return 0.0;
    }
}
