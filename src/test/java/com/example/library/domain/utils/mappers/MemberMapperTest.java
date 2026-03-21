package com.example.library.domain.utils.mappers;

import com.example.library.domain.model.Member;
import com.example.library.infrastructure.persistence.entities.BookEntity;
import com.example.library.infrastructure.persistence.entities.CategoryEntity;
import com.example.library.infrastructure.persistence.entities.LoanEntity;
import com.example.library.infrastructure.persistence.entities.MemberEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemberMapperTest {

    private final MemberMapper mapper = new MemberMapperImpl();

    @Test
    void toDomainMapsMemberAndLoans() {
        BookEntity bookEntity = BookEntity.builder()
                .id(5L)
                .title("Clean Code")
                .bookCategories(List.of(new CategoryEntity(2L, "Programming", null)))
                .loans(null)
                .build();
        LoanEntity loanEntity = new LoanEntity(3L, "2026-03-20", "2026-03-27", null, null, bookEntity);
        MemberEntity entity = new MemberEntity(1L, "Joel", "joel@test.dev", List.of(loanEntity));

        Member member = mapper.toDomain(entity);

        assertThat(member.getFullName()).isEqualTo("Joel");
        assertThat(member.getEmail()).isEqualTo("joel@test.dev");
        assertThat(member.getLoans()).hasSize(1);
        assertThat(member.getLoans().getFirst().getBook().getTitle()).isEqualTo("Clean Code");
        assertThat(member.getLoans().getFirst().getMember()).isNull();
    }

    @Test
    void toDomainWithoutLoansSkipsLoans() {
        MemberEntity entity = new MemberEntity(1L, "Joel", "joel@test.dev", List.of(
                new LoanEntity(3L, "2026-03-20", "2026-03-27", null, null, null)
        ));

        Member member = mapper.toDomainWithoutLoans(entity);

        assertThat(member.getFullName()).isEqualTo("Joel");
        assertThat(member.getEmail()).isEqualTo("joel@test.dev");
        assertThat(member.getLoans()).isNull();
    }

    @Test
    void toDomainListMapsAllItems() {
        List<Member> members = mapper.toDomainList(List.of(
                new MemberEntity(1L, "Joel", "joel@test.dev", null),
                new MemberEntity(2L, "Ana", "ana@test.dev", null)
        ));

        assertThat(members).extracting(Member::getFullName).containsExactly("Joel", "Ana");
    }
}
