package com.library.libraryexercise.service;

import com.library.libraryexercise.controller.dto.Book;
import com.library.libraryexercise.controller.dto.BookRequest;
import com.library.libraryexercise.controller.dto.CreateBookRequest;
import com.library.libraryexercise.controller.exceptions.BookNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LibraryServiceTest {

   private LibraryService libraryService;

   @BeforeEach
   void setUp() {
      libraryService = new LibraryService();
   }

   @Test
   void testGetBookWhenBookExists() {
      BookRequest request = new BookRequest();
      request.setTitle("Harry Potter i więzień Azkabanu");
      request.setAuthor("J.K. Rowling");

      Book existingBook = libraryService.getBook(request);

      assertEquals("Harry Potter i więzień Azkabanu", existingBook.getTitle());
      assertEquals("J.K. Rowling", existingBook.getAuthor());
   }

   @Test
   void testGetBookWhenBookDoesNotExist() {
      BookRequest request = new BookRequest();
      request.setTitle("Nieistniejąca Książka");
      request.setAuthor("Autor Testowy");

      assertThrows(BookNotFoundException.class, () -> libraryService.getBook(request));
   }

   @Test
   void testAddBook() {
      CreateBookRequest request = new CreateBookRequest("Nowa Książka", "Autor Testowy", "Gatunek", 200);

      Optional<Book> newBook = libraryService.addBook(request);

      assertTrue(newBook.isPresent());
      assertEquals("Nowa Książka", newBook.get().getTitle());
      assertEquals("Autor Testowy", newBook.get().getAuthor());
      assertEquals("Gatunek", newBook.get().getGenre());
      assertEquals(200, newBook.get().getPageCount());
   }

   @Test
   void testAddExistingBook() {
      CreateBookRequest request = new CreateBookRequest("Harry Potter i więzień Azkabanu", "J.K. Rowling", "Fantasy", 300);

      Optional<Book> newBook = libraryService.addBook(request);

      assertFalse(newBook.isPresent());
   }

   @Test
   void testDeleteExistingBook() {
      String title = "Harry Potter i więzień Azkabanu";
      String author = "J.K. Rowling";

      libraryService.delBook(title, author);

      assertThrows(BookNotFoundException.class, () -> {
         BookRequest request = new BookRequest();
         request.setTitle(title);
         request.setAuthor(author);
         libraryService.getBook(request);
      });
   }

   @Test
   void testDeleteNonExistingBook() {
      String title = "Nieistniejąca Książka";
      String author = "Autor Testowy";

      assertThrows(BookNotFoundException.class, () -> {
         libraryService.delBook(title, author);
      });
   }
}