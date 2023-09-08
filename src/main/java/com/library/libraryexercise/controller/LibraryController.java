package com.library.libraryexercise.controller;


import com.library.libraryexercise.controller.dto.Book;
import com.library.libraryexercise.controller.exceptions.BookNotFoundException;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/library")
public class LibraryController {

    private final Map<String, Book> books = new HashMap<>();

    @GetMapping("/books")
    public Book getBook(@ParameterObject Book Book) {
        return new Book("Harry Potter i więźien Azkabanu", "J.K.Rowling", "Fantasy", 300);
    }

    @PutMapping("/books/{title}")
    public Book addBook(@PathVariable String title,
                        @RequestParam String author,
                        @RequestParam String genre,
                        @RequestParam int pageCount,
                        @ParameterObject Book book) {
        if (books.containsKey(title)) {
            throw new IllegalArgumentException("Książka o tytule" + title + " jest już w bazie");
        }

        Book newBook = new Book(title, author, genre, pageCount);

        books.put(title, newBook);

        return newBook;


    }

    @DeleteMapping("books/{title}")
    public String delBook(@PathVariable String title, @RequestParam String author, @ParameterObject Book book) {
        if (!books.containsKey(title)) {
            throw new BookNotFoundException("Książka o tym tytule: " + title + " nie znajduje się w bazie");
        }
        books.remove(title);
        return "Książka o tym tytule: " + title + " została usunięta";

    }
}