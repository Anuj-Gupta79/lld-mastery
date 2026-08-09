package com.lld.problems.library.code;

import java.util.ArrayList;

import com.lld.problems.library.code.constants.RentalType;
import com.lld.problems.library.code.models.Book;
import com.lld.problems.library.code.models.BookItem;
import com.lld.problems.library.code.models.LibraryContext;
import com.lld.problems.library.code.models.Owner;
import com.lld.problems.library.code.models.Receipt;
import com.lld.problems.library.code.models.User;

public class Main {
    public static void main(String[] args) {
        // Create library context
        LibraryContext libraryContext = new LibraryContext.Builder()
                .setExpirationDays(30)
                .setMaxBooksAllowed(4)
                .setDamagePenaltyFee(500)
                .setLatePenaltyFee(50)
                .setPassCreationFee(200)
                .build();

        // Create Library
        Library library = new Library(libraryContext);

        // Create Owner
        Owner owner = new Owner("1", "John Lee", library);

        // Create books and its copies
        Book book1 = new Book("1", "Jungle Book", "legend 1", new ArrayList<>());
        owner.addBook(book1);

        BookItem bookItemACopy1 = new BookItem("1A", "1");
        BookItem bookItemACopy2 = new BookItem("2A", "1");
        BookItem bookItemACopy3 = new BookItem("3A", "1");
        BookItem bookItemACopy4 = new BookItem("4A", "1");
        BookItem bookItemACopy5 = new BookItem("5A", "1");

        owner.addBookItem(bookItemACopy1);
        owner.addBookItem(bookItemACopy2);
        owner.addBookItem(bookItemACopy3);
        owner.addBookItem(bookItemACopy4);
        owner.addBookItem(bookItemACopy5);

        Book book2 = new Book("2", "The Lion King", "legend 2", new ArrayList<>());
        owner.addBook(book2);

        BookItem bookItemBCopy1 = new BookItem("1B", "2");
        BookItem bookItemBCopy2 = new BookItem("2B", "2");
        BookItem bookItemBCopy3 = new BookItem("3B", "2");

        owner.addBookItem(bookItemBCopy1);
        owner.addBookItem(bookItemBCopy2);
        owner.addBookItem(bookItemBCopy3);

        // User Registration
        User user1 = new User("1", "Alex");
        User user2 = new User("2", "Beet");
        User user3 = new User("3", "Will");

        library.registerUser(user1);
        library.registerUser(user2);
        library.registerUser(user3);

        // Borrow
        Receipt receipt1 = library.borrowBook(RentalType.SIT_IN, "1", "1");
        System.out.println("Receipt 1 after borrow: " + receipt1.toString());

        Receipt receipt2 = library.borrowBook(RentalType.BORROW, "2", "1");
        System.out.println("Receipt 2 after borrow: " + receipt2.toString());

        Receipt receipt3 = library.borrowBook(RentalType.BORROW, "3", "2");
        System.out.println("Receipt 3 after borrow: " + receipt3.toString());

        Receipt receipt4 = library.borrowBook(RentalType.BORROW, "1", "2");
        System.out.println("Receipt 4 after borrow: " + receipt4.toString());

        Receipt receipt5 = library.borrowBook(RentalType.BORROW, "2", "2");
        System.out.println("Receipt 5 after borrow: " + receipt5.toString());

        // Failure Borrow
        // Receipt receipt6 = library.borrowBook(RentalType.SIT_IN, "3", "2");
        // System.out.println("Receipt 6 after borrow: " + receipt6);

        // // Return
        library.returnBook(receipt1, "1");
        System.out.println("Receipt 1 after return: " + receipt1.toString());

        library.returnBook(receipt2, "2");
        System.out.println("Receipt 2 after return: " + receipt2.toString());

        library.returnBook(receipt3, "3");
        System.out.println("Receipt 3 after return: " + receipt3.toString());

        library.returnBook(receipt4, "1");
        System.out.println("Receipt 4 after return: " + receipt4.toString());

        owner.markDamage(receipt5.getBookItemId());
        library.returnBook(receipt5, "2");
        System.out.println("Receipt 5 after return: " + receipt5.toString());
    }
}
