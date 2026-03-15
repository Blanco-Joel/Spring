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
    private String DueDate;
    private String returnDate;
    private Member member;

}
