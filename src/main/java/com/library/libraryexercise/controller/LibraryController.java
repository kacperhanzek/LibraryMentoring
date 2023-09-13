package com.library.libraryexercise.controller;


import com.library.libraryexercise.dto.Book;
import com.library.libraryexercise.exceptions.BookNotFoundException;
import com.library.libraryexercise.service.LibraryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/library")
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/books/{title}")
    public Book getBook(@PathVariable String title, @RequestParam String author) {
       try {
           return libraryService.getBook(title, author);
       } catch (BookNotFoundException ex) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Książka nie istnieje", ex);
       }
    }

    @PutMapping("/books/{title}")
    public Book addBook(@PathVariable String title,
                        @RequestParam String author,
                        @RequestParam String genre,
                        @RequestParam int pageCount) {
        return libraryService.addBook(title, author, genre, pageCount);
    }

    @DeleteMapping("books/{title}")
    @ResponseStatus(CREATED)
    public String delBook(@PathVariable String title, @RequestParam String author) {
        libraryService.delBook(title, author);
        return "Książka o tym tytule: " + title + " została usunięta";
    }
}