package com.library.libraryexercise.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.libraryexercise.controller.dto.Book;
import com.library.libraryexercise.controller.exceptions.BookNotFoundException;
import com.library.libraryexercise.service.LibraryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LibraryControllerIntegrationTest {

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
                        .contentType("application/json")
                        .content("{\"title\":\"Harry Potter i więzień Azkabanu\",\"author\":\"J.K. Rowling\",\"genre\":\"Fantasy\",\"pageCount\":300}"))
                .andExpect(status().isBadRequest());
    }

    public void testGetBookWhenBookExists() throws Exception {
        Book existingBook = new Book("Harry Potter i więzień Azkabanu", "J.K. Rowling", "Fantasy", 300);
        when(libraryService.getBook("Harry Potter i więzień Azkabanu", "J.K. Rowling"))
                .thenReturn(existingBook);

        mockMvc.perform(get("/books/{title}/{author}", "Harry Potter i więzień Azkabanu", "J.K. Rowling")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Harry Potter i więzień Azkabanu"))
                .andExpect(jsonPath("$.author").value("J.K. Rowling"))
                .andExpect(jsonPath("$.genre").value("Fantasy"))
                .andExpect(jsonPath("$.pageCount").value(300));
    }

    public void testGetBookWhenBookDoesNotExist() throws Exception {
        String nonExistentTitle = "Nonexistent Book";
        String nonExistentAuthor = "Unknown Author";

        when(libraryService.getBook(nonExistentTitle, nonExistentAuthor))
                .thenThrow(new BookNotFoundException("Książka o tytule: " + nonExistentTitle + " autorstwa " + nonExistentAuthor + " nie znajduje się w bazie"));

        mockMvc.perform(get("/books/{title}/{author}", nonExistentTitle, nonExistentAuthor))
                .andExpect(status().isNotFound()) // Oczekiwany status HTTP 404 Not Found
                .andExpect(MockMvcResultMatchers.content().string("Książka o tytule: " + nonExistentTitle + " autorstwa " + nonExistentAuthor + " nie znajduje się w bazie")); // Oczekiwany komunikat jako treść odpowiedzi
    }
}