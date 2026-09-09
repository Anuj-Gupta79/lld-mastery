package com.lld.problems.commentsection.code;

import com.lld.problems.commentsection.code.exceptions.DeletedCommentException;
import com.lld.problems.commentsection.code.models.Comment;
import com.lld.problems.commentsection.code.models.Content;
import com.lld.problems.commentsection.code.models.User;

public class Main {

    public static void main(String[] args) {

        User alice = new User("Alice");
        User bob = new User("Bob");
        User charlie = new User("Charlie");

        Content post = new Content();

        System.out.println("===== 1. Add top-level comments =====");
        Comment c1 = new Comment("Great post!", alice);
        Comment c2 = new Comment("I disagree with this.", bob);
        post.addComment(c1);
        post.addComment(c2);
        printAllComments(post);

        System.out.println("\n===== 2. Add nested reply (reply to a reply) =====");
        Comment reply1 = new Comment("Why do you disagree?", charlie);
        c2.addReply(reply1);
        Comment reply1a = new Comment("Because the data doesn't support it.", bob);
        reply1.addReply(reply1a);
        printAllComments(post);

        System.out.println("\n===== 3. Like / Dislike toggle behavior =====");
        c1.like(bob);
        System.out.println("Bob liked c1 -> reactions: " + c1.getReactions());

        c1.like(bob); // toggle off
        System.out.println("Bob liked c1 again (toggle off) -> reactions: " + c1.getReactions());

        c1.dislike(bob);
        System.out.println("Bob disliked c1 -> reactions: " + c1.getReactions());

        c1.like(bob); // switch dislike -> like
        System.out.println("Bob liked c1 (switch from dislike) -> reactions: " + c1.getReactions());

        System.out.println("\n===== 4. Edit comment =====");
        c1.editComment("Great post! (edited)");
        System.out.println("c1 message after edit: " + c1.getMessage());

        System.out.println("\n===== 5. Delete comment (soft delete) =====");
        c2.delete();
        System.out.println("c2 deleted flag: " + c2.isDeleted());
        printAllComments(post);

        System.out.println("\n===== 6. Reply still allowed under deleted comment =====");
        Comment reply2 = new Comment("Replying even though parent is deleted.", alice);
        c2.addReply(reply2);
        printAllComments(post);

        System.out.println("\n===== 7. Like on deleted comment -> should throw =====");
        try {
            c2.like(charlie);
            System.out.println("ERROR: expected DeletedCommentException, but none thrown");
        } catch (DeletedCommentException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
        }

        System.out.println("\n===== 8. Dislike on deleted comment -> should throw =====");
        try {
            c2.dislike(charlie);
            System.out.println("ERROR: expected DeletedCommentException, but none thrown");
        } catch (DeletedCommentException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
        }

        System.out.println("\n===== 9. Edit on deleted comment -> should throw =====");
        try {
            c2.editComment("trying to edit a deleted comment");
            System.out.println("ERROR: expected DeletedCommentException, but none thrown");
        } catch (DeletedCommentException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
        }

        System.out.println("\n===== 10. Existing reactions on a comment untouched after it gets deleted =====");
        c1.delete();
        System.out.println("c1 deleted flag: " + c1.isDeleted());
        System.out.println("c1 reactions still intact (not wiped by delete): " + c1.getReactions());

        System.out.println("\n===== Final tree state =====");
        printAllComments(post);
    }

    private static void printAllComments(Content content) {
        for (Comment comment : content.getAllComments()) {
            printComment(comment, 0);
        }
    }

    private static void printComment(Comment comment, int depth) {
        String indent = "  ".repeat(depth);
        String displayMessage = comment.isDeleted() ? "[deleted]" : comment.getMessage();
        System.out.println(indent + "- (" + comment.author().getUserName() + "): " + displayMessage
                + "  [reactions=" + comment.getReactions() + "]");
        for (Comment reply : comment.getReplies()) {
            printComment(reply, depth + 1);
        }
    }
}