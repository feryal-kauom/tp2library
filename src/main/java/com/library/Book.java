package com.library;


import java.util.Objects;

public class Book {
    private final String ISBN;
    private final String title;
    private final int year;

    public Book(String ISBN, String title, int year) {
        this.ISBN = Objects.requireNonNull(ISBN);
        this.title = Objects.requireNonNull(title);
        this.year = year;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Book)) return false;

        Book other = (Book) obj;
        return ISBN.equals(other.ISBN);
    }

    @Override
    public int hashCode() {
        return ISBN.hashCode();
    }
}