package com.library;

import java.time.LocalDate;
import java.util.Objects;

public class Loan {
    private final Member member;
    private final Copy copy;
    private final LocalDate startDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;

    public Loan(Member member, Copy copy, LocalDate startDate) {
        this.member = Objects.requireNonNull(member);
        this.copy = Objects.requireNonNull(copy);
        this.startDate = Objects.requireNonNull(startDate);
        this.dueDate = startDate.plusDays(14);
        this.returnDate = null;
    }

    public Member getMember() {
        return member;
    }

    public Copy getCopy() {
        return copy;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public boolean isOpen() {
        return returnDate == null;
    }

    public boolean isLate(LocalDate today) {
        return isOpen() && today.isAfter(dueDate);
    }

    public void close(LocalDate returnDate) {
        if (!isOpen()) {
            throw new IllegalStateException("Loan is already closed.");
        }

        this.returnDate = Objects.requireNonNull(returnDate);
    }
}