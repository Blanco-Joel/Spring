package com.example.library.infrastructure.persistence.repository;

import com.example.library.infrastructure.persistence.entities.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LoanRepository extends JpaRepository<LoanEntity, Long> {

    @Query("""
        SELECT l FROM LoanEntity l
        WHERE l.book.id = :bookId
        AND l.returnDate IS NULL
    """)
    Optional<LoanEntity> findActiveLoanByBookId(Long bookId);
}