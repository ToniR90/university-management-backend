package com.orientation.backend.users.infrastructure.web.dto.request;

import jakarta.validation.constraints.Email;

/**
 * DTO for updating student contact information.
 * At least one field should be provided (validated in service layer).
 */
public record UpdateContactRequest(

        @Email(message = "El format de l'email ha de ser vàlid")
        String email,

        String phone
) {
    public UpdateContactRequest {
        email = normalizeString(email);
        phone = normalizeString(phone);
    }

    private static String normalizeString(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}