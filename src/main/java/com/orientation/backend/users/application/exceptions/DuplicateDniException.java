package com.orientation.backend.users.application.exceptions;

public class DuplicateDniException extends RuntimeException {
    public DuplicateDniException(String dni) {
        super("No es pot crear l'usuari amb el DNI: " + dni + ". Aquest DNI ja existeix.");
    }
}
