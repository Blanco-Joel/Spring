package com.example.library.domain.service;

import com.example.library.application.model.BookRequest;
import com.example.library.application.model.BookResponse;
import com.example.library.domain.model.Book;
import com.example.library.infrastructure.persistence.entities.BookEntity;
import com.example.library.infrastructure.persistence.repository.BookRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        List<BookEntity> response = bookRepository.findAll();
        return response.stream()
                .map(book -> Book.builder()
                        .isbn(book.getIsbn())
                        .title(book.getTitle())
                        .author(book.getAuthor())
                        .publishedYear(book.getPublishedYear())
                        .build())
                .toList();
    }
    public void createBook(BookRequest data)
    {
        BookEntity bookEntityData = new BookEntity(data);
        bookRepository.save(bookEntityData);
    }
    public Book findById(Long id) throws RuntimeException {
        Optional<BookEntity> bookEntity = bookRepository.findById(id);
        if (!bookEntity.isPresent()) {
            throw new RuntimeException("Book not found");
        }

        return bookEntity.map(book -> Book.builder()
                        .isbn(book.getIsbn())
                        .title(book.getTitle())
                        .author(book.getAuthor())
                        .publishedYear(book.getPublishedYear())
                        .build()).get();
    }
    public void updateBook(Long id, BookRequest data)
    {
        BookEntity bookEntity = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        updateIfNotBlank(data.getIsbn(), bookEntity::setIsbn);
        updateIfNotBlank(data.getTitle(), bookEntity::setTitle);
        updateIfNotBlank(data.getAuthor(), bookEntity::setAuthor);
        updateIfNotBlank(data.getPublishedYear(), bookEntity::setPublishedYear);
        bookRepository.save(bookEntity);
    }
    public static void updateIfNotBlank(String newValue, Consumer<String> setter) {
        if (newValue != null && !newValue.isBlank()) {
            setter.accept(newValue);
        }
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
    public List<Optional<BookEntity>> findByFilter(String author) {
        List<Optional<BookEntity>> books = bookRepository.findByAuthor(author);
        if (books.isEmpty())
        {
            return null;
        }
        List<Book> response = books.stream()
                .map(book -> Book.builder()
                        .isbn(book.get().getIsbn())
                        .title(book.get().getTitle())
                        .author(book.get().getAuthor())
                        .publishedYear(book.get().getPublishedYear())
                        .build())
                .toList();
        return books;
    }
}
