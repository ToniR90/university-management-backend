package com.orientation.backend.users.application.exceptions;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(Long id) {
        super("No es troba l'usuari amb id: " + id );
    }

    public StudentNotFoundException(String dni) {
        super("No es troba l'usuari amb dni: " + dni);
    }
}
