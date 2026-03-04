package com.orientation.backend.users.application.exceptions.core;

public record BusinessViolation(
		String field,
		String message,
		ErrorCode errorCode
) {}
