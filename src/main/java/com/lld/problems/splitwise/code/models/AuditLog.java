package com.lld.problems.splitwise.code.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class AuditLog {
    private String logId;
    private LocalDateTime timestamp;
    private String action;
    private String actorId;

    public AuditLog(String action, String actorId) {
        this.logId = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.action = action;
        this.actorId = actorId;
    }

    public String getLogId() {
        return this.logId;
    }

    public String getAction() {
        return this.action;
    }

    public String getActorId() {
        return this.actorId;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public String getDetails() {
        return this.timestamp + ": action=" + this.action + " performed by " + this.actorId;
    }

}
