package com.lld.problems.googledocs.code.operations;

import com.lld.problems.googledocs.code.models.Document;

public class DeleteOperation extends Operation {
    private final int start;
    private final int end;
    private final String text;

    DeleteOperation(String userId, int baseRevision, int start, int end, String text) {
        super(userId, baseRevision);
        this.start = start;
        this.end = end;
        this.text = text;
    }

    public DeleteOperation(String userId, int baseRevision, int start, int end) {
        this(userId, baseRevision, start, end, null);
    }

    @Override
    public void apply(Document document) {
        document.deleteText(start, end);
    }

    @Override
    public int shift(int p, boolean moveOnTie) {
        if (p >= end) {
            return p - (this.end - this.start);
        }

        if (this.start < p && p < this.end) {
            return this.start;
        }

        return p;
    }

    @Override
    public Operation capture(Document document) {
        return new DeleteOperation(getUserId(), getBaseRevision(), this.start, this.end, document.getText(start, end));
    }

    @Override
    public Operation transformAgainst(Operation applied) {
        int newStart = applied.shift(this.start, true);
        int newEnd = applied.shift(this.end, false);

        if (newStart >= newEnd) {
            return null;
        }

        return new DeleteOperation(getUserId(), getBaseRevision(), newStart, newEnd);
    }

    @Override
    public Operation withBase(int revision) {
        return new DeleteOperation(getUserId(), revision, this.start, this.end, this.text);
    }

    @Override
    public Operation inverse() {
        return new InsertOperation(getUserId(), getBaseRevision(), this.start, this.text);
    }

    public int getStart() {
        return this.start;
    }

    public int getEnd() {
        return this.end;
    }

    public String getText() {
        return this.text;
    }
}
