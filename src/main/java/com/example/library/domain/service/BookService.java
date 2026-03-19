package com.example.library.domain.service;

import com.example.library.application.model.book.BookRequest;
import com.example.library.application.model.exceptions.BookNotFound;
import com.example.library.application.model.exceptions.CategoryNotFound;
import com.example.library.application.model.exceptions.LibraryException;
import com.example.library.domain.model.Book;
import com.example.library.domain.utils.mappers.BookMapper;
import com.example.library.infrastructure.persistence.entities.BookEntity;
import com.example.library.infrastructure.persistence.entities.CategoryEntity;
import com.example.library.infrastructure.persistence.repository.BookRepository;
import com.example.library.infrastructure.persistence.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;

    public Page<Book> findAll(int page, int limit) {
        try {
            Pageable pageable = PageRequest.of(page, limit);
            Page<BookEntity> response = bookRepository.findAll(pageable);

            return response.map(bookMapper::toDomain);

        } catch (Exception ex) {
            throw new LibraryException(ex.getMessage());
        }
    }
    public Book createBook(BookRequest data)
    {
        try{
            List<CategoryEntity>  categories = categoryRepository.findAllById(data.getCategoriesIds());
            List<Long> categoryIds = categories.stream()
                    .map(CategoryEntity::getId)
                    .toList();
            if (categories.isEmpty())
            {
                throw new CategoryNotFound("None of the entered categories exist");
            }
            List<Long> diff = new ArrayList<>(data.getCategoriesIds());
            diff.removeAll(categoryIds);
            if (!diff.isEmpty())
            {
                throw new CategoryNotFound(    "The categories: " + diff.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "))
                        + " do not exist");
            }
            data.setCategoriesIds(null);
            BookEntity bookEntityData = new BookEntity(data);
            bookEntityData.setBookCategories(categories);

            return bookMapper.toDomain(bookRepository.save(bookEntityData));

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

            return bookMapper.toDomain(bookEntity.get());

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

            return bookMapper.toDomain(bookRepository.save(bookEntity));

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

        return bookMapper.toDomainList(books);

        } catch (Exception ex)
        {
            throw new LibraryException(ex.getMessage());
        }
    }
}
