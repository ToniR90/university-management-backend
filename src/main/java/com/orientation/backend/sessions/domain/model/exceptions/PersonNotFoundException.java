package com.orientation.backend.sessions.domain.model.exceptions;

public class PersonNotFoundException extends RuntimeException {
    public PersonNotFoundException() {
        super("Person not found");
    }
}
