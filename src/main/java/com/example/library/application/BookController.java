package com.example.library.application;

import com.example.library.domain.BookService;
import com.example.library.infrastructure.Book;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/getall")
    public List<Book> getBooks() {
        return bookService.findAll();
    }

    @PostMapping("/create")
    public void createBook(@RequestBody BookMap request) {
        bookService.createBook(request);
    }
    @GetMapping("/getById/{id}")
    public Optional<Book> getBookById(@PathVariable("id") Long id) {
        return bookService.findById(id);
    }
    @PutMapping("/update/{id}")
    public void updateBook(@PathVariable("id") Long id, @RequestBody BookMap request) {
        bookService.updateBook(id, request);
    }
    @DeleteMapping("/getById/{id}")
    public void deleteBookById(@PathVariable("id") Long id) {
        bookService.deleteById(id);
    }
    @GetMapping("/getById/")
    public List<Optional<Book>> getBooksByAuthor(@RequestParam String author) {
        return bookService.findByAuthor(author);
    }
}