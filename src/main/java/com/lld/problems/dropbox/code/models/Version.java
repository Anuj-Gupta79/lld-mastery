package com.lld.problems.dropbox.code.models;

import java.time.LocalDateTime;

public class Version {
    private int versionNumber;
    private LocalDateTime timestamp;

    public Version(int versionNumber) {
        this.versionNumber = versionNumber;
        this.timestamp = LocalDateTime.now();
    }

    public int getVersionNumber() {
        return this.versionNumber;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }
}
