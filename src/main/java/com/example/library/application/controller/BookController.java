package com.example.library.application.controller;

import com.example.library.application.model.PagedResponse;
import com.example.library.application.model.book.BookRequest;
import com.example.library.application.model.book.BookResponse;
import com.example.library.application.model.exceptions.BookNotFound;
import com.example.library.application.utils.mappers.BookResponseMappers;
import com.example.library.domain.model.Book;
import com.example.library.domain.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con usuarios")
public class BookController {

    private final BookService bookService;
    private final BookResponseMappers bookResponseMappers;

    @GetMapping("/all")
    @Operation(summary = "Obtains the Book list")

    public ResponseEntity<PagedResponse<BookResponse>> getBooks(@RequestParam("page") int page,
                                                                @RequestParam("limit") int limit) {
        Page<Book> response = bookService.findAll(page, limit);

        Page<BookResponse> mappedResponse = response.map(bookResponseMappers::toResponse);

        return ResponseEntity.ok(PagedResponse.from(mappedResponse));
    }

    @PostMapping("/create")
    public ResponseEntity<BookResponse> createBook(@RequestBody(description = "Books data")
                                                   @org.springframework.web.bind.annotation.RequestBody() BookRequest request) {

        Book response = bookService.createBook(request);

        return new ResponseEntity<>(bookResponseMappers.toResponseMapperWithoutLoan(response), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@Parameter(description = "Book identifier") @PathVariable("id") Long id) throws BookNotFound {
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
    @Operation(summary = "Obtener usuario")
    @ApiResponse(responseCode = "200", description = "Finded book ")
    @ApiResponse(responseCode = "404", description = "Book not exist")
    public ResponseEntity<List<BookResponse>> getBooksByFilter(@RequestParam(required = false) String author,
                                                               @RequestParam(required = false) String title,
                                                               @RequestParam(required = false) String year) {
        List<Book> response = bookService.findByFilter(author,title,year);
        return new ResponseEntity<>(bookResponseMappers.toResponseList(response), HttpStatus.OK);

    }
}
