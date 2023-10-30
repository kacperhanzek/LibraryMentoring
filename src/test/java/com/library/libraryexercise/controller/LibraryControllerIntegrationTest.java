package com.library.libraryexercise.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.libraryexercise.controller.dto.Book;
import com.library.libraryexercise.controller.dto.BookRequest;
import com.library.libraryexercise.controller.dto.CreateBookRequest;
import com.library.libraryexercise.controller.exceptions.BookNotFoundException;
import com.library.libraryexercise.service.LibraryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext
@AutoConfigureMockMvc
class LibraryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddBookSuccess() throws Exception {
        Book newBook = new Book("Nowa Książka", "Autor Testowy", "Gatunek", 200);

        ResultActions resultActions = mockMvc.perform(post("/library/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newBook)));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Nowa Książka"))
                .andExpect(jsonPath("$.author").value("Autor Testowy"))
                .andExpect(jsonPath("$.genre").value("Gatunek"))
                .andExpect(jsonPath("$.pageCount").value(200));
    }

    @Test
    public void testAddBookDuplicateTitle() throws Exception {
        Book existingBook = new Book("Harry Potter i więzień Azkabanu", "J.K. Rowling", "Fantasy", 300);
        CreateBookRequest request = new CreateBookRequest(existingBook.getTitle(), existingBook.getAuthor(), existingBook.getGenre(), existingBook.getPageCount());

        mockMvc.perform(post("/library/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
              .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetBookWhenBookExists() throws Exception {
        Book existingBook = new Book("Harry Potter i więzień Azkabanu", "J.K. Rowling");

        mockMvc.perform(post("/library/books")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(existingBook)));


        mockMvc.perform(get("/library/books")
                    .param("title", existingBook.getTitle())
                    .param("author", existingBook.getAuthor()))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.title").value(existingBook.getTitle()))
              .andExpect(jsonPath("$.author").value(existingBook.getAuthor()));
    }

    @Test
    public void testGetBookWhenBookDoesNotExist() throws Exception {
        BookRequest request = new BookRequest();
        request.setTitle("Nieistniejąca Książka");
        request.setAuthor("Autor Testowy");


        mockMvc.perform(get("/library/books")
                    .param("title", request.getTitle())
                    .param("author", request.getAuthor()))
              .andExpect(status().isNotFound());
    }
}