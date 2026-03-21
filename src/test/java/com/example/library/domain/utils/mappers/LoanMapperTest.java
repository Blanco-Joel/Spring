package com.example.library.domain.utils.mappers;

import com.example.library.domain.model.Loan;
import com.example.library.infrastructure.persistence.entities.LoanEntity;
import com.example.library.infrastructure.persistence.entities.MemberEntity;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LoanMapperTest {

    private final LoanMapper mapper;

    LoanMapperTest() {
        LoanMapperImpl loanMapper = new LoanMapperImpl();
        ReflectionTestUtils.setField(loanMapper, "memberMapper", new MemberMapperImpl());
        this.mapper = loanMapper;
    }

    @Test
    void toDomainMapsLoanWithoutBookAndWithoutMemberLoans() {
        MemberEntity memberEntity = new MemberEntity(1L, "Joel", "joel@test.dev", List.of(
                new LoanEntity(10L, "2026-03-20", "2026-03-27", null, null, null)
        ));
        LoanEntity entity = new LoanEntity(2L, "2026-03-20", "2026-03-27", null, memberEntity, null);

        Loan loan = mapper.toDomain(entity);

        assertThat(loan.getLoanDate()).isEqualTo("2026-03-20");
        assertThat(loan.getDueDate()).isEqualTo("2026-03-27");
        assertThat(loan.getBook()).isNull();
        assertThat(loan.getMember()).isNotNull();
        assertThat(loan.getMember().getFullName()).isEqualTo("Joel");
        assertThat(loan.getMember().getLoans()).isNull();
    }

    @Test
    void toDomainListMapsAllItems() {
        LoanEntity first = new LoanEntity(1L, "2026-03-20", "2026-03-27", null, null, null);
        LoanEntity second = new LoanEntity(2L, "2026-03-21", "2026-03-28", null, null, null);

        List<Loan> loans = mapper.toDomainList(List.of(first, second));

        assertThat(loans).extracting(Loan::getLoanDate).containsExactly("2026-03-20", "2026-03-21");
    }
}
