package com.library;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Library {
    private final Map<String, Book> books = new HashMap<>();
    private final Map<String, Copy> copies = new HashMap<>();
    private final Map<String, Member> members = new HashMap<>();
    private final List<Loan> loans = new ArrayList<>();

    public void registerBook(Book book) {
        books.put(book.getISBN(), book);
    }

    public void registerCopy(Copy copy) {
        copies.put(copy.getInventoryNumber(), copy);
    }

    public void registerMember(Member member) {
        members.put(member.getNumber(), member);
    }

    public Loan lend(String memberNumber, String inventoryNumber,
                     LocalDate today) {
        Member member = members.get(memberNumber);
        Copy copy = copies.get(inventoryNumber);

        if (member == null) {
            throw new IllegalArgumentException("Unknown member.");
        }

        if (copy == null) {
            throw new IllegalArgumentException("Unknown copy.");
        }

        if (!copy.isAvailable()) {
            throw new IllegalStateException("Copy is not available.");
        }

        long activeLoans = loans.stream()
                .filter(loan -> loan.getMember().getNumber().equals(memberNumber))
                .filter(Loan::isOpen)
                .count();

        if (activeLoans >= 5) {
            throw new IllegalStateException(
                    "Member cannot have more than 5 open loans.");
        }

        Loan loan = new Loan(member, copy, today);
        loans.add(loan);
        copy.setAvailable(false);

        return loan;
    }

    public Loan returnCopy(String inventoryNumber, LocalDate today) {
        Copy copy = copies.get(inventoryNumber);

        if (copy == null) {
            throw new IllegalArgumentException("Unknown copy.");
        }

        Loan activeLoan = loans.stream()
                .filter(loan -> loan.getCopy().getInventoryNumber()
                        .equals(inventoryNumber))
                .filter(Loan::isOpen)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException("Copy has no open loan."));

        activeLoan.close(today);
        copy.setAvailable(true);

        return activeLoan;
    }

    public List<Book> searchByTitle(String keyword) {
        String search = keyword.toLowerCase();

        return books.values().stream()
                .filter(book -> book.getTitle()
                        .toLowerCase().contains(search))
                .toList();
    }

    public List<Loan> getLoans() {
        return new ArrayList<>(loans);
    }
}