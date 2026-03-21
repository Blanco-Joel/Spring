package com.example.library.application.utils.mappers;

import com.example.library.application.model.member.MemberResponse;
import com.example.library.domain.model.Loan;
import com.example.library.domain.model.Member;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemberResponseMappersTest {

    private final MemberResponseMappers mapper = new MemberResponseMappers();

    @Test
    void responseMapperMapsMemberFields() {
        Loan loan = Loan.builder().loanDate("2026-03-20").build();
        Member member = Member.builder()
                .fullName("Joel")
                .email("joel@test.dev")
                .loans(List.of(loan))
                .build();

        MemberResponse response = mapper.responseMapper(member);

        assertThat(response.getFullName()).isEqualTo("Joel");
        assertThat(response.getEmail()).isEqualTo("joel@test.dev");
        assertThat(response.getLoans()).containsExactly(loan);
    }

    @Test
    void responseListMapperMapsAllMembers() {
        List<MemberResponse> responses = mapper.responseListMapper(List.of(
                Member.builder().fullName("Joel").email("joel@test.dev").build(),
                Member.builder().fullName("Ana").email("ana@test.dev").build()
        ));

        assertThat(responses).extracting(MemberResponse::getFullName).containsExactly("Joel", "Ana");
    }

    @Test
    void responseMapperWithoutLoanSkipsLoans() {
        Member member = Member.builder()
                .fullName("Joel")
                .email("joel@test.dev")
                .loans(List.of(Loan.builder().loanDate("2026-03-20").build()))
                .build();

        MemberResponse response = mapper.responseMapperWithoutLoan(member);

        assertThat(response.getFullName()).isEqualTo("Joel");
        assertThat(response.getEmail()).isEqualTo("joel@test.dev");
        assertThat(response.getLoans()).isNull();
    }
}
