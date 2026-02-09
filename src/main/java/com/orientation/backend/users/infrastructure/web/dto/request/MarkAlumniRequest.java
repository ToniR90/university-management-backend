package com.orientation.backend.users.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for marking a student as alumni.
 * Year and type validation handled by domain (AlumniInfo).
 */
public record MarkAlumniRequest(

        @NotBlank(message = "El tipus d'alumni és obligatori")
        String alumniType,

        @NotNull(message = "L'any de graduació és obligatori")
        Integer graduationYear
) {}