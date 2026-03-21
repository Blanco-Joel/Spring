## Petición
Quiero que analices las clases en los **Ejemplos** y quiero que me hagas las siguientes clases incluyendo la lógica de un CRUD pero en el Create tienes que verificar que el book id que se pida por request no esté relacionado a ningún otro loan

- LoanRepository
- LoanMapper
- LoanService
- LoanController

Quiero que tengas especial cuidado en las distintas relaciones de las entidades y evites problemas de StackOverflow a la hora de mapear 

Aquí te muestro las distintas entidades:


````Java
package com.example.library.infrastructure.persistence.entities;

import com.example.library.application.model.book.BookRequest;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table( name = "book")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String isbn;
    private String title;
    private String publishedYear;
    private String author;

    @ManyToMany
    @JoinTable(
            name = "book_categories",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<CategoryEntity> bookCategories;

    @JsonManagedReference
    @OneToMany(mappedBy = "book")
    private List<LoanEntity> loans;

    public BookEntity(BookRequest bookRequest) {
        this.isbn = bookRequest.getIsbn();
        this.title = bookRequest.getTitle();
        this.publishedYear = bookRequest.getPublishedYear();
        this.author = bookRequest.getAuthor();
    }

}

````

````java
package com.example.library.infrastructure.persistence.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@ToString(exclude = "book")
@AllArgsConstructor
@Table( name = "category")
@NoArgsConstructor
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)

    private Long id;
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "bookCategories")
    private List<BookEntity> book;
}
````
````java

package com.example.library.infrastructure.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@Table( name = "loan")
@NoArgsConstructor

public class LoanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String loanDate;
    private String dueDate;
    private String returnDate;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "book_id")
    private BookEntity book;
}

````
````java
package com.example.library.infrastructure.persistence.entities;

import com.example.library.application.model.book.BookRequest;
import com.example.library.application.model.member.MemberRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@Table( name = "member")
@NoArgsConstructor

public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String fullName;
    private String email;

    @OneToMany(mappedBy = "member")
    private List<LoanEntity> loans;
    public MemberEntity(MemberRequest memberRequest) {
        this.email = memberRequest.getEmail();
        this.fullName = memberRequest.getFullName();

    }
}

````
````java
package com.example.library.domain.utils.mappers;

import com.example.library.domain.model.Loan;
import com.example.library.infrastructure.persistence.entities.LoanEntity;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Mapper(componentModel = "spring", uses = MemberMapper.class)
public interface LoanMapper {

    @Named("withoutMembersLoan")
    @Mapping(source = "member", target = "member", qualifiedByName = "withoutLoans")
    @Mapping(target = "book", ignore = true)
    Loan toDomain(LoanEntity entity);

    @Named("withoutMembersLoanList")
    @IterableMapping(qualifiedByName = "withoutMembersLoan")
    List<Loan> toDomainList(List<LoanEntity> entities);
}

````
***
Quiero que añadas al Loan mapper la siguiente lógica, completandola con la tuya:
````java
package com.example.library.domain.utils.mappers;

import com.example.library.domain.model.Loan;
import com.example.library.infrastructure.persistence.entities.LoanEntity;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Mapper(componentModel = "spring", uses = MemberMapper.class)
public interface LoanMapper {

    @Named("withoutMembersLoan")
    @Mapping(source = "member", target = "member", qualifiedByName = "withoutLoans")
    @Mapping(target = "book", ignore = true)
    Loan toDomain(LoanEntity entity);

    @Named("withoutMembersLoanList")
    @IterableMapping(qualifiedByName = "withoutMembersLoan")
    List<Loan> toDomainList(List<LoanEntity> entities);
}
````
***

**Ejemplos**

- Repository
````java
package com.example.library.infrastructure.persistence.repository;

import com.example.library.infrastructure.persistence.entities.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity,Long > {
    @Query("""
                SELECT DISTINCT b FROM BookEntity b
                JOIN FETCH b.bookCategories bc
                WHERE (:title IS NULL OR b.title = :title)
                AND (:author IS NULL OR b.author = :author)
                AND (:year IS NULL OR b.publishedYear = :year)
                """)
    List<BookEntity> findByFilter(String title,String author,String year);

    @Query("SELECT DISTINCT b FROM BookEntity b JOIN FETCH b.bookCategories bc")
    Page<BookEntity> findAll(Pageable pageable);

    @Override
    @Query("SELECT DISTINCT b FROM BookEntity b JOIN FETCH b.bookCategories bc WHERE b.id = :id")
    Optional<BookEntity> findById(Long id);
}

````
- Mapper
````java

import com.example.library.domain.model.Book;
import com.example.library.domain.model.Category;
import com.example.library.domain.model.Loan;
import com.example.library.domain.model.Member;
import com.example.library.infrastructure.persistence.entities.BookEntity;
import com.example.library.infrastructure.persistence.entities.CategoryEntity;
import com.example.library.infrastructure.persistence.entities.LoanEntity;
import com.example.library.infrastructure.persistence.entities.MemberEntity;
import lombok.Data;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {CategoryMapper.class, LoanMapper.class}
)
public interface BookMapper {

    @Mapping(target = "loans", qualifiedByName = "withoutMembersLoanList")
    Book toDomain(BookEntity entity);

    @Mapping(target = "loans", qualifiedByName = "withoutMembersLoanList")
    List<Book> toDomainList(List<BookEntity> entities);
}
````
- Service
````java
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


````
- Model
````java
package com.example.library.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Book {
    private Long id;
    private String isbn;
    private String title;
    private String publishedYear;
    private String author;
    private List<Category> bookCategories;
    private List<Loan> loans;
}

````
- Controller
````java
package com.example.library.application.controller;

import com.example.library.application.model.PagedResponse;
import com.example.library.application.model.book.BookRequest;
import com.example.library.application.model.book.BookResponse;
import com.example.library.application.model.exceptions.BookNotFound;
import com.example.library.application.utils.mappers.BookResponseMappers;
import com.example.library.domain.model.Book;
import com.example.library.domain.service.BookService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
@SecurityRequirement(name = "bearerAuth")
public class BookController {

    private final BookService bookService;
    private final BookResponseMappers bookResponseMappers;

    @GetMapping("/all")
    public ResponseEntity<PagedResponse<BookResponse>> getBooks(@RequestParam("page") int page,
                                                                @RequestParam("limit") int limit) {
        Page<Book> response = bookService.findAll(page, limit);

        Page<BookResponse> mappedResponse = response.map(bookResponseMappers::toResponse);

        return ResponseEntity.ok(PagedResponse.from(mappedResponse));
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

````