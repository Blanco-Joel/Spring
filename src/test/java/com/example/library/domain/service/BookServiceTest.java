package com.example.library.domain.service;

import com.example.library.application.model.book.BookRequest;
import com.example.library.application.model.exceptions.LibraryException;
import com.example.library.domain.model.Book;
import com.example.library.domain.utils.mappers.BookMapper;
import com.example.library.infrastructure.persistence.entities.BookEntity;
import com.example.library.infrastructure.persistence.entities.CategoryEntity;
import com.example.library.infrastructure.persistence.repository.BookRepository;
import com.example.library.infrastructure.persistence.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    @Test
    void findAllReturnsMappedPage() {
        BookEntity entity = BookEntity.builder().id(1L).title("Clean Code").build();
        Book domain = Book.builder().id(1L).title("Clean Code").build();
        Page<BookEntity> repositoryPage = new PageImpl<>(List.of(entity), PageRequest.of(0, 5), 1);

        given(bookRepository.findAll(PageRequest.of(0, 5))).willReturn(repositoryPage);
        given(bookMapper.toDomain(entity)).willReturn(domain);

        Page<Book> result = bookService.findAll(0, 5);

        assertThat(result.getContent()).containsExactly(domain);
        verify(bookRepository).findAll(PageRequest.of(0, 5));
        verify(bookMapper).toDomain(entity);
    }

    @Test
    void createBookSavesBookWhenAllCategoriesExist() {
        BookRequest request = BookRequest.builder()
                .isbn("isbn-1")
                .title("Domain-Driven Design")
                .author("Eric Evans")
                .publishedYear("2003")
                .categoriesIds(List.of(10L, 20L))
                .build();
        CategoryEntity firstCategory = new CategoryEntity(10L, "Architecture", null);
        CategoryEntity secondCategory = new CategoryEntity(20L, "Design", null);
        BookEntity savedEntity = BookEntity.builder().id(99L).title("Domain-Driven Design").build();
        Book mappedBook = Book.builder().id(99L).title("Domain-Driven Design").build();

        given(categoryRepository.findAllById(List.of(10L, 20L))).willReturn(List.of(firstCategory, secondCategory));
        given(bookRepository.save(any(BookEntity.class))).willReturn(savedEntity);
        given(bookMapper.toDomain(savedEntity)).willReturn(mappedBook);

        Book result = bookService.createBook(request);

        ArgumentCaptor<BookEntity> captor = ArgumentCaptor.forClass(BookEntity.class);
        verify(bookRepository).save(captor.capture());
        BookEntity persisted = captor.getValue();
        assertThat(persisted.getTitle()).isEqualTo("Domain-Driven Design");
        assertThat(persisted.getBookCategories()).containsExactly(firstCategory, secondCategory);
        assertThat(request.getCategoriesIds()).isNull();
        assertThat(result).isEqualTo(mappedBook);
    }

    @Test
    void createBookThrowsLibraryExceptionWhenNoCategoryExists() {
        BookRequest request = BookRequest.builder()
                .title("Refactoring")
                .categoriesIds(List.of(30L))
                .build();

        given(categoryRepository.findAllById(List.of(30L))).willReturn(List.of());

        assertThatThrownBy(() -> bookService.createBook(request))
                .isInstanceOf(LibraryException.class)
                .hasMessage("None of the entered categories exist");

        verify(bookRepository, never()).save(any(BookEntity.class));
    }

    @Test
    void createBookThrowsLibraryExceptionWhenSomeCategoriesAreMissing() {
        BookRequest request = BookRequest.builder()
                .title("Refactoring")
                .categoriesIds(List.of(30L, 40L))
                .build();
        CategoryEntity existingCategory = new CategoryEntity(30L, "Refactoring", null);

        given(categoryRepository.findAllById(List.of(30L, 40L))).willReturn(List.of(existingCategory));

        assertThatThrownBy(() -> bookService.createBook(request))
                .isInstanceOf(LibraryException.class)
                .hasMessage("The categories: 40 do not exist");

        verify(bookRepository, never()).save(any(BookEntity.class));
    }

    @Test
    void findByIdReturnsMappedBook() {
        BookEntity entity = BookEntity.builder().id(7L).title("Patterns").build();
        Book mapped = Book.builder().id(7L).title("Patterns").build();

        given(bookRepository.findById(7L)).willReturn(Optional.of(entity));
        given(bookMapper.toDomain(entity)).willReturn(mapped);

        Book result = bookService.findById(7L);

        assertThat(result).isEqualTo(mapped);
        verify(bookRepository).findById(7L);
        verify(bookMapper).toDomain(entity);
    }

    @Test
    void findByIdWrapsNotFoundAsLibraryException() {
        given(bookRepository.findById(8L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.findById(8L))
                .isInstanceOf(LibraryException.class)
                .hasMessage("Book not found");
    }

    @Test
    void updateBookUpdatesOnlyNonBlankFields() {
        BookRequest request = BookRequest.builder()
                .isbn("isbn-updated")
                .title("  ")
                .author("Updated Author")
                .publishedYear(null)
                .build();
        BookEntity entity = BookEntity.builder()
                .id(11L)
                .isbn("isbn-old")
                .title("Original Title")
                .author("Original Author")
                .publishedYear("1999")
                .build();
        Book updated = Book.builder().id(11L).isbn("isbn-updated").title("Original Title").author("Updated Author").build();

        given(bookRepository.findById(11L)).willReturn(Optional.of(entity));
        given(bookRepository.save(entity)).willReturn(entity);
        given(bookMapper.toDomain(entity)).willReturn(updated);

        Book result = bookService.updateBook(11L, request);

        assertThat(entity.getIsbn()).isEqualTo("isbn-updated");
        assertThat(entity.getTitle()).isEqualTo("Original Title");
        assertThat(entity.getAuthor()).isEqualTo("Updated Author");
        assertThat(entity.getPublishedYear()).isEqualTo("1999");
        assertThat(result).isEqualTo(updated);
    }

    @Test
    void deleteByIdDelegatesToRepository() {
        bookService.deleteById(15L);

        verify(bookRepository).deleteById(15L);
    }

    @Test
    void findByFilterReturnsMappedBooks() {
        BookEntity entity = BookEntity.builder().id(2L).title("TDD").build();
        Book mapped = Book.builder().id(2L).title("TDD").build();

        given(bookRepository.findByFilter("TDD", "Kent Beck", "2002")).willReturn(List.of(entity));
        given(bookMapper.toDomainList(List.of(entity))).willReturn(List.of(mapped));

        List<Book> result = bookService.findByFilter("Kent Beck", "TDD", "2002");

        assertThat(result).containsExactly(mapped);
        verify(bookRepository).findByFilter("TDD", "Kent Beck", "2002");
        verify(bookMapper).toDomainList(List.of(entity));
    }

    @Test
    void findByFilterWrapsEmptyResultAsLibraryException() {
        given(bookRepository.findByFilter("TDD", "Kent Beck", "2002")).willReturn(List.of());

        assertThatThrownBy(() -> bookService.findByFilter("Kent Beck", "TDD", "2002"))
                .isInstanceOf(LibraryException.class)
                .hasMessage("Book not found with the filters entered");
    }

    @Test
    void updateIfNotBlankInvokesSetterOnlyForTextValues() {
        StringBuilder value = new StringBuilder("initial");

        BookService.updateIfNotBlank("updated", text -> value.replace(0, value.length(), text));
        BookService.updateIfNotBlank(" ", text -> value.replace(0, value.length(), text));
        BookService.updateIfNotBlank(null, text -> value.replace(0, value.length(), text));

        assertThat(value.toString()).isEqualTo("updated");
    }
}
