package com.orientation.backend.users.domain.model.exceptions;

public class StudentAlreadyInactiveException extends RuntimeException {
    public StudentAlreadyInactiveException(Long id) {
        super("Lestudiant amb id: " + id + " ja està donat de baixa");
    }

    public StudentAlreadyInactiveException(String dni){
        super("L'estudiant amb dni: " + dni + " ja està donat de baixa");
    }
}
