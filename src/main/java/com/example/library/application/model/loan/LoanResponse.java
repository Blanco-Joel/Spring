package com.example.library.application.model.loan;

import com.example.library.domain.model.Category;
import com.example.library.domain.model.Loan;
import com.example.library.domain.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanResponse {
    private String loanDate;
    private String DueDate;
    private String returnDate;
    private Member member;
}
