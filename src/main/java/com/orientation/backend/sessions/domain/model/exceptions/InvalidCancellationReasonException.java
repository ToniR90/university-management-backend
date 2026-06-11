package com.orientation.backend.sessions.domain.model.exceptions;

public class InvalidCancellationReasonException extends RuntimeException {
    public InvalidCancellationReasonException() {
        super("Cancelled reason cannot be empty");
    }
}
