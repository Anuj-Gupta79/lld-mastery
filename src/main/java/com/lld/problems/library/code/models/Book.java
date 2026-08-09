package com.lld.problems.library.code.models;

import java.util.List;

public class Book {
    private String bookId;
    private String title;
    private String author;
    private List<String> bookItemIds;

    public Book(String id, String title, String author, List<String> bookItems) {
        this.bookId = id;
        this.title = title;
        this.author = author;
        this.bookItemIds = bookItems;
    }

    public String getBookId() {
        return this.bookId;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public List<String> getBookItemsIds() {
        return this.bookItemIds;
    }

    public void addBookItem(String bookItemId) {
        this.bookItemIds.add(bookItemId);
    }

    public void removeBookItem(String bookItemId) {
        int ind = this.bookItemIds.indexOf(bookItemId);
        this.bookItemIds.remove(ind);
    }

}
