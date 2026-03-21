package com.example.library.domain.utils.mappers;

import com.example.library.domain.model.Book;
import com.example.library.infrastructure.persistence.entities.BookEntity;
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