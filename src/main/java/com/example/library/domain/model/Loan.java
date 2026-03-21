package com.example.library.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Loan {
    private String loanDate;
    private String dueDate;
    private String returnDate;
    private Member member;
    private Book book;
}
