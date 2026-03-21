package com.example.library.domain.service;

import com.example.library.application.model.exceptions.LibraryException;
import com.example.library.application.model.member.MemberRequest;
import com.example.library.domain.model.Member;
import com.example.library.domain.utils.mappers.MemberMapper;
import com.example.library.infrastructure.persistence.entities.MemberEntity;
import com.example.library.infrastructure.persistence.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberMapper memberMapper;

    @InjectMocks
    private MemberService memberService;

    @Test
    void findAllReturnsMappedMembers() {
        MemberEntity entity = new MemberEntity(1L, "Joel", "joel@test.dev", null);
        Member mapped = Member.builder().fullName("Joel").email("joel@test.dev").build();

        given(memberRepository.findAll()).willReturn(List.of(entity));
        given(memberMapper.toDomainList(List.of(entity))).willReturn(List.of(mapped));

        List<Member> result = memberService.findAll();

        assertThat(result).containsExactly(mapped);
        verify(memberRepository).findAll();
        verify(memberMapper).toDomainList(List.of(entity));
    }

    @Test
    void createMemberSavesEntityAndReturnsMappedMember() {
        MemberRequest request = MemberRequest.builder().fullName("Joel").email("joel@test.dev").build();
        MemberEntity savedEntity = new MemberEntity(2L, "Joel", "joel@test.dev", null);
        Member mapped = Member.builder().fullName("Joel").email("joel@test.dev").build();

        given(memberRepository.save(any(MemberEntity.class))).willReturn(savedEntity);
        given(memberMapper.toDomain(savedEntity)).willReturn(mapped);

        Member result = memberService.createMember(request);

        assertThat(result).isEqualTo(mapped);
        verify(memberRepository).save(any(MemberEntity.class));
        verify(memberMapper).toDomain(savedEntity);
    }

    @Test
    void findByIdReturnsMappedMember() {
        MemberEntity entity = new MemberEntity(3L, "Joel", "joel@test.dev", null);
        Member mapped = Member.builder().fullName("Joel").email("joel@test.dev").build();

        given(memberRepository.findById(3L)).willReturn(Optional.of(entity));
        given(memberMapper.toDomain(entity)).willReturn(mapped);

        Member result = memberService.findById(3L);

        assertThat(result).isEqualTo(mapped);
    }

    @Test
    void findByIdWrapsNotFoundAsLibraryException() {
        given(memberRepository.findById(4L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.findById(4L))
                .isInstanceOf(LibraryException.class)
                .hasMessage("Member not found");
    }

    @Test
    void updateMemberUpdatesOnlyNonBlankFields() {
        MemberRequest request = MemberRequest.builder().email("new@test.dev").fullName(" ").build();
        MemberEntity entity = new MemberEntity(5L, "Original Name", "old@test.dev", null);
        Member mapped = Member.builder().fullName("Original Name").email("new@test.dev").build();

        given(memberRepository.findById(5L)).willReturn(Optional.of(entity));
        given(memberRepository.save(entity)).willReturn(entity);
        given(memberMapper.toDomain(entity)).willReturn(mapped);

        Member result = memberService.updateMember(5L, request);

        assertThat(entity.getEmail()).isEqualTo("new@test.dev");
        assertThat(entity.getFullName()).isEqualTo("Original Name");
        assertThat(result).isEqualTo(mapped);
    }

    @Test
    void deleteByIdDelegatesToRepository() {
        memberService.deleteById(6L);

        verify(memberRepository).deleteById(6L);
    }

    @Test
    void findByFilterReturnsMappedMembers() {
        MemberEntity entity = new MemberEntity(7L, "Joel", "joel@test.dev", null);
        Member mapped = Member.builder().fullName("Joel").email("joel@test.dev").build();

        given(memberRepository.findByFilter("joel@test.dev", "Joel")).willReturn(List.of(entity));
        given(memberMapper.toDomainList(List.of(entity))).willReturn(List.of(mapped));

        List<Member> result = memberService.findByFilter("joel@test.dev", "Joel");

        assertThat(result).containsExactly(mapped);
        verify(memberRepository).findByFilter("joel@test.dev", "Joel");
        verify(memberMapper).toDomainList(List.of(entity));
    }

    @Test
    void findByFilterWrapsEmptyResultAsLibraryException() {
        given(memberRepository.findByFilter("joel@test.dev", "Joel")).willReturn(List.of());

        assertThatThrownBy(() -> memberService.findByFilter("joel@test.dev", "Joel"))
                .isInstanceOf(LibraryException.class)
                .hasMessage("Member not found with the filters entered");

        verify(memberMapper, never()).toDomainList(any());
    }

    @Test
    void updateIfNotBlankInvokesSetterOnlyForTextValues() {
        StringBuilder value = new StringBuilder("initial");

        MemberService.updateIfNotBlank("updated", text -> value.replace(0, value.length(), text));
        MemberService.updateIfNotBlank(" ", text -> value.replace(0, value.length(), text));
        MemberService.updateIfNotBlank(null, text -> value.replace(0, value.length(), text));

        assertThat(value.toString()).isEqualTo("updated");
    }
}
