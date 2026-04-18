package com.orientation.backend.users.infrastructure.web.dto.request;

import jakarta.validation.constraints.*;

/**
 * DTO for creating a new student via REST API.
 * Contains all required fields with HTTP-level validation.
 */
public record CreateStudentRequest(

        @NotBlank(message = "DNI is required")
        String dni,  // Validación profunda la hace Dni.of()

        @NotBlank(message = "S'ha d'introduir un nom")
        @Size(max = 100)
        String name,

        @NotBlank(message = "S'ha d'introduir el primer cognom")
        @Size(max = 100)
        String surname,

        @Email(message = "El format de l'email ha de ser vàlid")
        String email,

        String phone,  // Validación la hace Phone.of()

        @NotBlank(message = "Es necessita un grau")
        @Size(max = 200)
        String degree,

        @NotBlank(message = "S'ha d'introduir el curs")
        String currentYear  // Validación la hace CurrentYear.valueOf()
) {
    public CreateStudentRequest {
        email = normalizeString(email);
        phone = normalizeString(phone);
    }

    private static String normalizeString(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}