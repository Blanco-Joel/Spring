package com.example.library.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@Table( name = "loan")
@NoArgsConstructor

public class LoanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long loanId;
    private String loanDate;
    private String DueDate;
    private String returnDate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity members;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private BookEntity books;
}
