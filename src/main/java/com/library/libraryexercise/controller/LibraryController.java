package com.library.libraryexercise.controller;


import com.library.libraryexercise.controller.dto.Book;
import com.library.libraryexercise.controller.dto.BookRequest;
import com.library.libraryexercise.controller.dto.CreateBookRequest;
import com.library.libraryexercise.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/books")
    public Book getBook(@RequestParam String title, @RequestParam String author) {
        BookRequest request = new BookRequest();
        request.setTitle(title);
        request.setAuthor(author);
        return libraryService.getBook(request);
    }

    @PostMapping("/books")
    public ResponseEntity<Book> addBook(@RequestBody CreateBookRequest request) {
        Optional<Book> book = libraryService.addBook(request);
        return book.map(newBook -> ResponseEntity.status(HttpStatus.OK).body(newBook))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/books/{title}")
    public ResponseEntity<String> delBook(@PathVariable String title, @RequestParam String author) {
        libraryService.delBook(title, author);
        return ResponseEntity.ok("Książka o tytule: " + title + " autorstwa " + author + " została usunięta");
    }
}