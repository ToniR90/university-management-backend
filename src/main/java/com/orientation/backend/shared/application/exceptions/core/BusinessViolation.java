package com.orientation.backend.shared.application.exceptions.core;

public record BusinessViolation(
		String field,
		String message,
		ErrorCode errorCode
) {}
