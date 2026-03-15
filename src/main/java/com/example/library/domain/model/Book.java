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
    private Long bookId;
    private String isbn;
    private String title;
    private String publishedYear;
    private String author;
    private List<Category> bookCategories;
    private List<Loan> loans;
}
