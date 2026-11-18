package com.lld.problems.googledocs.code.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Comment {
    private final String commentId;
    private final String userId;
    private int start;
    private int end;
    private final String text;
    private final LocalDateTime createdAt;

    public Comment(String userId, int start, int end, String text) {
        this.commentId = UUID.randomUUID().toString();
        this.userId = userId;
        this.start = start;
        this.end = end;
        this.text = text;
        this.createdAt = LocalDateTime.now();
    }

    public String getCommentId() {
        return this.commentId;
    }

    public String getUserId() {
        return this.userId;
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

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
