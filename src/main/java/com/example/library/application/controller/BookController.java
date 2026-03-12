package com.example.library.application.controller;

import com.example.library.application.model.BookRequest;
import com.example.library.application.model.BookResponse;
import com.example.library.domain.model.Book;
import com.example.library.domain.service.BookService;
import com.example.library.infrastructure.persistence.entities.BookEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
                        .build()).toList();

        return new ResponseEntity<>(mappedList, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<BookResponse> createBook(@RequestBody BookRequest request) {
        bookService.createBook(request);
        return ResponseEntity.ok(new BookResponse().builder()
                .isbn(request.getIsbn())
                .title(request.getTitle())
                .author(request.getAuthor())
                .publishedYear(request.getPublishedYear())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable("id") Long id) throws RuntimeException {
        Book response = bookService.findById(id);
        BookResponse mappedResponse = new BookResponse().builder().isbn(response.getIsbn())
                .title(response.getTitle())
                .author(response.getAuthor())
                .publishedYear(response.getPublishedYear())
                .build();
        return ResponseEntity.ok(mappedResponse);
    }

    @PutMapping("/{id}")
    public  ResponseEntity<BookResponse> updateBook(@PathVariable("id") Long id, @RequestBody BookRequest request) {
        bookService.updateBook(id, request);
        return ResponseEntity.ok(new BookResponse().builder()
                .isbn(request.getIsbn())
                .title(request.getTitle())
                .author(request.getAuthor())
                .publishedYear(request.getPublishedYear())
                .build());
    }

    @DeleteMapping("/{id}")
    public void deleteBookById(@PathVariable("id") Long id) {
        bookService.deleteById(id);
    }

    @GetMapping("/byFilter")
    public List<Optional<BookEntity>> getBooksByAuthor(@RequestParam(required = false) String author,
                                                       @RequestParam(required = false) String title,
                                                       @RequestParam(required = false) String isbn) {
        return bookService.findByFilter(author); //WIP
    }
}