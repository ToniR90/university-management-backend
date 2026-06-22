package com.orientation.backend.users.application.exceptions.students;

import com.orientation.backend.shared.application.exceptions.core.BusinessValidationException;
import com.orientation.backend.shared.application.exceptions.core.BusinessViolation;
import com.orientation.backend.shared.application.exceptions.core.ErrorCode;

import java.util.List;

public class UpdateStudentException extends BusinessValidationException {

	public UpdateStudentException(String field, String message) {
		super(List.of(new BusinessViolation(field, message, ErrorCode.BUSINESS_RULE_VIOLATION)),
				ErrorCode.USER_NOT_FOUND);
	}

	public UpdateStudentException(List<BusinessViolation> violations) {
		super(violations, ErrorCode.BUSINESS_RULE_VIOLATION);
	}
}
