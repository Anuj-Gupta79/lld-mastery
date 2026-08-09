package com.lld.problems.library.code.models;

import java.time.LocalDate;

public class Pass {
    private LocalDate expirationDate;

    public Pass(int validityDays) {
        this.expirationDate = LocalDate.now().plusDays(validityDays);
    }

    public boolean isActive() {
        return LocalDate.now().isBefore(expirationDate);
    }

    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }

    public void extendExpirationDate(int validityDays) {
        this.expirationDate = this.expirationDate.plusDays(validityDays);
    }

    public void setExpirationDate(int validityDays) {
        this.expirationDate = LocalDate.now().plusDays(validityDays);
    }
}
