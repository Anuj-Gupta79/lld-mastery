package com.lld.problems.library.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lld.problems.library.code.constants.BookStatus;
import com.lld.problems.library.code.constants.RentalType;
import com.lld.problems.library.code.exceptions.BookItemNotFoundException;
import com.lld.problems.library.code.exceptions.BookNotFoundException;
import com.lld.problems.library.code.exceptions.BookUnAvailableException;
import com.lld.problems.library.code.exceptions.BorrowLimitExceedsException;
import com.lld.problems.library.code.exceptions.PassExpiredException;
import com.lld.problems.library.code.exceptions.TransactionNotFoundException;
import com.lld.problems.library.code.exceptions.UserNotFoundException;
import com.lld.problems.library.code.models.Book;
import com.lld.problems.library.code.models.BookItem;
import com.lld.problems.library.code.models.LibraryContext;
import com.lld.problems.library.code.models.Receipt;
import com.lld.problems.library.code.models.Transaction;
import com.lld.problems.library.code.models.User;
import com.lld.problems.library.code.services.PaymentService;

public class Library {
    private Map<String, Book> books;
    private Map<String, BookItem> bookItems;
    private Map<String, Transaction> transactions;
    private Map<String, User> users;
    private PaymentService paymentService;
    private int expirationDays;
    private double latePenaltyFee;
    private double damagePenaltyFee;
    private double passCreationFee;
    private int maxBooksAllowed;

    public Library(LibraryContext context) {
        this.books = new HashMap<>();
        this.bookItems = new HashMap<>();
        this.transactions = new HashMap<>();
        this.users = new HashMap<>();
        this.expirationDays = context.getExpirationDays();
        this.latePenaltyFee = context.getLatePenaltyFee();
        this.damagePenaltyFee = context.getDamagePenaltyFee();
        this.passCreationFee = context.getPassCreationFee();
        this.maxBooksAllowed = context.getMaxBookAllowed();
        this.paymentService = new PaymentService();
    }

    public Receipt borrowBook(RentalType rentalType, String userId, String bookId) {
        User user = getUser(userId);

        passValidation(user);

        Book book = getBook(bookId);
        BookItem bookItem = getBookItem(book);

        borrowLimitValidation(user);

        Transaction transaction = new Transaction(this.paymentService);
        Receipt receipt = transaction.recordBorrowBook(rentalType, bookItem);

        this.transactions.put(transaction.getTransactionId(), transaction);

        updateBookItemStatus(BookStatus.ALLOCATE, bookItem.getBookItemId());
        user.updateCurrentBooksHeld(1);

        return receipt;
    }

    public Receipt returnBook(Receipt receipt, String userId) {

        User user = getUser(userId);
        Transaction transaction = getTransaction(receipt.getTransactionId());
        BookItem bookItem = getBookItem(receipt.getBookItemId());

        receipt = transaction.recordReturnBook(user.getPass(), bookItem, this.latePenaltyFee,
                this.damagePenaltyFee);

        if (bookItem.getCurrStatus() == BookStatus.ALLOCATE) {
            updateBookItemStatus(BookStatus.AVAILABLE, bookItem.getBookItemId());
        } else if (bookItem.getCurrStatus() == BookStatus.DAMAGE) {
            removeBookItem(bookItem.getBookItemId());
        }

        user.updateCurrentBooksHeld(-1);

        return receipt;
    }

    public void registerUser(User user) {
        user.createPass(this.expirationDays, this.passCreationFee, paymentService);
        this.users.put(user.getUserId(), user);
    }

    public void updateBookItemStatus(BookStatus bookStatus, String bookItemId) {
        this.bookItems.get(bookItemId).updateStatus(bookStatus);
    }

    public void addBook(Book book) {
        this.books.put(book.getBookId(), book);
    }

    public void addBookItem(BookItem bookItem) {
        this.books.get(bookItem.getParentId()).addBookItem(bookItem.getBookItemId());
        this.bookItems.put(bookItem.getBookItemId(), bookItem);
    }

    public void removeBook(String bookId) {
        Book book = this.books.get(bookId);
        this.books.remove(bookId);

        for (String bookItemId : book.getBookItemsIds()) {
            this.bookItems.remove(bookItemId);
        }
    }

    public void removeBookItem(String bookItemId) {
        BookItem bookItem = this.bookItems.get(bookItemId);
        this.books.get(bookItem.getParentId()).removeBookItem(bookItemId);
        this.bookItems.remove(bookItemId);
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactionsList = new ArrayList<>();

        for (Transaction transaction : transactions.values()) {
            transactionsList.add(transaction);
        }
        return transactionsList;
    }

    private User getUser(String userId) {
        if (this.users.containsKey(userId)) {
            return this.users.get(userId);
        }

        throw new UserNotFoundException("User with userId: " + userId + " not present");
    }

    private void passValidation(User user) {
        if (!user.getPass().isActive()) {
            throw new PassExpiredException("Pass has been Expired! Need to renew the pass");
        }
    }

    private Book getBook(String bookId) {
        if (books.containsKey(bookId)) {
            return this.books.get(bookId);
        }

        throw new BookNotFoundException("Requested Book is not found in Library System!");
    }

    private BookItem getBookItem(Book book) {
        for (String bookItemId : book.getBookItemsIds()) {
            if (bookItems.containsKey(bookItemId)
                    && bookItems.get(bookItemId).getCurrStatus() == BookStatus.AVAILABLE) {
                return bookItems.get(bookItemId);
            }
        }

        throw new BookUnAvailableException("Requested Book is not available!");
    }

    private BookItem getBookItem(String bookItemId) {
        if (bookItems.containsKey(bookItemId)) {
            return bookItems.get(bookItemId);
        }

        throw new BookItemNotFoundException("Requested Book is not found!");
    }

    private void borrowLimitValidation(User user) {
        if (user.getCurrentBooksHeld() >= this.maxBooksAllowed) {
            throw new BorrowLimitExceedsException("User has been exceeded its borrow limit!");
        }
    }

    private Transaction getTransaction(String transactionId) {
        if (transactions.containsKey(transactionId)) {
            return transactions.get(transactionId);
        }

        throw new TransactionNotFoundException("There is no transaction exist, in provided receipt: " + transactionId);
    }
}
