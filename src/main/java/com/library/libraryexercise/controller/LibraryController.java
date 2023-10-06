package com.library.libraryexercise.controller;


import com.library.libraryexercise.controller.dto.Book;
import com.library.libraryexercise.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/library")
public class LibraryController {

    private final LibraryService libraryService;

    @Autowired
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/books/{title}")
    public Book getBook(@PathVariable String title, @RequestParam String author) {
        return libraryService.getBook(title, author);
    }

    @PutMapping("/books/{title}")
    public Optional<Book> addBook(@PathVariable String title,
                                  @RequestParam String author,
                                  @RequestParam String genre,
                                  @RequestParam int pageCount) {
        return libraryService.addBook(title, author, genre, pageCount);
    }

    @DeleteMapping("/books/{title}")
    public ResponseEntity<String> delBook(@PathVariable String title, @RequestParam String author) {
        libraryService.delBook(title, author);
        return ResponseEntity.ok("Książka o tytule: " + title + " autorstwa " + author + " została usunięta");
    }
}