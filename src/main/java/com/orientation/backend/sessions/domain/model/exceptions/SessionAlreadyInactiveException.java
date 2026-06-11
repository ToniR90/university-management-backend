package com.orientation.backend.sessions.domain.model.exceptions;

public class SessionAlreadyInactiveException extends RuntimeException {
    public SessionAlreadyInactiveException() {
        super("Session already inactive");
    }
}
