package com.library.libraryexercise.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.libraryexercise.controller.dto.Book;
import com.library.libraryexercise.controller.exceptions.BookNotFoundException;
import com.library.libraryexercise.service.LibraryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class LibraryControllerIntegrationTest {

    @Autowired
    private LibraryController libraryController;

    @MockBean
    private LibraryService libraryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddBookSuccess() throws Exception {
        Book newBook = new Book("Nowa Książka", "Autor Testowy", "Gatunek", 200);
        ResponseEntity<Book> responseEntity = ResponseEntity.ok(newBook);
        when(libraryService.addBook("Nowa Książka", "Autor Testowy", "Gatunek", 200)).thenReturn(responseEntity);

        ResponseEntity<Book> response = libraryController.addBook("Nowa Książka", "Autor Testowy", "Gatunek", 200);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualTo("Nowa Książka");
        assertThat(response.getBody().getAuthor()).isEqualTo("Autor Testowy");
        assertThat(response.getBody().getGenre()).isEqualTo("Gatunek");
        assertThat(response.getBody().getPageCount()).isEqualTo(200);
    }

    @Test
    public void testAddBookDuplicateTitle() throws Exception {
        Book existingBook = new Book("Harry Potter i więzień Azkabanu", "J.K. Rowling", "Fantasy", 300);
        ResponseEntity<Book> responseEntity = ResponseEntity.badRequest().body(null);
        when(libraryService.addBook("Harry Potter i więzień Azkabanu", "J.K. Rowling", "Fantasy", 300))
                .thenReturn(responseEntity);

        ResponseEntity<Book> response = libraryController.addBook(
                "Harry Potter i więzień Azkabanu",
                "J.K. Rowling",
                "Fantasy",
                300
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testGetBookWhenBookExists() throws Exception {
        Book existingBook = new Book("Harry Potter i więzień Azkabanu", "J.K. Rowling", "Fantasy", 300);
        when(libraryService.getBook("Harry Potter i więzień Azkabanu", "J.K. Rowling")).thenReturn(existingBook);

        Book response = libraryController.getBook("Harry Potter i więzień Azkabanu", "J.K. Rowling");

        assertThat(response.getTitle()).isEqualTo("Harry Potter i więzień Azkabanu");
        assertThat(response.getAuthor()).isEqualTo("J.K. Rowling");
        assertThat(response.getGenre()).isEqualTo("Fantasy");
        assertThat(response.getPageCount()).isEqualTo(300);
    }

    @Test
    public void testGetBookWhenBookDoesNotExist() {
        String nonExistentTitle = "Nonexistent Book";
        String nonExistentAuthor = "Unknown Author";

        when(libraryService.getBook(nonExistentTitle, nonExistentAuthor))
                .thenThrow(new BookNotFoundException("Książka o tytule: " + nonExistentTitle + " autorstwa " + nonExistentAuthor + " nie znajduje się w bazie"));

        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> libraryController.getBook(nonExistentTitle, nonExistentAuthor));

        assertThat(exception.getMessage())
                .isEqualTo("Książka o tytule: " + nonExistentTitle + " autorstwa " + nonExistentAuthor + " nie znajduje się w bazie");
    }
}