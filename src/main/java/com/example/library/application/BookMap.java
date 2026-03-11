package com.example.library.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookMap {
    private String isbn;
    private String title;
    private String publishedYear;
    private String author;
}
