package com.lld.problems.library.code.models;

import java.util.List;

import com.lld.problems.library.code.Library;
import com.lld.problems.library.code.constants.BookStatus;

public class Owner {
    private String ownerId;
    private String ownerName;
    private Library library;

    public Owner(String id, String name, Library library) {
        this.ownerId = id;
        this.ownerName = name;
        this.library = library;
    }

    public void addBook(Book book) {
        this.library.addBook(book);
    }

    public void removeBook(String bookId) {
        this.library.removeBook(bookId);
    }

    public void addBookItem(BookItem bookItem) {
        this.library.addBookItem(bookItem);
    }

    public void removeBookItem(String bookItemId) {
        this.library.removeBookItem(bookItemId);
    }

    public List<Transaction> getAllTransactions() {
        return this.library.getAllTransactions();
    }
    
    public void markDamage(String bookItemId) {
        this.library.updateBookItemStatus(BookStatus.DAMAGE, bookItemId);
    }
    
    public String getOwnerId() {
        return this.ownerId;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public Library getLibrary() {
        return this.library;
    }
}
