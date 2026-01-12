package com.orientation.backend.users.application.exceptions;

public class InvalidStudentOperationException extends RuntimeException {
    public InvalidStudentOperationException(String message) {
        super(message);
    }
}
