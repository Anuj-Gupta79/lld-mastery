package com.lld.problems.googledocs.code.models;

import com.lld.problems.googledocs.code.constants.TextStyle;

public class StyleSpan {
    private int start;
    private int end;
    private final TextStyle style;

    public StyleSpan(int start, int end, TextStyle style) {
        this.start = start;
        this.end = end;
        this.style = style;
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

    public StyleSpan copy(){
        return new StyleSpan(start, end, style);
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
