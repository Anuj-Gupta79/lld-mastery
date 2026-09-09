package com.lld.problems.commentsection.code.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.lld.problems.commentsection.code.constants.ReactionType;
import com.lld.problems.commentsection.code.exceptions.DeletedCommentException;

public class Comment {
    private String commentId;
    private String message;
    private User author;
    private List<Comment> replies;
    private Map<String, ReactionType> reactions;
    private boolean deleted;

    public Comment(String message, User author) {
        this.commentId = UUID.randomUUID().toString();
        this.message = message;
        this.author = author;
        this.replies = new ArrayList<>();
        this.reactions = new HashMap<>();
        this.deleted = false;
    }

    public void addReply(Comment comment) {
        this.replies.add(comment);
    }

    public void editComment(String newMessage) {
        if (this.deleted) {
            throw new DeletedCommentException(
                    "[Error Edit Comment] Action can not be performed as comment is deleted!");
        }

        this.message = newMessage;
    }

    public void like(User user) {
        if (this.deleted) {
            throw new DeletedCommentException("[Error Like] Action can not be performed as comment is deleted!");
        }

        if (this.reactions.containsKey(user.getUserId())) {
            if (this.reactions.get(user.getUserId()).equals(ReactionType.LIKE)) {
                this.reactions.remove(user.getUserId());
            } else {
                this.reactions.put(user.getUserId(), ReactionType.LIKE);
            }
        } else {
            this.reactions.put(user.getUserId(), ReactionType.LIKE);
        }

    }

    public void dislike(User user) {
        if (this.deleted) {
            throw new DeletedCommentException("[Error Dislike] Action can not be performed as comment is deleted!");
        }

        if (this.reactions.containsKey(user.getUserId())) {
            if (this.reactions.get(user.getUserId()).equals(ReactionType.DISLIKE)) {
                this.reactions.remove(user.getUserId());
            } else {
                this.reactions.put(user.getUserId(), ReactionType.DISLIKE);
            }
        } else {
            this.reactions.put(user.getUserId(), ReactionType.DISLIKE);
        }

    }

    public void delete() {
        this.deleted = true;
    }

    public String getCommentId() {
        return this.commentId;
    }

    public String getMessage() {
        return this.message;
    }

    public User author() {
        return this.author;
    }

    public List<Comment> getReplies() {
        return this.replies;
    }

    public Map<String, ReactionType> getReactions() {
        return this.reactions;
    }

    public boolean isDeleted() {
        return this.deleted;
    }
}
