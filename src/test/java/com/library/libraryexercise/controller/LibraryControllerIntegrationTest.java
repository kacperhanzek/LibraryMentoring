package com.library.libraryexercise.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.libraryexercise.controller.dto.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class LibraryControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	public void setUp() {

	}

	@Test
	public void testAddBookSuccess() throws Exception {
		Book newBook = new Book("Nowa Książka", "Autor Testowy", "Gatunek", 200);

		String jsonRequest = objectMapper.writeValueAsString(newBook);

		mockMvc.perform(MockMvcRequestBuilders.put("/library/books/{title}", "Nowa Książka")
						.param("author", "Autor Testowy")
						.param("genre", "Gatunek")
						.param("pageCount", "200")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonRequest))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Nowa Książka"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.author").value("Autor Testowy"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.genre").value("Gatunek"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.pageCount").value(200));
	}

	@Test
	public void testAddBookDuplicateTitle() throws Exception {
		Book existingBook = new Book("Harry Potter i więzień Azkabanu", "J.K. Rowling", "Fantasy", 300);

		String jsonRequest = objectMapper.writeValueAsString(existingBook);

		mockMvc.perform(MockMvcRequestBuilders.put("/library/books/{title}", "Harry Potter i więzień Azkabanu")
						.param("author", "J.K. Rowling")
						.param("genre", "Fantasy")
						.param("pageCount", "300")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonRequest))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void testGetBookWhenBookExists() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/library/books/{title}", "Harry Potter i więzień Azkabanu")
						.param("author", "J.K. Rowling")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Harry Potter i więzień Azkabanu"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.author").value("J.K. Rowling"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.genre").value("Fantasy"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.pageCount").value(300));
	}

	@Test
	public void testGetBookWhenBookDoesNotExist() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/library/books/{title}", "Nonexistent Book")
						.param("author", "Unknown Author")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
}