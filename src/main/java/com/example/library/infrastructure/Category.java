package com.example.library.infrastructure;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@Table( name = "category")

public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)

    private final int id;
    private final String name;

}
