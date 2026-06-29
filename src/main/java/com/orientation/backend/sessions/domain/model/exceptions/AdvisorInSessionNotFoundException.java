package com.orientation.backend.sessions.domain.model.exceptions;

public class AdvisorInSessionNotFoundException extends RuntimeException {
    public AdvisorInSessionNotFoundException() {
        super("Advisor not found in session");
    }
}
