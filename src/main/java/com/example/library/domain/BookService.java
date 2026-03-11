package com.example.library.domain;

import com.example.library.application.BookMap;
import com.example.library.infrastructure.BookRepository;
import com.example.library.infrastructure.Book;
import org.springframework.stereotype.Service;

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
        return bookRepository.findAll();
    }
    public void createBook(BookMap data)
    {
        Book bookData = new Book(data);
        bookRepository.save(bookData);
    }
    public Optional<Book> findById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (!book.isEmpty())
        {
            return book;
        }
        return null;
    }
    public void updateBook(Long id, BookMap data)
    {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        updateIfNotBlank(data.getIsbn(), book::setIsbn);
        updateIfNotBlank(data.getTitle(), book::setTitle);
        updateIfNotBlank(data.getAuthor(), book::setAuthor);
        updateIfNotBlank(data.getPublishedYear(), book::setPublishedYear);
        bookRepository.save(book);
    }
    public static void updateIfNotBlank(String newValue, Consumer<String> setter) {
        if (newValue != null && !newValue.isBlank()) {
            setter.accept(newValue);
        }
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
    public List<Optional<Book>> findByAuthor(String author) {
        List<Optional<Book>> book = bookRepository.findByAuthor(author);
        if (!book.isEmpty())
        {
            return book;
        }
        return null;
    }
}
