package com.lld.problems.bookmyshow.code.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.lld.problems.bookmyshow.code.constants.BookMyShowConstants;
import com.lld.problems.bookmyshow.code.constants.SeatType;
import com.lld.problems.bookmyshow.code.constants.ShowType;
import com.lld.problems.bookmyshow.code.dto.ShowUpdateRequest;
import com.lld.problems.bookmyshow.code.exceptions.UserAlreadyExistException;
import com.lld.problems.bookmyshow.code.exceptions.UserNotFoundException;
import com.lld.problems.bookmyshow.code.observers.Subject;

public class Show implements Subject {
    private String showId;
    private String name;
    private double basePrice;
    private ShowType showType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Venue venue;
    private List<User> observers;
    private Queue<User> waitList;
    private Map<String, ShowSeat> showSeats;

    public Show(String id, String name, ShowType type, LocalDateTime startTime, LocalDateTime endTime, Venue venue,
            double basePrice) {
        this.showId = id;
        this.name = name;
        this.showType = type;
        this.basePrice = basePrice;
        this.startTime = startTime;
        this.endTime = endTime;
        this.venue = venue;
        this.observers = new ArrayList<>();
        this.waitList = new LinkedList<>();
        this.showSeats = new HashMap<>();
    }

    public void generateShowSeats() {
        this.showSeats = this.venue.getSeats().stream()
                .map(seat -> new ShowSeat(UUID.randomUUID().toString(), seat,
                        getSeatPrice(seat.getSeatType(), this.basePrice)))
                .collect(Collectors.toMap(
                        ShowSeat::getShowSeatId, Function.identity()));
    }

    public List<ShowSeat> getShowSeats() {
        return this.showSeats.values().stream().toList();
    }

    public Map<String, ShowSeat> getShowSeats(List<String> seatIds) {
        return seatIds.stream()
                .filter(this.showSeats::containsKey)
                .collect(Collectors.toMap(
                        key -> key,
                        key -> this.showSeats.get(key)));
    }

    public List<ShowSeat> getAvailableShowSeats() {
        return this.showSeats.values().stream().filter(showSeat -> showSeat.isAvailable()).toList();
    }

    public void updateShowDetails(ShowUpdateRequest request) {
        if (Objects.nonNull(request.getName())) {
            this.name = request.getName();
        }

        if (Objects.nonNull(request.getShowType())) {
            this.showType = request.getShowType();
        }

        if (Objects.nonNull(request.getStartTime())) {
            this.startTime = request.getStartTime();
        }

        if (Objects.nonNull(request.getEndTime())) {
            this.endTime = request.getEndTime();
        }

        notifyObservers(this.name + " has been updated");
    }

    public void notifyWaitList(String message) {
        if (this.waitList.isEmpty()) {
            return;
        }

        User user = this.waitList.poll();

        user.update(new Notification(UUID.randomUUID().toString(), message, user));
    }

    public void joinWaitList(User user) {
        if (isWaitListerExist(user.getUserId())) {
            throw new UserAlreadyExistException("[Join Wait List] User is already exist in WaitList!");
        }

        this.waitList.add(user);
    }

    public void leaveWaitList(String userId) {
        if (!isWaitListerExist(userId)) {
            throw new UserNotFoundException("[Leave Wait List] User is not exist in WaitList!");
        }

        this.waitList.removeIf(user -> user.getUserId().equals(userId));
    }

    @Override
    public void notifyObservers(String message) {
        for (User user : this.observers) {
            user.update(new Notification(UUID.randomUUID().toString(), message, user));
        }
    }

    @Override
    public void addObserver(User user) {
        if (isObserverExist(user.getUserId())) {
            throw new UserAlreadyExistException("[Error Add Observer] Observer already exist in observer list!");
        }

        this.observers.add(user);
    }

    @Override
    public void removeObserver(String userId) {
        if (!isObserverExist(userId)) {
            throw new UserNotFoundException("[Error Remove Observer] Observer is not exist in observer list!");
        }

        this.observers.removeIf(observer -> observer.getUserId().equals(userId));

    }

    public LocalDateTime getStarTime() {
        return this.startTime;
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    public String getShowId() {
        return this.showId;
    }

    public String getName() {
        return this.name;
    }

    public ShowType getShowType() {
        return this.showType;
    }

    public double getBasePrice() {
        return this.basePrice;
    }

    private boolean isObserverExist(String userId) {
        return this.observers.stream().anyMatch(observer -> observer.getUserId().equals(userId));
    }

    private boolean isWaitListerExist(String userId) {
        return this.waitList.stream().anyMatch(waitLister -> waitLister.getUserId().equals(userId));
    }

    private double getSeatPrice(SeatType type, double price) {
        return price + price * BookMyShowConstants.SEAT_PRICE_SHARE.get(type) / 100.0;
    }

}
