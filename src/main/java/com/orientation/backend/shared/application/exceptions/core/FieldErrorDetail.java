package com.orientation.backend.shared.application.exceptions.core;

public record FieldErrorDetail(
        String field,
        String message
) {}