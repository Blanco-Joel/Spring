package com.example.library.infrastructure;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@Table( name = "loan")

public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private final int id;
    private final String loanDate;
    private final String DueDate;
    private final String returnDate;
    private final int memberId;
    private final int bookId;
}
