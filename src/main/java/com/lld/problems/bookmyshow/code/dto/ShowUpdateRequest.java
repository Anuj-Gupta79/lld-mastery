package com.lld.problems.bookmyshow.code.dto;

import java.time.LocalDateTime;

import com.lld.problems.bookmyshow.code.constants.ShowType;

public class ShowUpdateRequest {
    private final String name;
    private final ShowType showType;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    private ShowUpdateRequest(Builder builder) {
        this.name = builder.name;
        this.showType = builder.showType;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
    }

    public static class Builder {
        private String name;
        private ShowType showType;
        private LocalDateTime startTime;
        private LocalDateTime endTime;

        public ShowUpdateRequest build() {
            return new ShowUpdateRequest(this);
        }

        public Builder setEventName(String name) {
            this.name = name;
            return this;
        }

        public Builder setEventType(ShowType type) {
            this.showType = type;
            return this;
        }

        public Builder setStartTime(LocalDateTime time) {
            this.startTime = time;
            return this;
        }

        public Builder setEndTime(LocalDateTime time) {
            this.endTime = time;
            return this;
        }
    }

    public String getName() {
        return this.name;
    }

    public ShowType getShowType() {
        return this.showType;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
    }
}
