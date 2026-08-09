package com.lld.problems.library.code.models;

import com.lld.problems.library.code.constants.BookStatus;

public class BookItem {
    private String bookItemId;
    private String parentId;
    private BookStatus status;

    public BookItem(String id, String parentId) {
        this.bookItemId = id;
        this.parentId = parentId;
        this.status = BookStatus.AVAILABLE;
    }

    public String getBookItemId() {
        return this.bookItemId;
    }

    public String getParentId() {
        return this.parentId;
    }

    public BookStatus getCurrStatus() {
        return this.status;
    }

    public void updateStatus(BookStatus status) {
        this.status = status;
    }
}
