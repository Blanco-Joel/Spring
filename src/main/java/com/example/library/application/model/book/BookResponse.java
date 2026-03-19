package com.example.library.application.model.book;

import com.example.library.domain.model.Category;
import com.example.library.domain.model.Loan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    private String isbn;
    private String title;
    private String publishedYear;
    private String author;
    private List<Category> bookCategories;
    private List<Loan> loans;
}
