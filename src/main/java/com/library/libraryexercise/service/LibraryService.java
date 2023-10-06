package com.library.libraryexercise.service;

import com.library.libraryexercise.controller.dto.Book;
import com.library.libraryexercise.controller.exceptions.BookNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {

    private final List<Book> books = new ArrayList<>();

    public LibraryService() {
        books.add(new Book("Harry Potter i więzień Azkabanu", "J.K. Rowling", "Fantasy", 300));
    }

    public Book getBook(String title, String author) {
        return books.stream()
                .filter(book -> book.getTitle().equals(title))
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException("Książka o tym tytule: " + title + " nie znajduje się w bazie"));
    }

    public Optional<Book> addBook(String title, String author, String genre, int pageCount) {
        boolean exists = books.stream()
                .anyMatch(book -> book.getTitle().equals(title));

        if (exists) {
            return Optional.empty();
        }

        Book newBook = new Book(title, author, genre, pageCount);
        books.add(newBook);

        return Optional.of(newBook);
    }

    public void delBook(String title, String author) {
        Optional<Book> bookToRemove = books.stream()
                .filter(book -> book.getTitle().equals(title))
                .filter(book -> book.getAuthor().equals(author))
                .findFirst();

        bookToRemove.ifPresent(books::remove);
    }
}
