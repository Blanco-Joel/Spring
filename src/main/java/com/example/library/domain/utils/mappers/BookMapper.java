package com.example.library.domain.utils.mappers;

import com.example.library.domain.model.Book;
import com.example.library.domain.model.Category;
import com.example.library.domain.model.Loan;
import com.example.library.domain.model.Member;
import com.example.library.infrastructure.persistence.entities.BookEntity;
import com.example.library.infrastructure.persistence.entities.CategoryEntity;
import com.example.library.infrastructure.persistence.entities.LoanEntity;
import com.example.library.infrastructure.persistence.entities.MemberEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public class BookMapper {

    public static Book toDomain(BookEntity entity) {

        return Book.builder()
                .isbn(entity.getIsbn())
                .title(entity.getTitle())
                .author(entity.getAuthor())
                .publishedYear(entity.getPublishedYear())
                .bookCategories(
                        entity.getBookCategories()
                                .stream()
                                .map(BookMapper::toDomainCategory)
                                .toList()
                ).loans(
                        entity.getLoans()
                                .stream()
                                .map(BookMapper::toDomainLoan)
                                .toList()
                )
                .build();
    }

    private static Category toDomainCategory(CategoryEntity entity) {

        return Category.builder()
                .name(entity.getName())
                .build();
    }
    private static Loan toDomainLoan(LoanEntity entity) {

        return Loan.builder()
                .loanId(entity.getLoanId())
                .loanDate(entity.getLoanDate())
                .DueDate(entity.getLoanDate())
                .returnDate(entity.getReturnDate())
                .members(toDomainMember(entity.getMembers()))
                .build();
    }
    private static Member toDomainMember(MemberEntity entity) {

        return Member.builder()
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .build();
    }
}