package com.orientation.backend.sessions.domain.model.exceptions;

public class AssistantNotFoundException extends RuntimeException {
    public AssistantNotFoundException() {
        super("Assistant not found");
    }
}
