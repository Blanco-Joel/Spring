package com.example.library.integration.controller;

import com.example.library.application.model.book.BookRequest;
import com.example.library.infrastructure.persistence.entities.BookEntity;
import com.example.library.infrastructure.persistence.entities.CategoryEntity;
import com.example.library.infrastructure.persistence.repository.BookRepository;
import com.example.library.infrastructure.persistence.repository.CategoryRepository;
import com.example.library.testsupport.SqliteMvcIntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@SqliteMvcIntegrationTest
class BookControllerIT {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final BookRepository bookRepository;

    private final CategoryRepository categoryRepository;

    @Test
    void createBookCreatesAndReturnsBook() throws Exception {
        CategoryEntity category = categoryRepository.save(new CategoryEntity(null, "Terror", null));
        BookRequest request = BookRequest.builder()
                .isbn("ISBN-001")
                .title("El principito")
                .publishedYear("2024")
                .author("Paco")
                .categoriesIds(List.of(category.getId()))
                .build();

        mockMvc.perform(post("/books/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("ISBN-001"))
                .andExpect(jsonPath("$.title").value("El principito"))
                .andExpect(jsonPath("$.author").value("Paco"))
                .andExpect(jsonPath("$.bookCategories", hasSize(1)))
                .andExpect(jsonPath("$.bookCategories[0].name").value("Terror"))
                .andExpect(jsonPath("$.loans").doesNotExist());
    }

    @Test
    void getBooksReturnsPagedBooks() throws Exception {
        CategoryEntity category = categoryRepository.save(new CategoryEntity(null, "Fantasia", null));
        bookRepository.save(BookEntity.builder()
                .isbn("ISBN-002")
                .title("Clean Code")
                .publishedYear("2008")
                .author("Robert C. Martin")
                .bookCategories(List.of(category))
                .build());

        mockMvc.perform(get("/books/all")
                        .param("page", "0")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title").value("Clean Code"))
                .andExpect(jsonPath("$.page.number").value(0))
                .andExpect(jsonPath("$.page.size").value(10))
                .andExpect(jsonPath("$.page.totalElements").value(1));
    }

    @Test
    void getBookByIdReturnsPersistedBook() throws Exception {
        CategoryEntity category = categoryRepository.save(new CategoryEntity(null, "Drama", null));
        BookEntity savedBook = bookRepository.save(BookEntity.builder()
                .isbn("ISBN-003")
                .title("Patterns")
                .publishedYear("1994")
                .author("GoF")
                .bookCategories(List.of(category))
                .build());

        mockMvc.perform(get("/books/{id}", savedBook.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("ISBN-003"))
                .andExpect(jsonPath("$.title").value("Patterns"))
                .andExpect(jsonPath("$.bookCategories[0].name").value("Drama"));
    }

    @Test
    void updateBookUpdatesPersistedData() throws Exception {
        CategoryEntity category = categoryRepository.save(new CategoryEntity(null, "Tecnico", null));
        BookEntity savedBook = bookRepository.save(BookEntity.builder()
                .isbn("ISBN-004")
                .title("Original")
                .publishedYear("2000")
                .author("Autor Original")
                .bookCategories(List.of(category))
                .build());
        BookRequest request = BookRequest.builder()
                .title("Actualizado")
                .author("Autor Actualizado")
                .build();

        mockMvc.perform(put("/books/{id}", savedBook.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Actualizado"))
                .andExpect(jsonPath("$.author").value("Autor Actualizado"));
    }

    @Test
    void deleteBookRemovesBookFromDatabase() throws Exception {
        CategoryEntity category = categoryRepository.save(new CategoryEntity(null, "Clasico", null));
        BookEntity savedBook = bookRepository.save(BookEntity.builder()
                .isbn("ISBN-005")
                .title("Borrable")
                .publishedYear("2001")
                .author("Autor")
                .bookCategories(List.of(category))
                .build());

        mockMvc.perform(delete("/books/{id}", savedBook.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/books/all")
                        .param("page", "0")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @Test
    void getBooksByFilterReturnsMatchingBooks() throws Exception {
        CategoryEntity category = categoryRepository.save(new CategoryEntity(null, "Fantasia", null));
        bookRepository.save(BookEntity.builder()
                .isbn("ISBN-006")
                .title("Filtrado")
                .publishedYear("2024")
                .author("Autor Filtro")
                .bookCategories(List.of(category))
                .build());

        mockMvc.perform(get("/books/byFilter")
                        .param("author", "Autor Filtro")
                        .param("title", "Filtrado")
                        .param("year", "2024"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Filtrado"))
                .andExpect(jsonPath("$[0].author").value("Autor Filtro"));
    }
}
