package com.library.libraryexercise.service.impl;

import com.library.libraryexercise.dto.Book;
import com.library.libraryexercise.exceptions.BookNotFoundException;
import com.library.libraryexercise.service.LibraryService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class LibraryServiceImpl implements LibraryService {

    private final Map<String, Book> books = new HashMap<>() {{
        put("Harry Potter", new Book("Harry Potter i więzień Azkabanu", "J.K. Rowling", "Fantasy", 300));
    }};

    @Override
    public Book getBook(String title, String author) {
        if (!books.containsKey(title)) {
            throw new BookNotFoundException("Książka o tym tytule: " + title + " nie znajduje się w bazie");
        }
        return books.get(title);
    }

    @Override
    public Book addBook(String title, String author, String genre, int pageCount) {
        if (books.containsKey(title)) {
            throw new IllegalArgumentException("Książka o tytule" + title + " jest już w bazie");
        }

        Book newBook = new Book(title, author, genre, pageCount);

        books.put(title, newBook);

        return newBook;
    }

    @Override
    public void delBook(String title, String author) {
        books.remove(title);
    }


}
