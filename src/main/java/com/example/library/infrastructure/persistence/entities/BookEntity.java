package com.example.library.infrastructure.persistence.entities;

import com.example.library.application.model.BookRequest;
import jakarta.persistence.*;
import lombok.*;

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
    private Long bookId;
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

    @OneToMany(mappedBy = "books")
    private List<LoanEntity> loans;

    public BookEntity(BookRequest bookRequest) {
        this.isbn = bookRequest.getIsbn();
        this.title = bookRequest.getTitle();
        this.publishedYear = bookRequest.getPublishedYear();
        this.author = bookRequest.getAuthor();
    }

}
