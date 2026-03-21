package com.example.library.domain.service;

import com.example.library.application.model.exceptions.LibraryException;
import com.example.library.application.model.exceptions.BookNotFound;
import com.example.library.application.model.exceptions.MemberNotFound;
import com.example.library.domain.model.Loan;
import com.example.library.domain.utils.mappers.LoanMapper;
import com.example.library.infrastructure.persistence.entities.BookEntity;
import com.example.library.infrastructure.persistence.entities.LoanEntity;
import com.example.library.infrastructure.persistence.entities.MemberEntity;
import com.example.library.infrastructure.persistence.repository.BookRepository;
import com.example.library.infrastructure.persistence.repository.LoanRepository;
import com.example.library.infrastructure.persistence.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final LoanMapper loanMapper;

    // CREATE
    public Loan createLoan(Long bookId, Long memberId, Loan data) {
        try {
            // 🔥 VALIDACIÓN CLAVE
            loanRepository.findActiveLoanByBookId(bookId)
                    .ifPresent(l -> {
                        throw new LibraryException("This book is already loaned");
                    });

            BookEntity book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new BookNotFound("Book not found"));

            MemberEntity member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new MemberNotFound("Member not found"));

            LoanEntity entity = loanMapper.toEntity(data);
            entity.setBook(book);
            entity.setMember(member);

            return loanMapper.toDomainFull(loanRepository.save(entity));

        } catch (Exception ex) {
            throw new LibraryException(ex.getMessage());
        }
    }

    // READ ALL
    public List<Loan> findAll() {
        try {
            return loanMapper.toDomainList(loanRepository.findAll());
        } catch (Exception ex) {
            throw new LibraryException(ex.getMessage());
        }
    }

    // READ BY ID
    public Loan findById(Long id) {
        try {
            LoanEntity entity = loanRepository.findById(id)
                    .orElseThrow(() -> new LibraryException("Loan not found"));

            return loanMapper.toDomainFull(entity);

        } catch (Exception ex) {
            throw new LibraryException(ex.getMessage());
        }
    }

    // UPDATE
    public Loan updateLoan(Long id, Loan data) {
        try {
            LoanEntity entity = loanRepository.findById(id)
                    .orElseThrow(() -> new LibraryException("Loan not found"));

            if (data.getLoanDate() != null) entity.setLoanDate(data.getLoanDate());
            if (data.getDueDate() != null) entity.setDueDate(data.getDueDate());
            if (data.getReturnDate() != null) entity.setReturnDate(data.getReturnDate());

            return loanMapper.toDomainFull(loanRepository.save(entity));

        } catch (Exception ex) {
            throw new LibraryException(ex.getMessage());
        }
    }

    // DELETE
    public void deleteLoan(Long id) {
        try {
            loanRepository.deleteById(id);
        } catch (Exception ex) {
            throw new LibraryException(ex.getMessage());
        }
    }
}