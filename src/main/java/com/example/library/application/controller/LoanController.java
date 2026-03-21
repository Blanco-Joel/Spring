package com.example.library.application.controller;

import com.example.library.domain.model.Loan;
import com.example.library.domain.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/create")
    public ResponseEntity<Loan> createLoan(@RequestParam Long bookId,
                                           @RequestParam Long memberId,
                                           @RequestBody Loan request) {

        Loan response = loanService.createLoan(bookId, memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Loan>> getAll() {
        return ResponseEntity.ok(loanService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> getById(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Loan> update(@PathVariable Long id,
                                       @RequestBody Loan request) {

        return ResponseEntity.ok(loanService.updateLoan(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        loanService.deleteLoan(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}