package com.example.library.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
