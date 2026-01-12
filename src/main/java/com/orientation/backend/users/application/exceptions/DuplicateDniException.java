package com.orientation.backend.users.application.exceptions;

public class DuplicateDniException extends RuntimeException {
    public DuplicateDniException(String message) {
        super(message);
    }
}
