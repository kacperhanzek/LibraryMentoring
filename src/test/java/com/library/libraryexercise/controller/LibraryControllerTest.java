package com.library.libraryexercise.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest
@AutoConfigureMockMvc
class LibraryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testGetBookWhenBookExists() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/library/books/{title}", "Harry Potter")
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
