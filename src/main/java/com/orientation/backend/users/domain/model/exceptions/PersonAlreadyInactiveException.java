package com.orientation.backend.users.domain.model.exceptions;

public class PersonAlreadyInactiveException extends RuntimeException {
    public PersonAlreadyInactiveException() {
        super("Person with given id is already inactive");
    }

    public PersonAlreadyInactiveException(String dni){
        super("Person with dni: " + dni + " already deactivated");
    }
}
