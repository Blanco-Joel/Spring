package com.example.library.domain.utils.mappers;

import com.example.library.domain.model.Book;
import com.example.library.domain.model.Loan;
import com.example.library.infrastructure.persistence.entities.BookEntity;
import com.example.library.infrastructure.persistence.entities.CategoryEntity;
import com.example.library.infrastructure.persistence.entities.LoanEntity;
import com.example.library.infrastructure.persistence.entities.MemberEntity;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BookMapperTest {

    private final BookMapper mapper;

    BookMapperTest() {
        BookMapperImpl bookMapper = new BookMapperImpl();
        LoanMapperImpl loanMapper = new LoanMapperImpl();
        ReflectionTestUtils.setField(loanMapper, "memberMapper", new MemberMapperImpl());
        ReflectionTestUtils.setField(bookMapper, "categoryMapper", new CategoryMapperImpl());
        ReflectionTestUtils.setField(bookMapper, "loanMapper", loanMapper);
        this.mapper = bookMapper;
    }

    @Test
    void toDomainMapsBookCategoriesAndLoans() {
        CategoryEntity category = new CategoryEntity(1L, "Programming", null);
        MemberEntity member = new MemberEntity(2L, "Joel", "joel@test.dev", null);
        LoanEntity loanEntity = new LoanEntity(3L, "2026-03-20", "2026-03-27", null, member, null);
        BookEntity entity = BookEntity.builder()
                .id(10L)
                .isbn("isbn-1")
                .title("Clean Code")
                .publishedYear("2008")
                .author("Robert C. Martin")
                .bookCategories(List.of(category))
                .loans(List.of(loanEntity))
                .build();

        Book book = mapper.toDomain(entity);

        assertThat(book.getId()).isEqualTo(10L);
        assertThat(book.getTitle()).isEqualTo("Clean Code");
        assertThat(book.getBookCategories())
                .extracting(com.example.library.domain.model.Category::getName)
                .containsExactly("Programming");
        assertThat(book.getLoans()).hasSize(1);
        Loan loan = book.getLoans().getFirst();
        assertThat(loan.getBook()).isNull();
        assertThat(loan.getMember().getFullName()).isEqualTo("Joel");
        assertThat(loan.getMember().getLoans()).isNull();
    }

    @Test
    void toDomainListMapsAllItems() {
        List<Book> books = mapper.toDomainList(List.of(
                BookEntity.builder().id(1L).title("Clean Code").build(),
                BookEntity.builder().id(2L).title("Refactoring").build()
        ));

        assertThat(books).extracting(Book::getTitle).containsExactly("Clean Code", "Refactoring");
    }
}
