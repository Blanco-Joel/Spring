package com.example.library.application.utils.mappers;

import com.example.library.application.model.book.BookResponse;
import com.example.library.domain.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
@Mapper(componentModel = "spring")

public interface BookResponseMappers {
    BookResponse toResponse(Book book);

    @Named("withoutLoans")
    @Mapping(target = "loans", ignore = true)
    BookResponse toResponseMapperWithoutLoan(Book book);

    List<BookResponse> toResponseList(List<Book> books);


}
