package com.example.library.application.controller;

import com.example.library.application.model.BookRequest;
import com.example.library.application.model.BookResponse;
import com.example.library.domain.model.Book;
import com.example.library.domain.service.BookService;
import com.example.library.infrastructure.persistence.entities.BookEntity;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

        List<BookResponse> mappedList = new ArrayList<>();

        return new ResponseEntity<>(mappedList, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<BookResponse> createBook(@RequestBody BookRequest request) {
        bookService.createBook(request);

        return ResponseEntity.ok(new BookResponse());
    }

    @GetMapping("/{id}")
    public Optional<BookEntity> getBookById(@PathVariable("id") Long id) throws BadRequestException {
        return bookService.findById(id);
    }

    @PutMapping("/{id}")
    public void updateBook(@PathVariable("id") Long id, @RequestBody BookRequest request) {
        bookService.updateBook(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteBookById(@PathVariable("id") Long id) {
        bookService.deleteById(id);
    }

    @GetMapping("/byFilter")
    public List<Optional<BookEntity>> getBooksByAuthor(@RequestParam(required = false) String author,
                                                       @RequestParam(required = false) String title,
                                                       @RequestParam(required = false) String isbn) {
        return bookService.findByAuthor(author);
    }
}