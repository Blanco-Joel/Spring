package com.example.library.domain.service;

import com.example.library.application.model.book.BookRequest;
import com.example.library.application.model.exceptions.CategoryNotFound;
import com.example.library.application.model.exceptions.LibraryException;
import com.example.library.application.model.exceptions.MemberNotFound;
import com.example.library.application.model.member.MemberRequest;
import com.example.library.domain.model.Member;
import com.example.library.domain.utils.mappers.MemberMapper;
import com.example.library.infrastructure.persistence.entities.CategoryEntity;
import com.example.library.infrastructure.persistence.entities.MemberEntity;
import com.example.library.infrastructure.persistence.repository.CategoryRepository;
import com.example.library.infrastructure.persistence.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public List<Member> findAll() {
        try {
            List<MemberEntity> response = memberRepository.findAll();
            return memberMapper.toDomainList(response);

        } catch (Exception ex) {
            throw new LibraryException(ex.getMessage());
        }
    }

    public Member createMember(MemberRequest data) {
        try {
            MemberEntity memberEntityData = new MemberEntity(data);

            return memberMapper.toDomain(memberRepository.save(memberEntityData));

        } catch (Exception ex) {
            throw new LibraryException(ex.getMessage());
        }
    }

    public Member findById(Long id) throws MemberNotFound {
        try {
            Optional<MemberEntity> memberEntity = memberRepository.findById(id);
            if (memberEntity.isEmpty()) {
                throw new MemberNotFound("Member not found");
            }

            return memberMapper.toDomain(memberEntity.get());

        } catch (Exception ex) {
            throw new LibraryException(ex.getMessage());
        }
    }

    public Member updateMember(Long id, MemberRequest data) {
        try {
            MemberEntity memberEntity = memberRepository.findById(id)
                    .orElseThrow(() -> new MemberNotFound("Member not found"));
            updateIfNotBlank(data.getEmail(), memberEntity::setEmail);
            updateIfNotBlank(data.getFullName(), memberEntity::setFullName);

            return memberMapper.toDomain(memberRepository.save(memberEntity));

        } catch (Exception ex) {
            throw new LibraryException(ex.getMessage());
        }
    }

    public static void updateIfNotBlank(String newValue, Consumer<String> setter) {
        if (newValue != null && !newValue.isBlank()) {
            setter.accept(newValue);
        }
    }

    public void deleteById(Long id) {
        try {
            memberRepository.deleteById(id);

        } catch (Exception ex) {
            throw new LibraryException(ex.getMessage());
        }
    }

    public List<Member> findByFilter(String email, String fullName) {
        try {
            List<MemberEntity> members = memberRepository.findByFilter(email, fullName);

            if (members.isEmpty()) {
                throw new MemberNotFound("Member not found with the filters entered");
            }

            return memberMapper.toDomainList(members);

        } catch (Exception ex) {
            throw new LibraryException(ex.getMessage());
        }
    }
}
