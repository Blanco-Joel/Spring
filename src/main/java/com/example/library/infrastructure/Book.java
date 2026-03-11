package com.example.library.infrastructure;

import com.example.library.application.BookMap;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table( name = "book")

public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)

    private Long id;
    private String isbn;
    private String title;
    private String publishedYear;
    private String author;

    public Book(BookMap bookMap) {
        this.isbn = bookMap.getIsbn();
        this.title = bookMap.getTitle();
        this.publishedYear = bookMap.getPublishedYear();
        this.author = bookMap.getAuthor();
    }
}
