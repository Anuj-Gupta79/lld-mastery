package com.lld.problems.googledocs.code.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Version {
    private final int number;
    private final List<StyleSpan> spans;
    private final String content;
    private final String authorId;
    private final LocalDateTime createdAt;

    public Version(int number, List<StyleSpan> spans, String content, String authorId) {
        this.number = number;
        this.spans = copyOf(spans);
        this.content = content;
        this.authorId = authorId;
        this.createdAt = LocalDateTime.now();
    }

    public int getNumber() {
        return this.number;
    }

    public List<StyleSpan> getSpans() {
        return copyOf(this.spans);
    }

    public String getContent() {
        return this.content;
    }

    public String getAuthorId() {
        return this.authorId;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    private static List<StyleSpan> copyOf(List<StyleSpan> spans) {
        List<StyleSpan> list = new ArrayList<>();

        for (StyleSpan style : spans) {
            list.add(style.copy());
        }
        return list;
    }
}
