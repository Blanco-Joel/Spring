package com.example.library.application.controller;

import com.example.library.application.model.BookRequest;
import com.example.library.application.model.BookResponse;
import com.example.library.application.model.exceptions.BookNotFound;
import com.example.library.domain.model.Book;
import com.example.library.domain.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @GetMapping("/all")
    public ResponseEntity<List<BookResponse>> getBooks() {
        List<Book> response = bookService.findAll();

        List<BookResponse> mappedList = response.stream().map(bookResponse -> BookResponse.builder()
                        .isbn(bookResponse.getIsbn())
                        .title(bookResponse.getTitle())
                        .author(bookResponse.getAuthor())
                        .publishedYear(bookResponse.getPublishedYear())
                        .bookCategories(bookResponse.getBookCategories())
                        .loans(bookResponse.getLoans())
                        .build()).toList();

        return new ResponseEntity<>(mappedList, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<BookResponse> createBook(@RequestBody BookRequest request) {
        Book response = bookService.createBook(request);
        return ResponseEntity.ok(BookResponse.builder()
                .isbn(response.getIsbn())
                .title(response.getTitle())
                .author(response.getAuthor())
                .publishedYear(response.getPublishedYear())
                .build());
//TODO:añadir category con getter previo
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable("id") Long id) throws BookNotFound {
        Book response = bookService.findById(id);
        BookResponse mappedResponse = new BookResponse().builder()
                .isbn(response.getIsbn())
                .title(response.getTitle())
                .author(response.getAuthor())
                .publishedYear(response.getPublishedYear())
                .bookCategories(response.getBookCategories())
                .loans(response.getLoans())
                .build();
        return ResponseEntity.ok(mappedResponse);
    }

    @PutMapping("/{id}")
    public  ResponseEntity<BookResponse> updateBook(@PathVariable("id") Long id, @RequestBody BookRequest request) {
        Book response = bookService.updateBook(id, request);

        return ResponseEntity.ok(BookResponse.builder()
                .isbn(response.getIsbn())
                .title(response.getTitle())
                .author(response.getAuthor())
                .publishedYear(response.getPublishedYear())
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable("id") Long id) {
        bookService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @GetMapping("/byFilter")
    public ResponseEntity<List<BookResponse>> getBooksByFilter(@RequestParam(required = false) String author,
                                                               @RequestParam(required = false) String title,
                                                               @RequestParam(required = false) String year) {
        List<Book> request = bookService.findByFilter(author,title,year);
        List<BookResponse> mappedList = request.stream().map(bookResponse -> BookResponse.builder()
                .isbn(bookResponse.getIsbn())
                .title(bookResponse.getTitle())
                .author(bookResponse.getAuthor())
                .publishedYear(bookResponse.getPublishedYear())
                .bookCategories(bookResponse.getBookCategories())
                .loans(bookResponse.getLoans())
                .build()).toList();
        return ResponseEntity.ok(mappedList);
    }
}