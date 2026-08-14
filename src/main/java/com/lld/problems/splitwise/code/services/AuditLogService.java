package com.lld.problems.splitwise.code.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.lld.problems.splitwise.code.models.AuditLog;

public class AuditLogService {
    private List<AuditLog> logs;

    public AuditLogService() {
        this.logs = new ArrayList<>();
    }

    public void addLog(AuditLog log) {
        this.logs.add(log);
    }

    public List<AuditLog> getAllLogs() {
        return Collections.unmodifiableList(this.logs);
    }
}
