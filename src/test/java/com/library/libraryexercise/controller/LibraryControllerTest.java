package com.library.libraryexercise.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.libraryexercise.controller.dto.Book;
import com.library.libraryexercise.controller.dto.BookRequest;
import com.library.libraryexercise.controller.dto.CreateBookRequest;
import com.library.libraryexercise.controller.exceptions.BookNotFoundException;
import com.library.libraryexercise.service.LibraryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LibraryController.class)
class LibraryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibraryService libraryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddBookSuccess() throws Exception {
        CreateBookRequest request = new CreateBookRequest("Nowa Książka", "Autor Testowy", "Gatunek", 200);
        Book newBook = new Book("Nowa Książka", "Autor Testowy", "Gatunek", 200);
        Optional<Book> optionalBook = Optional.of(newBook);

        when(libraryService.addBook(request)).thenReturn(optionalBook);

        mockMvc.perform(post("/library/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
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

        when(libraryService.addBook(request))
              .thenReturn(Optional.of(existingBook))
              .thenReturn(Optional.empty());

        mockMvc.perform(post("/library/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
              .andExpect(status().isOk());

        mockMvc.perform(post("/library/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetBookWhenBookExists() throws Exception {
        Book existingBook = new Book("Harry Potter i więzień Azkabanu", "J.K. Rowling");
        BookRequest request = new BookRequest();
        request.setTitle("Harry Potter i więzień Azkabanu");
        request.setAuthor("J.K. Rowling");

        when(libraryService.getBook(request)).thenReturn(existingBook);

        mockMvc.perform(get("/library/books")
                    .param("title", request.getTitle())
                    .param("author", request.getAuthor()))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.title").value("Harry Potter i więzień Azkabanu"))
              .andExpect(jsonPath("$.author").value("J.K. Rowling"));
    }

    @Test
    public void testGetBookWhenBookDoesNotExist() throws Exception {
        BookRequest request = new BookRequest();
        request.setTitle("Nieistniejąca Książka");
        request.setAuthor("Autor Testowy");

        when(libraryService.getBook(request)).thenThrow(new BookNotFoundException("asdf"));


        mockMvc.perform(get("/library/books")
                    .param("title", request.getTitle())
                    .param("author", request.getAuthor()))
              .andExpect(status().isNotFound());
    }
}