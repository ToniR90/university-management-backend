package com.orientation.backend.sessions.infraestructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AddAssistantRequest (
        @NotBlank(message = "Person DNI is required")
        String personDni
){
}
