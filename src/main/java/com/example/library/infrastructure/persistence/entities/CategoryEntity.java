package com.example.library.infrastructure.persistence.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@Table( name = "category")
@NoArgsConstructor

public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)

    private Long categoryId;
    private String name;

    @ManyToMany(mappedBy = "bookCategories")
    private Set<BookEntity> books;
}
