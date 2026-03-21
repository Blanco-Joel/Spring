package com.example.library.application.utils.mappers;

import com.example.library.application.model.book.BookResponse;
import com.example.library.application.model.member.MemberResponse;
import com.example.library.domain.model.Member;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemberResponseMappers {
    public MemberResponse responseMapper(Member response)
    {
        return MemberResponse.builder()
                .email(response.getEmail())
                .fullName(response.getFullName())
                .loans(response.getLoans())
                .build();
    }
    public List<MemberResponse> responseListMapper(List<Member> response)
    {
        return  response.stream().map(memberResponse -> MemberResponse.builder()
                .email(memberResponse.getEmail())
                .fullName(memberResponse.getFullName())
                .loans(memberResponse.getLoans())
                .build()).toList();

    }
    public MemberResponse responseMapperWithoutLoan(Member response)
    {
        return MemberResponse.builder()
                .email(response.getEmail())
                .fullName(response.getFullName())
                .loans(null)
                .build();

    }

}
