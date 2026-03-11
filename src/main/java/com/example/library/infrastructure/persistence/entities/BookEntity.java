package com.example.library.infrastructure.persistence.entities;

import com.example.library.application.model.BookRequest;
import jakarta.persistence.*;
import lombok.*;

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

    public BookEntity(BookRequest bookRequest) {
        this.isbn = bookRequest.getIsbn();
        this.title = bookRequest.getTitle();
        this.publishedYear = bookRequest.getPublishedYear();
        this.author = bookRequest.getAuthor();
    }
}
