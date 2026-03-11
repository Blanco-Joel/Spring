package com.example.library.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@Table( name = "member")

public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private final int id;
    private final String fullName;
    private final String email;

}
