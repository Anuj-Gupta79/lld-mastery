package com.lld.problems.googledocs.code.operations;

import com.lld.problems.googledocs.code.models.Document;

public abstract class Operation {
    private final String userId;
    private final int baseRevision;

    public Operation(String userId, int baseRevision) {
        this.userId = userId;
        this.baseRevision = baseRevision;
    }

    public String getUserId() {
        return this.userId;
    }

    public int getBaseRevision() {
        return this.baseRevision;
    }

    public Operation capture(Document document) {
        return this;
    }

    public abstract Operation transformAgainst(Operation applied);

    public abstract void apply(Document document);

    public abstract int shift(int position, boolean moveOnTie);

    public abstract Operation withBase(int revision);

    public abstract Operation inverse();
}
