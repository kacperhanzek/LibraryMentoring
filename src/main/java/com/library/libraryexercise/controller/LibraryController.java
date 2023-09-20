package com.library.libraryexercise.controller;


import com.library.libraryexercise.controller.dto.Book;
import com.library.libraryexercise.controller.exceptions.BookNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/library")
public class LibraryController {

    private final List<Book> books = new ArrayList<>();

    public LibraryController() {
        books.add(new Book("Harry Potter i więzień Azkabanu", "J.K. Rowling", "Fantasy", 300));
    }

    @GetMapping("/books/{title}")
    public Book getBook(@PathVariable String title, @RequestParam String author) {
        return books.stream()
                .filter(book -> book.getTitle().equals(title))
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException("Książka o tym tytule: " + title + " nie znajduje się w bazie"));
    }

    @PutMapping("/books/{title}")
    public ResponseEntity<Book> addBook(@PathVariable String title,
                                        @RequestParam String author,
                                        @RequestParam String genre,
                                        @RequestParam int pageCount) {
        boolean exists = books.stream()
                .anyMatch((book -> book.getTitle().equals(title)));

        if (exists) {
            return ResponseEntity.badRequest().body(null);
        }

        Book newBook = new Book(title, author, genre, pageCount);

        books.add(newBook);

        return ResponseEntity.ok(newBook);
    }

    @DeleteMapping("/books/{title}")
    public ResponseEntity<String> delBook(@PathVariable String title, @RequestParam String author) {
        Optional<Book> bookToRemove = books.stream()
                .filter(book -> book.getTitle().equals(title))
                .filter(book -> book.getAuthor().equals(author))
                .findFirst();

        bookToRemove.ifPresent(books::remove);

        return ResponseEntity.ok("Książka o tytule: " + title + " autorstwa " + author + " została usunięta");
    }
}