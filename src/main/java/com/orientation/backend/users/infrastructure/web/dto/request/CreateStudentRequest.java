package com.orientation.backend.users.infrastructure.web.dto.request;

import jakarta.validation.constraints.*;

/**
 * DTO for creating a new student via REST API.
 * Contains all required fields with HTTP-level validation.
 */
public record CreateStudentRequest(

        @NotBlank(message = "DNI is required")
        @Pattern(regexp = "^[0-9]{8}[A-Z]$", message = "El DNI ha de contenir 8 dígits i una lletra majúscula")
        String dni,

        @NotBlank(message = "S'ha d'introduir un nom")
        @Size(min = 1, max = 100, message = "El nom ha de tenir entre 1 i 100 caràcters")
        String name,

        @NotBlank(message = "S'ha d'introduir el primer cognom")
        @Size(min = 1, max = 100, message = "El primer cognom ha de tenir entre 1 i 100 caràcters")
        String firstSurname,

        @Size(max = 100, message = "El segon cognom no pot tenir més de 100 caràcters")
        String secondSurname,

        @Email(message = "El format de l'email ha de ser vàlid")
        String email,

        @Pattern(regexp = "^[0-9]{9}$", message = "El telèfon ha de tenir 9 dígits")
        String phone,

        @NotBlank(message = "Es necessita un grau")
        @Size(min = 1, max = 200, message = "El grau ha de tenir entre 1 i 200 caràcters")
        String degree,

        @NotBlank(message = "S'ha d'introduir el curs")
        @Pattern(regexp = "^(FIRST|SECOND|THIRD|FOURTH|FIFTH|SIXTH)$",
                message = "Els curs han de ser FIRST, SECOND, THIRD, FOURTH, FIFTH, o SIXTH")
        String currentYear,

        @NotNull(message = "S'ha d'introduir si és alumni")
        Boolean isAlumni,

        String alumniType,

        @Min(value = 1900, message = "L'any de graduació ha de ser posterior al 1900")
        @Max(value = 2100, message = "L'any de graduació ha de ser abans del 2100")
        Integer alumniGraduationYear,

        @NotBlank(message = "Es necessita l'estat del RGPD")
        @Pattern(regexp = "^(ACCEPTED|REJECTED|PENDING)$",
                message = "L'estat ha de ser ACCEPTED, REJECTED, o PENDING")
        String rgpdConsentStatus,

        Integer rgpdSignedYear,

        @Size(max = 2000, message = "Les notes no poden ser més de 2000 caràcters")
        String counselorNotes
) {

    /**
     * Compact constructor for additional business validations.
     */
    public CreateStudentRequest {
        // Normalize optional fields
        secondSurname = normalizeString(secondSurname);
        email = normalizeString(email);
        phone = normalizeString(phone);
        alumniType = normalizeString(alumniType);
        counselorNotes = normalizeString(counselorNotes);

        // Business rule: if alumni, alumniType must be provided
        if (Boolean.TRUE.equals(isAlumni) && (alumniType == null || alumniType.isBlank())) {
            throw new IllegalArgumentException("Es necessita un tipus d'alumni si la persona ho és");
        }

        // Business rule: if alumni, graduation year should be provided
        if (Boolean.TRUE.equals(isAlumni) && alumniGraduationYear == null) {
            throw new IllegalArgumentException("Es necessita un any de graduació si la persona és alumni");
        }
    }

    /**
     * Normalizes a string by trimming and converting empty strings to null.
     */
    private static String normalizeString(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}