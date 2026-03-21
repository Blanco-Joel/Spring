package com.example.library.application.model.member;

import com.example.library.domain.model.Category;
import com.example.library.domain.model.Loan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponse {
    private String fullName;
    private String email;
    private List<Loan> loans;
}
