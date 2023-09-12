package com.library.libraryexercise.service;

import com.library.libraryexercise.dto.Book;

public interface LibraryService {

    Book getBook(String title, String author);
    Book addBook(String title, String author, String genre, int pageCount);
    void delBook(String title, String author);
}
