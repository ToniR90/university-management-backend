package com.orientation.backend.shared.infrastructure.web.dto.response;

import com.orientation.backend.shared.application.exceptions.core.ErrorCode;
import com.orientation.backend.shared.application.exceptions.core.FieldErrorDetail;

import java.time.LocalDateTime;
import java.util.List;

public record ApiError(
		LocalDateTime timestamp,
		int status,
		ErrorCode errorCode,
		String message,
		List<FieldErrorDetail> errors
) {
}
