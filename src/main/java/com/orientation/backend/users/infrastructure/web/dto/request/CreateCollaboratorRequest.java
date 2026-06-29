package com.orientation.backend.users.infrastructure.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCollaboratorRequest (

        @NotBlank(message = "DNI is required")
        String dni,

        @NotBlank(message = "Name is required")
        @Size(max = 100)
        String name,

        @NotBlank(message = "Surname is required")
        @Size(max = 100)
        String surname,

        @Email(message = "Email format is not valid")
        String email,

        String phone,

        boolean external,

        String organizationName
){

    public CreateCollaboratorRequest {
        email = normalizeString(email);
        phone = normalizeString(phone);
        organizationName = normalizeString(organizationName);
    }

    private static String normalizeString(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}