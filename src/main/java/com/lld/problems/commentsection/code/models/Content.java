package com.lld.problems.commentsection.code.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Content {
    private String contentId;
    private List<Comment> comments;

    public Content() {
        this.contentId = UUID.randomUUID().toString();
        this.comments = new ArrayList<>();
    }

    public String getContentId() {
        return this.contentId;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public List<Comment> getAllComments() {
        return this.comments;
    }
}
