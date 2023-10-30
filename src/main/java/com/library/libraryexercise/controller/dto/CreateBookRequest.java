package com.library.libraryexercise.controller.dto;

import java.util.Objects;

public class CreateBookRequest {
    private String title;
    private String author;
    private String genre;
    private int pageCount;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public CreateBookRequest(String title, String author, String genre, int pageCount) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.pageCount = pageCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreateBookRequest that = (CreateBookRequest) o;
        return pageCount == that.pageCount && Objects.equals(title, that.title) && Objects.equals(author, that.author) &&
              Objects.equals(genre, that.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, genre, pageCount);
    }
}
