package com.orientation.backend.sessions.infraestructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record CreateSessionRequest (

        @NotBlank(message = "Title is required")
        String title,

        String description,

        String motivation,

        @NotBlank(message = "Session type is required")
        String sessionType,

        @NotBlank(message = "Session origin is required")
        String sessionOrigin,

        boolean allWelcome,

        LocalDateTime startDateTime
){}
