package com.orientation.backend.users.infrastructure.web.dto.response;

import java.time.LocalDateTime;

/**
 * Standard error response for the API.
 * Consistent error format across all endpoints.
 */
public record ErrorResponse(
        int status,
        String error,
        String message,
        LocalDateTime timestamp
) {

    public static ErrorResponse of(int status, String error, String message) {
        return new ErrorResponse(status, error, message, LocalDateTime.now());
    }
}