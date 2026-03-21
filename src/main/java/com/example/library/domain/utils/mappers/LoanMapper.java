package com.example.library.domain.utils.mappers;

import com.example.library.domain.model.Loan;
import com.example.library.infrastructure.persistence.entities.LoanEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MemberMapper.class})
public interface LoanMapper {

    // ENTITY -> DOMAIN (evitando loops)
    @Named("withoutMembersLoan")
    @Mapping(source = "member", target = "member", qualifiedByName = "withoutLoans")
    @Mapping(target = "book", ignore = true)
    Loan toDomain(LoanEntity entity);

    @Named("withoutMembersLoanList")
    @IterableMapping(qualifiedByName = "withoutMembersLoan")
    List<Loan> toDomainList(List<LoanEntity> entities);

    // ENTITY -> DOMAIN COMPLETO (controlado)
    @Mapping(source = "member", target = "member", qualifiedByName = "withoutLoans")
    @Mapping(target = "book.loans", ignore = true) // 🔥 evita recursión infinita
    Loan toDomainFull(LoanEntity entity);

    // DOMAIN -> ENTITY
    @Mapping(target = "member", ignore = true)
    @Mapping(target = "book", ignore = true)
    LoanEntity toEntity(Loan loan);
}