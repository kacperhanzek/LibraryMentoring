package com.library.libraryexercise.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.libraryexercise.controller.dto.Book;
import com.library.libraryexercise.controller.exceptions.BookNotFoundException;
import com.library.libraryexercise.service.LibraryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class LibraryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibraryService libraryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddBookSuccess() throws Exception {
        Book newBook = new Book("Nowa Książka", "Autor Testowy", "Gatunek", 200);
        Optional<Book> optionalBook = Optional.of(newBook);

        when(libraryService.addBook("Nowa Książka", "Autor Testowy", "Gatunek", 200))
                .thenReturn(optionalBook);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Nowa Książka"))
                .andExpect(jsonPath("$.author").value("Autor Testowy"))
                .andExpect(jsonPath("$.genre").value("Gatunek"))
                .andExpect(jsonPath("$.pageCount").value(200));
    }

    @Test
    public void testAddBookDuplicateTitle() throws Exception {
        Book existingBook = new Book("Harry Potter i więzień Azkabanu", "J.K. Rowling", "Fantasy", 300);
        Optional<Book> optionalBook = Optional.empty();
        when(libraryService.addBook("Harry Potter i więzień Azkabanu", "J.K. Rowling", "Fantasy", 300))
                .thenReturn(optionalBook);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingBook)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetBookWhenBookExists() throws Exception {
        Book existingBook = new Book("Harry Potter i więzień Azkabanu", "J.K. Rowling", "Fantasy", 300);
        when(libraryService.getBook("Harry Potter i więzień Azkabanu", "J.K. Rowling")).thenReturn(existingBook);

        mockMvc.perform(get("/books/Harry Potter i więzień Azkabanu/J.K. Rowling"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Harry Potter i więzień Azkabanu"))
                .andExpect(jsonPath("$.author").value("J.K. Rowling"))
                .andExpect(jsonPath("$.genre").value("Fantasy"))
                .andExpect(jsonPath("$.pageCount").value(300));
    }

    @Test
    public void testGetBookWhenBookDoesNotExist() throws Exception {
        String nonExistentTitle = "Nonexistent Book";
        String nonExistentAuthor = "Unknown Author";

        when(libraryService.getBook(nonExistentTitle, nonExistentAuthor))
                .thenThrow(new BookNotFoundException("Książka o tytule: " + nonExistentTitle + " autorstwa " + nonExistentAuthor + " nie znajduje się w bazie"));

        mockMvc.perform(get("/books/" + nonExistentTitle + "/" + nonExistentAuthor))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Książka o tytule: " + nonExistentTitle + " autorstwa " + nonExistentAuthor + " nie znajduje się w bazie"));
    }
}