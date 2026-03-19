package com.example.library.infrastructure.persistence.repository;

import com.example.library.infrastructure.persistence.entities.BookEntity;
import com.example.library.infrastructure.persistence.entities.CategoryEntity;
import com.example.library.infrastructure.persistence.entities.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<MemberEntity,Long > {

    @Query("""
                SELECT DISTINCT m FROM MemberEntity m
                WHERE (:email IS NULL OR m.email = :email)
                AND (:fullName IS NULL OR m.fullName = :fullName)
                """)
    List<MemberEntity> findByFilter(String email, String fullName);
}
