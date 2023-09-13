package com.library.libraryexercise.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.libraryexercise.controller.LibraryController;
import com.library.libraryexercise.dto.Book;
import com.library.libraryexercise.exceptions.BookNotFoundException;
import com.library.libraryexercise.service.LibraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class LibraryControllerTest {

	private MockMvc mockMvc;

	@Mock
	private LibraryService libraryService;

	private ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(new LibraryController(libraryService)).build();
	}

	@Test
	public void testGetBookWhenBookExists() throws Exception {
		Book book = new Book("Harry Potter i więzień Azkabanu", "J.K. Rowling", "Fantasy", 300);

		when(libraryService.getBook("Harry Potter", "J.K. Rowling")).thenReturn(book);

		mockMvc.perform(get("/library/books/{title}", "Harry Potter")
						.param("author", "J.K. Rowling")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Harry Potter i więzień Azkabanu"))
				.andExpect(jsonPath("$.author").value("J.K. Rowling"))
				.andExpect(jsonPath("$.genre").value("Fantasy"))
				.andExpect(jsonPath("$.pageCount").value(300));

		verify(libraryService, times(1)).getBook("Harry Potter", "J.K. Rowling");
	}

	@Test
	public void testGetBookWhenBookDoesNotExist() throws Exception {
		when(libraryService.getBook("Nonexistent Book", "Unknown Author")).thenThrow(new BookNotFoundException("Książka nie istnieje"));

		try {
			mockMvc.perform(MockMvcRequestBuilders.get("/library/books/{title}", "Nonexistent Book")
					.param("author", "Unknown Author")
					.contentType(MediaType.APPLICATION_JSON));
		} catch (Exception e) {
			if (e.getCause() instanceof BookNotFoundException) {
				return; // Wyjątek BookNotFoundException został rzucony, test jest zaliczony.
			}
		}

		// Jeśli nie rzucono wyjątku BookNotFoundException, to oznacza, że test nie zaliczył się.
		fail("Oczekiwano wyjątku BookNotFoundException");
	}
	}