package com.lld.problems.bookmyshow.code.dto;

public class VenueUpdateRequest {
    private final String name;
    private final String address;

    private VenueUpdateRequest(Builder builder) {
        this.name = builder.name;
        this.address = builder.address;
    }
    
    public static class Builder {
        private String name;
        private String address;

        public VenueUpdateRequest build() {
            return new VenueUpdateRequest(this);
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }
}
