package com.orientation.backend.users.infrastructure.web.exception;

import com.orientation.backend.users.infrastructure.web.dto.response.ApiError;
import com.orientation.backend.users.application.exceptions.CreatedStudentException;
import com.orientation.backend.users.application.exceptions.StudentNotFoundException;
import com.orientation.backend.users.application.exceptions.UpdateStudentException;
import com.orientation.backend.users.application.exceptions.core.BusinessValidationException;
import com.orientation.backend.users.application.exceptions.core.FieldErrorDetail;
import com.orientation.backend.users.application.exceptions.core.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

	@ExceptionHandler(CreatedStudentException.class)
	public ResponseEntity<ApiError> handleCreatedStudent(CreatedStudentException ex) {
		return handleBusinessValidation(ex, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(StudentNotFoundException.class)
	public ResponseEntity<ApiError> handleStudentNotFound(StudentNotFoundException ex) {
		return handleBusinessValidation(ex, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UpdateStudentException.class)
	public ResponseEntity<ApiError> handleUpdateStudent(UpdateStudentException ex) {
		return handleBusinessValidation(ex, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(BusinessValidationException.class)
	public ResponseEntity<ApiError> handleBusinessValidationException(BusinessValidationException exception) {
		return handleBusinessValidation(exception, HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity<ApiError> handleBusinessValidation(BusinessValidationException exception,
			HttpStatus status) {
		List<FieldErrorDetail> fieldErrors = exception.getViolations().stream()
				.map(v -> new FieldErrorDetail(v.field(), v.message()))
				.toList();

		return buildErrorResponse(status, exception.getErrorCode(), exception.getMessage(), fieldErrors);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
		List<FieldErrorDetail> fieldErrors = exception.getBindingResult().getFieldErrors()
				.stream()
				.map(err -> new FieldErrorDetail(err.getField(), err.getDefaultMessage()))
				.toList();

		return buildErrorResponse(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR,
				"Multiple business rule violations", fieldErrors);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException exception) {
		// Domain validation errors (Value Objects) usually throw
		// IllegalArgumentException
		return buildErrorResponse(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR, exception.getMessage(),
				List.of());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleException(Exception exception) {
		log.error("Unexpected error occurred", exception);

		return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, "An unexpected error occurred", List.of());
	}

	private ResponseEntity<ApiError> buildErrorResponse(HttpStatus status, ErrorCode errorCode, String message,
			List<FieldErrorDetail> errors) {
		ApiError apiError = new ApiError(
				LocalDateTime.now(),
				status.value(),
				errorCode,
				message,
				errors);
		return ResponseEntity.status(status).body(apiError);
	}
}
