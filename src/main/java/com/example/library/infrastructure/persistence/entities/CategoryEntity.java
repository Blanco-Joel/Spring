package com.example.library.infrastructure.persistence.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@Table( name = "category")
@NoArgsConstructor

public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)

    private Long id;
    private String name;

    @JsonBackReference
    @ManyToMany(mappedBy = "bookCategories")
    private List<BookEntity> book;
}
