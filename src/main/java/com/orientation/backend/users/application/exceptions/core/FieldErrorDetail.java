package com.orientation.backend.users.application.exceptions.core;

public record FieldErrorDetail(
        String field,
        String message
) {}