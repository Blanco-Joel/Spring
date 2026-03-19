package com.example.library.infrastructure.persistence.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@ToString(exclude = "book")
@AllArgsConstructor
@Table( name = "category")
@NoArgsConstructor
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)

    private Long id;
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "bookCategories")
    private List<BookEntity> book;
}
