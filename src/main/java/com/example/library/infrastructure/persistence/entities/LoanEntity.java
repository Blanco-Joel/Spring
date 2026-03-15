package com.example.library.infrastructure.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @JsonIgnore
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "book_id")
    private BookEntity book;
}
