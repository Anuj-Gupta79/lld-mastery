package com.lld.problems.googledocs.code.operations;

import com.lld.problems.googledocs.code.constants.TextStyle;
import com.lld.problems.googledocs.code.models.Document;

public class FormatOperation extends Operation {
    private final int start;
    private final int end;
    private final TextStyle style;
    private final boolean on;

    public FormatOperation(String userId, int baseRevision, int start, int end, TextStyle style, boolean on) {
        super(userId, baseRevision);
        this.start = start;
        this.end = end;
        this.style = style;
        this.on = on;
    }

    @Override
    public void apply(Document document) {
        document.applyStyle(start, end, style, on);
    }

    @Override
    public Operation transformAgainst(Operation applied) {
        int newStart = applied.shift(this.start, true);
        int newEnd = applied.shift(this.end, false);

        if (newStart >= newEnd) {
            return null;
        }

        return new FormatOperation(getUserId(), getBaseRevision(), newStart, newEnd, this.style,
                this.on);
    }

    @Override
    public int shift(int p, boolean moveOnTie) {
        return p;
    }

    @Override
    public Operation withBase(int revision) {
        return new FormatOperation(getUserId(), revision, this.start, this.end, this.style, this.on);
    }

    @Override
    public Operation inverse() {
        return new FormatOperation(getUserId(), getBaseRevision(), this.start, this.end, this.style, !this.on);
    }

    public int getStart() {
        return this.start;
    }

    public int getEnd() {
        return this.end;
    }

    public TextStyle getStyle() {
        return this.style;
    }

    public boolean isOn() {
        return this.on;
    }
}
