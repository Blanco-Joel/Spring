package com.example.library.application.controller;

import com.example.library.application.model.PagedResponse;
import com.example.library.application.model.book.BookRequest;
import com.example.library.application.model.book.BookResponse;
import com.example.library.application.utils.mappers.BookResponseMappers;
import com.example.library.domain.model.Book;
import com.example.library.domain.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @Mock
    private BookResponseMappers bookResponseMappers;

    @InjectMocks
    private BookController bookController;

    @Test
    void getBooksReturnsPagedResponse() {
        Book book = Book.builder().title("Clean Code").build();
        BookResponse response = BookResponse.builder().title("Clean Code").build();
        Page<Book> page = new PageImpl<>(List.of(book), PageRequest.of(0, 10), 1);

        given(bookService.findAll(0, 10)).willReturn(page);
        given(bookResponseMappers.toResponse(book)).willReturn(response);

        ResponseEntity<PagedResponse<BookResponse>> result = bookController.getBooks(0, 10);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getContent()).containsExactly(response);
        assertThat(result.getBody().getPage().getNumber()).isEqualTo(0);
        assertThat(result.getBody().getPage().getSize()).isEqualTo(10);
    }

    @Test
    void createBookReturnsMappedResponseWithoutLoans() {
        BookRequest request = BookRequest.builder().title("Refactoring").build();
        Book book = Book.builder().title("Refactoring").build();
        BookResponse response = BookResponse.builder().title("Refactoring").build();

        given(bookService.createBook(request)).willReturn(book);
        given(bookResponseMappers.toResponseMapperWithoutLoan(book)).willReturn(response);

        ResponseEntity<BookResponse> result = bookController.createBook(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    void getBookByIdReturnsMappedResponse() {
        Book book = Book.builder().title("Patterns").build();
        BookResponse response = BookResponse.builder().title("Patterns").build();

        given(bookService.findById(3L)).willReturn(book);
        given(bookResponseMappers.toResponse(book)).willReturn(response);

        ResponseEntity<BookResponse> result = bookController.getBookById(3L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    void updateBookReturnsMappedResponse() {
        BookRequest request = BookRequest.builder().title("Updated").build();
        Book book = Book.builder().title("Updated").build();
        BookResponse response = BookResponse.builder().title("Updated").build();

        given(bookService.updateBook(4L, request)).willReturn(book);
        given(bookResponseMappers.toResponse(book)).willReturn(response);

        ResponseEntity<BookResponse> result = bookController.updateBook(4L, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    void deleteBookByIdReturnsOkAndDelegates() {
        ResponseEntity<Void> result = bookController.deleteBookById(5L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(bookService).deleteById(5L);
    }

    @Test
    void getBooksByFilterReturnsMappedResponseList() {
        Book book = Book.builder().title("TDD").build();
        BookResponse response = BookResponse.builder().title("TDD").build();

        given(bookService.findByFilter("Kent Beck", "TDD", "2002")).willReturn(List.of(book));
        given(bookResponseMappers.toResponseList(List.of(book))).willReturn(List.of(response));

        ResponseEntity<List<BookResponse>> result = bookController.getBooksByFilter("Kent Beck", "TDD", "2002");

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).containsExactly(response);
    }
}
