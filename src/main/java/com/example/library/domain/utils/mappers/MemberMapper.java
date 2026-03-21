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

@Mapper(componentModel = "spring")
//@Data
public interface MemberMapper {

    // ✅ COMPLETO (pero controlado desde Loan)
    Member toDomain(MemberEntity entity);

    // 🔥 SIN LOANS (para evitar bucles)

    @Named("withoutLoans")
    @Mapping(target = "loans", ignore = true)
    Member toDomainWithoutLoans(MemberEntity entity);

    List<Member> toDomainList(List<MemberEntity> entities);

    @Named("listWithoutLoans")
    @Mapping(target = "loans", ignore = true)
    List<Member> toDomainListWithoutLoans(List<MemberEntity> entities);

}
//    public static Member toDomain(MemberEntity entity) {
//
//        return Member.builder()
//                .email(entity.getEmail())
//                .fullName(entity.getFullName())
//                .loans(
//                        entity.getLoans()
//                                .stream()
//                                .map(MemberMapper::toDomainLoan)
//                                .toList()
//                )
//                .build();
//    }
//    private static Loan toDomainLoan(LoanEntity entity) {
//
//        return Loan.builder()
//                .loanDate(entity.getLoanDate())
//                .DueDate(entity.getDueDate())
//                .returnDate(entity.getReturnDate())
//                .book(MemberMapper.toDomainBook(entity.getBook()))
//                .build();
//    }
//    public static Book toDomainBook(BookEntity entity) {
//
//        return Book.builder()
//                .isbn(entity.getIsbn())
//                .title(entity.getTitle())
//                .author(entity.getAuthor())
//                .publishedYear(entity.getPublishedYear())
//                .bookCategories(
//                        entity.getBookCategories()
//                                .stream()
//                                .map(MemberMapper::toDomainCategory)
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

