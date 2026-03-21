package com.example.library.application.model.exceptions;

public class MemberNotFound extends RuntimeException {
    public MemberNotFound(String message) {
        super(message);
    }
}
