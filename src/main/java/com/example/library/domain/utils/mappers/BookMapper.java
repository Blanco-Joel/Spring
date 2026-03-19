package com.example.library.domain.utils.mappers;

import com.example.library.domain.model.Book;
import com.example.library.domain.model.Category;
import com.example.library.domain.model.Loan;
import com.example.library.domain.model.Member;
import com.example.library.infrastructure.persistence.entities.BookEntity;
import com.example.library.infrastructure.persistence.entities.CategoryEntity;
import com.example.library.infrastructure.persistence.entities.LoanEntity;
import com.example.library.infrastructure.persistence.entities.MemberEntity;
import lombok.Data;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {CategoryMapper.class, LoanMapper.class}
)
public interface BookMapper {

    @Mapping(target = "loans", qualifiedByName = "withoutMembersLoanList")
    Book toDomain(BookEntity entity);

    @Mapping(target = "loans", qualifiedByName = "withoutMembersLoanList")
    List<Book> toDomainList(List<BookEntity> entities);
}
//    public static Book toDomain(BookEntity entity) {
//
//        return Book.builder()
//                .isbn(entity.getIsbn())
//                .title(entity.getTitle())
//                .author(entity.getAuthor())
//                .publishedYear(entity.getPublishedYear())
//                .bookCategories(
//                        entity.getBookCategories()
//                                .stream()
//                                .map(BookMapper::toDomainCategory)
//                                .toList()
//                ).loans(
//                        entity.getLoans()
//                                .stream()
//                                .map(BookMapper::toDomainLoan)
//                                .toList()
//                )
//                .build();
//    }
//    public static Book toDomainWithoutLoan(BookEntity entity) {
//
//        return Book.builder()
//                .isbn(entity.getIsbn())
//                .title(entity.getTitle())
//                .author(entity.getAuthor())
//                .publishedYear(entity.getPublishedYear())
//                .bookCategories(
//                        entity.getBookCategories()
//                                .stream()
//                                .map(BookMapper::toDomainCategory)
//                                .toList()
//                )
//                .build();
//    }
//    private static Category toDomainCategory(CategoryEntity entity) {
//
//        return Category.builder()
//                .name(entity.getName())
//                .build();
//    }
//    private static Loan toDomainLoan(LoanEntity entity) {
//
//        return Loan.builder()
//                .loanDate(entity.getLoanDate())
//                .DueDate(entity.getDueDate())
//                .returnDate(entity.getReturnDate())
//                .member(toDomainMember(entity.getMember()))
//                .build();
//    }
//    private static Member toDomainMember(MemberEntity entity) {
//
//        return Member.builder()
//                .fullName(entity.getFullName())
//                .email(entity.getEmail())
//                .build();
//    }
