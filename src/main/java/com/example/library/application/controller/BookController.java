package com.example.library.application.controller;

import com.example.library.application.model.book.BookRequest;
import com.example.library.application.model.book.BookResponse;
import com.example.library.application.model.exceptions.BookNotFound;
import com.example.library.application.utils.mappers.BookResponseMappers;
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
    private final BookResponseMappers bookResponseMappers;

    @GetMapping("/all")
    public ResponseEntity<List<BookResponse>> getBooks() {
        List<Book> response = bookService.findAll();

        return new ResponseEntity<>(bookResponseMappers.toResponseList(response), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<BookResponse> createBook(@RequestBody BookRequest request) {
        Book response = bookService.createBook(request);

        return new ResponseEntity<>(bookResponseMappers.toResponseMapperWithoutLoan(response), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable("id") Long id) throws BookNotFound {
        Book response = bookService.findById(id);

        return new ResponseEntity<>(bookResponseMappers.toResponse(response), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public  ResponseEntity<BookResponse> updateBook(@PathVariable("id") Long id, @RequestBody BookRequest request) {
        Book response = bookService.updateBook(id, request);

        return new ResponseEntity<>(bookResponseMappers.toResponse(response), HttpStatus.OK);
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
        List<Book> response = bookService.findByFilter(author,title,year);
        return new ResponseEntity<>(bookResponseMappers.toResponseList(response), HttpStatus.OK);

    }
}