package com.library.libraryexercise.service;

import com.library.libraryexercise.controller.dto.Book;
import com.library.libraryexercise.controller.dto.BookRequest;
import com.library.libraryexercise.controller.dto.CreateBookRequest;
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

    public Book getBook(BookRequest request) {
        Optional<Book> matchingBook = books.stream()
              .filter(book -> book.getTitle().equals(request.getTitle()) && book.getAuthor().equals(request.getAuthor()))
              .findFirst();

        return matchingBook.orElseThrow(() -> new BookNotFoundException("Książka o tytule: " + request.getTitle() + " i autorze: " + request.getAuthor() + " nie znajduje się w bazie"));
    }

    public Optional<Book> addBook(CreateBookRequest request) {
        boolean exists = books.stream()
                .anyMatch(book -> book.getTitle().equals(request.getTitle()));
        if (exists) {
            return Optional.empty();
        }

        Book newBook = new Book(request.getTitle(), request.getAuthor(), request.getGenre(), request.getPageCount());
        books.add(newBook);

        return Optional.of(newBook);
    }

    public void delBook(String title, String author) {
        Optional<Book> bookToRemove = books.stream()
              .filter(book -> book.getTitle().equals(title))
              .filter(book -> book.getAuthor().equals(author))
              .findFirst();

        if (bookToRemove.isPresent()) {
            books.remove(bookToRemove.get());
        } else {
            throw new BookNotFoundException("Książka o tytule: " + title + " i autorze: " + author + " nie znajduje się w bazie");
        }
    }
}
