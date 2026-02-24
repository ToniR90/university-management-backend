package com.orientation.backend.users.application.exceptions;

import com.orientation.backend.users.application.exceptions.core.BusinessValidationException;
import com.orientation.backend.users.application.exceptions.core.BusinessViolation;
import com.orientation.backend.users.application.exceptions.core.ErrorCode;

import java.util.List;

public class CreatedStudentException extends BusinessValidationException {
	public CreatedStudentException(List<BusinessViolation> violations) {
		super(violations, ErrorCode.USER_ALREADY_EXISTS);
	}
}
