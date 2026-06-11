package com.orientation.backend.shared.application.exceptions.core;

import lombok.Getter;

import java.util.List;

@Getter
public class BusinessValidationException extends RuntimeException {
	private final List<BusinessViolation> violations;
	private final ErrorCode errorCode;

	public BusinessValidationException(List<BusinessViolation> violations, ErrorCode errorCode) {
		super("Multiple business rule violations");
		this.violations = violations;
		this.errorCode = errorCode;
	}

}
