package com.example.library.domain.service;

import com.example.library.application.model.BookRequest;
import com.example.library.application.model.exceptions.BookNotFound;
import com.example.library.application.model.exceptions.LibraryException;
import com.example.library.domain.model.Book;
import com.example.library.domain.utils.mappers.BookMapper;
import com.example.library.infrastructure.persistence.entities.BookEntity;
import com.example.library.infrastructure.persistence.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;

    public List<Book> findAll() {
        try
        {
            List<BookEntity> response = bookRepository.findAll();
            return response.stream()
                    .map(BookMapper::toDomain)
                    .toList();

        }catch (Exception ex)
        {
            throw new LibraryException(ex.getMessage());
        }
    }
    public Book createBook(BookRequest data)
    {
        try{
            BookEntity bookEntityData = new BookEntity(data);
            BookEntity response = bookRepository.save(bookEntityData);
            return BookMapper.toDomain(response);

        }catch (Exception ex)
        {
            throw new LibraryException(ex.getMessage());
        }
    }

    public Book findById(Long id) throws BookNotFound {
        try{
            Optional<BookEntity> bookEntity = bookRepository.findById(id);
            if (bookEntity.isEmpty()) {
                throw new BookNotFound("Book not found");
            }

            return BookMapper.toDomain(bookEntity.get());

        }catch (Exception ex)
        {
            throw new LibraryException(ex.getMessage());
        }
    }
    public Book updateBook(Long id, BookRequest data)
    {
        try{
            BookEntity bookEntity = bookRepository.findById(id)
                    .orElseThrow(() -> new BookNotFound("Book not found"));
            updateIfNotBlank(data.getIsbn(), bookEntity::setIsbn);
            updateIfNotBlank(data.getTitle(), bookEntity::setTitle);
            updateIfNotBlank(data.getAuthor(), bookEntity::setAuthor);
            updateIfNotBlank(data.getPublishedYear(), bookEntity::setPublishedYear);
            return BookMapper.toDomain(bookRepository.save(bookEntity));

        }catch (Exception ex)
        {
            throw new LibraryException(ex.getMessage());
        }
    }
    public static void updateIfNotBlank(String newValue, Consumer<String> setter) {
        if (newValue != null && !newValue.isBlank()) {
            setter.accept(newValue);
        }
    }

    public void deleteById(Long id) {
        try{
            bookRepository.deleteById(id);
    
        }catch (Exception ex)
        {
            throw new LibraryException(ex.getMessage());
        }
    }
    public List<Book> findByFilter(String author,String title,String year) {
    try{
        List<BookEntity> books = bookRepository.findByFilter(title, author,  year);

        if (books.isEmpty())
        {
            throw new BookNotFound("Book not found with the filters entered");
        }

        return books.stream()
                .map(BookMapper::toDomain)
                .toList();

        } catch (Exception ex)
        {
            throw new LibraryException(ex.getMessage());
        }
    }
}
