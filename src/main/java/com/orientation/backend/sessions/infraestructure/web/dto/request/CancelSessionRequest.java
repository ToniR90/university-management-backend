package com.orientation.backend.sessions.infraestructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CancelSessionRequest (
        @NotBlank
        String cancelReason
){}
