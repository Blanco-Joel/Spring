package com.example.library.application.controller;

import com.example.library.application.model.member.MemberRequest;
import com.example.library.application.model.member.MemberResponse;
import com.example.library.application.utils.mappers.MemberResponseMappers;
import com.example.library.domain.model.Member;
import com.example.library.domain.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

    @Mock
    private MemberService memberService;

    @Mock
    private MemberResponseMappers memberResponseMappers;

    @InjectMocks
    private MemberController memberController;

    @Test
    void getMembersReturnsMappedResponseList() {
        Member member = Member.builder().fullName("Joel").email("joel@test.dev").build();
        MemberResponse response = MemberResponse.builder().fullName("Joel").email("joel@test.dev").build();

        given(memberService.findAll()).willReturn(List.of(member));
        given(memberResponseMappers.responseListMapper(List.of(member))).willReturn(List.of(response));

        ResponseEntity<List<MemberResponse>> result = memberController.getMembers();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).containsExactly(response);
    }

    @Test
    void createBookReturnsMappedResponseWithoutLoans() {
        MemberRequest request = MemberRequest.builder().fullName("Joel").email("joel@test.dev").build();
        Member member = Member.builder().fullName("Joel").email("joel@test.dev").build();
        MemberResponse response = MemberResponse.builder().fullName("Joel").email("joel@test.dev").build();

        given(memberService.createMember(request)).willReturn(member);
        given(memberResponseMappers.responseMapperWithoutLoan(member)).willReturn(response);

        ResponseEntity<MemberResponse> result = memberController.createBook(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    void getBookByIdReturnsMappedResponse() {
        Member member = Member.builder().fullName("Joel").email("joel@test.dev").build();
        MemberResponse response = MemberResponse.builder().fullName("Joel").email("joel@test.dev").build();

        given(memberService.findById(3L)).willReturn(member);
        given(memberResponseMappers.responseMapper(member)).willReturn(response);

        ResponseEntity<MemberResponse> result = memberController.getBookById(3L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    void updateBookReturnsMappedResponse() {
        MemberRequest request = MemberRequest.builder().fullName("Joel Updated").email("joel@test.dev").build();
        Member member = Member.builder().fullName("Joel Updated").email("joel@test.dev").build();
        MemberResponse response = MemberResponse.builder().fullName("Joel Updated").email("joel@test.dev").build();

        given(memberService.updateMember(4L, request)).willReturn(member);
        given(memberResponseMappers.responseMapper(member)).willReturn(response);

        ResponseEntity<MemberResponse> result = memberController.updateBook(4L, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    void deleteBookByIdReturnsOkAndDelegates() {
        ResponseEntity<Void> result = memberController.deleteBookById(5L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(memberService).deleteById(5L);
    }

    @Test
    void getBooksByFilterReturnsMappedResponseList() {
        Member member = Member.builder().fullName("Joel").email("joel@test.dev").build();
        MemberResponse response = MemberResponse.builder().fullName("Joel").email("joel@test.dev").build();

        given(memberService.findByFilter("joel@test.dev", "Joel")).willReturn(List.of(member));
        given(memberResponseMappers.responseListMapper(List.of(member))).willReturn(List.of(response));

        ResponseEntity<List<MemberResponse>> result = memberController.getBooksByFilter("joel@test.dev", "Joel");

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).containsExactly(response);
    }
}
