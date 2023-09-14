package com.library.libraryexercise.controller;


import com.library.libraryexercise.controller.dto.Book;
import com.library.libraryexercise.controller.exceptions.BookNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/library")
public class LibraryController {

    private final Map<String, Book> books = new HashMap<>() {{
        put("Harry Potter i więzień Azkabanu", new Book("Harry Potter i więzień Azkabanu", "J.K. Rowling", "Fantasy", 300));
    }};;

    @GetMapping("/books/{title}")
    public Book getBook(@PathVariable String title, @RequestParam String author) {
        if (!books.containsKey(title)) {
            throw new BookNotFoundException("Książka o tym tytule: " + title + " nie znajduje się w bazie");
        }
        return books.get(title);
    }

    @PutMapping("/books/{title}")
    public ResponseEntity<Book> addBook(@PathVariable String title,
                                        @RequestParam String author,
                                        @RequestParam String genre,
                                        @RequestParam int pageCount) {
        if (books.containsKey(title)) {
            return ResponseEntity.badRequest().body(null);
        }

        Book newBook = new Book(title, author, genre, pageCount);

        books.put(title, newBook);

        return ResponseEntity.ok(newBook);
    }

    @DeleteMapping("books/{title}")
    @ResponseStatus(CREATED)
    public String delBook(@PathVariable String title, @RequestParam String author) {
        books.remove(title);
        return "Książka o tym tytule: " + title + " została usunięta";
    }
}