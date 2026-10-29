package com.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    private static final LocalDate DAY =
            LocalDate.of(2026, 10, 1);

    private Library library;
    private Book book;
    private Copy copy;
    private Member member;

    @BeforeEach
    void setUp() {
        library = new Library();

        book = new Book(
                "978-0-00-000001-1",
                "Software Engineering",
                2016
        );

        copy = new Copy("C-001", book);
        member = new Member("M-01", "First member");

        library.registerBook(book);
        library.registerCopy(copy);
        library.registerMember(member);
    }

    @Test
    void lendingMakesCopyUnavailable() {
        Loan loan = library.lend("M-01", "C-001", DAY);

        assertFalse(copy.isAvailable());
        assertTrue(loan.isOpen());
        assertEquals(DAY.plusDays(14), loan.getDueDate());
    }

    @Test
    void returningMakesCopyAvailable() {
        library.lend("M-01", "C-001", DAY);

        Loan loan = library.returnCopy(
                "C-001",
                DAY.plusDays(5)
        );

        assertTrue(copy.isAvailable());
        assertFalse(loan.isOpen());
        assertEquals(DAY.plusDays(5), loan.getReturnDate());
    }

    @Test
    void cannotLendUnavailableCopy() {
        library.lend("M-01", "C-001", DAY);

        assertThrows(
                IllegalStateException.class,
                () -> library.lend("M-01", "C-001", DAY)
        );
    }

    @Test
    void unknownMemberCausesException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> library.lend("UNKNOWN", "C-001", DAY)
        );
    }

    @Test
    void unknownCopyCausesException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> library.lend("M-01", "UNKNOWN", DAY)
        );
    }

    @Test
    void loanIsLateAfterDueDate() {
        Loan loan = library.lend("M-01", "C-001", DAY);

        assertFalse(loan.isLate(DAY.plusDays(14)));
        assertTrue(loan.isLate(DAY.plusDays(15)));
    }

    @Test
    void memberCannotHaveMoreThanFiveOpenLoans() {
        for (int i = 1; i <= 5; i++) {
            Book anotherBook = new Book(
                    "ISBN-" + i,
                    "Book " + i,
                    2020
            );

            Copy anotherCopy = new Copy("C-00" + (i + 1), anotherBook);

            library.registerBook(anotherBook);
            library.registerCopy(anotherCopy);

            library.lend("M-01", anotherCopy.getInventoryNumber(), DAY);
        }

        Book sixthBook = new Book("ISBN-6", "Book 6", 2020);
        Copy sixthCopy = new Copy("C-007", sixthBook);

        library.registerBook(sixthBook);
        library.registerCopy(sixthCopy);

        assertThrows(
                IllegalStateException.class,
                () -> library.lend("M-01", "C-007", DAY)
        );
    }
}