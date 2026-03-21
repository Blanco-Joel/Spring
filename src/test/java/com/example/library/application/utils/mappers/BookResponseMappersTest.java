package com.example.library.application.utils.mappers;

import com.example.library.application.model.book.BookResponse;
import com.example.library.domain.model.Book;
import com.example.library.domain.model.Category;
import com.example.library.domain.model.Loan;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BookResponseMappersTest {

    private final BookResponseMappers mapper = new BookResponseMappersImpl();

    @Test
    void toResponseMapsBookFields() {
        Loan loan = Loan.builder().loanDate("2026-03-20").build();
        Book book = Book.builder()
                .isbn("isbn-1")
                .title("Clean Code")
                .publishedYear("2008")
                .author("Robert C. Martin")
                .bookCategories(List.of(Category.builder().name("Programming").build()))
                .loans(List.of(loan))
                .build();

        BookResponse response = mapper.toResponse(book);

        assertThat(response.getIsbn()).isEqualTo("isbn-1");
        assertThat(response.getTitle()).isEqualTo("Clean Code");
        assertThat(response.getPublishedYear()).isEqualTo("2008");
        assertThat(response.getAuthor()).isEqualTo("Robert C. Martin");
        assertThat(response.getBookCategories()).extracting(Category::getName).containsExactly("Programming");
        assertThat(response.getLoans()).containsExactly(loan);
    }

    @Test
    void toResponseMapperWithoutLoanSkipsLoans() {
        Book book = Book.builder()
                .title("Refactoring")
                .loans(List.of(Loan.builder().loanDate("2026-03-20").build()))
                .build();

        BookResponse response = mapper.toResponseMapperWithoutLoan(book);

        assertThat(response.getTitle()).isEqualTo("Refactoring");
        assertThat(response.getLoans()).isNull();
    }

    @Test
    void toResponseListMapsAllItems() {
        List<BookResponse> responses = mapper.toResponseList(List.of(
                Book.builder().title("Clean Code").build(),
                Book.builder().title("Refactoring").build()
        ));

        assertThat(responses).extracting(BookResponse::getTitle).containsExactly("Clean Code", "Refactoring");
    }
}
