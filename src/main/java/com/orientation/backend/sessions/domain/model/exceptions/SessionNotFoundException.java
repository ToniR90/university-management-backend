package com.orientation.backend.sessions.domain.model.exceptions;

public class SessionNotFoundException extends RuntimeException {
    public SessionNotFoundException() {
        super("Session not found");
    }
}
