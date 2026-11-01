package com.lld.problems.jobprocessingsystem.code.models;

import java.util.Objects;

public class JobKey {
    private final String clientId;
    private final String content;

    public JobKey(String clientId, String content) {
        this.clientId = clientId;
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public String getClientId() {
        return this.clientId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof JobKey jkObj)) {
            return false;
        }

        return Objects.equals(this.clientId, jkObj.clientId) && Objects.equals(this.content, jkObj.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, content);
    }
}
