package com.example.library.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    private String isbn;
    private String title;
    private String publishedYear;
    private String author;
}
