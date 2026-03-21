package com.example.library.application.model.exceptions;

public class LoanNotFound extends RuntimeException {
    public LoanNotFound(String message) {
        super(message);
    }
}
