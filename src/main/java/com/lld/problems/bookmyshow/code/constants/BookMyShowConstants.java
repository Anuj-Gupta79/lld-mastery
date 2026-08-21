package com.lld.problems.bookmyshow.code.constants;

import java.util.Map;

public class BookMyShowConstants {
    public static final int LOCK_EXPIRATION_MINUTES = 10;
    public static final Map<SeatType, Double> SEAT_PRICE_SHARE = Map.of(
            SeatType.VIP, 25.0,
            SeatType.PREMIUM, 10.0,
            SeatType.REGULAR, 0.0);
}
