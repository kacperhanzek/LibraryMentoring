package com.library.LibraryExercise;

public class Book {
    private String title;
    private String author;
    private String genre;
    private int pageCount;

    public Book() {
    }

    public Book(String title, String author, String genre, int pageCount) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.pageCount = pageCount;
    }

    public String getTitle() {
        return title;
    }

    public String setTitle(String title) {
        this.title = title;
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String setAuthor(String author) {
        this.author = author;
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String setGenre(String genre) {
        this.genre = genre;
        return genre;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int setPageCount(int pageCount) {
        this.pageCount = pageCount;
        return pageCount;
    }

}
