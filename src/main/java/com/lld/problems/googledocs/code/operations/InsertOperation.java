package com.lld.problems.googledocs.code.operations;

import com.lld.problems.googledocs.code.models.Document;

public class InsertOperation extends Operation {
    private final int position;
    private final String text;

    public InsertOperation(String userId, int baseRevision, int position, String text) {
        super(userId, baseRevision);
        this.position = position;
        this.text = text;
    }

    @Override
    public void apply(Document document) {
        document.insertText(this.position, this.text);
    }

    @Override
    public Operation transformAgainst(Operation applied) {
        int newPosition = applied.shift(this.position, true);
        return new InsertOperation(getUserId(), getBaseRevision(), newPosition, this.text);
    }

    @Override
    public int shift(int p, boolean moveOnTie) {
        if (p > this.position || (p == this.position && moveOnTie)) {
            return p + this.text.length();
        }

        return p;
    }

    @Override
    public Operation withBase(int revision) {
        return new InsertOperation(getUserId(), revision, this.position, this.text);
    }

    @Override
    public Operation inverse() {
        return new DeleteOperation(getUserId(), getBaseRevision(), this.position, this.position + this.text.length(),
                this.text);
    }

    public int getPosition() {
        return this.position;
    }

    public String getText() {
        return this.text;
    }

}
