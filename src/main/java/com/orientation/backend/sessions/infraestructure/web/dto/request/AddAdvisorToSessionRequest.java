package com.orientation.backend.sessions.infraestructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AddAdvisorToSessionRequest (
       @NotBlank(message = "Advisor DNI is required")
       String advisorDni
){
}
