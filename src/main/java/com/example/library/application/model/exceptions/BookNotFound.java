package com.example.library.application.model.exceptions;

public class BookNotFound extends RuntimeException {

    public BookNotFound(String message) {
        super(message);
    }
}