package com.example.library.domain.utils.mappers;

import com.example.library.domain.model.Loan;
import com.example.library.infrastructure.persistence.entities.LoanEntity;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Mapper(componentModel = "spring", uses = MemberMapper.class)
public interface LoanMapper {

    @Named("withoutMembersLoan")
    @Mapping(source = "member", target = "member", qualifiedByName = "withoutLoans")
    @Mapping(target = "book", ignore = true)
    Loan toDomain(LoanEntity entity);

    @Named("withoutMembersLoanList")
    @IterableMapping(qualifiedByName = "withoutMembersLoan")
    List<Loan> toDomainList(List<LoanEntity> entities);
}
