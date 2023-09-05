package com.library.LibraryExercise;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/library")
public class LibraryController {

    @GetMapping("/books")
    public Book getBook(){
        Book book1 = new Book("Harry Potter i więźien Azkabanu", "J.K.Rowling", "Fantasy", 300);
        return book1;
    }
}