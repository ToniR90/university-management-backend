package com.orientation.backend.users.application.exceptions.collaborators;

import com.orientation.backend.shared.application.exceptions.core.BusinessValidationException;
import com.orientation.backend.shared.application.exceptions.core.BusinessViolation;
import com.orientation.backend.shared.application.exceptions.core.ErrorCode;

import java.util.List;
import java.util.UUID;

public class CollaboratorNotFoundException extends BusinessValidationException {
    public CollaboratorNotFoundException(String dni) {
        super(
                List.of(new BusinessViolation("dni", "User not found with dni " + dni, ErrorCode.USER_NOT_FOUND)),
                ErrorCode.USER_NOT_FOUND);
    }

    public CollaboratorNotFoundException(UUID id) {
        super(
                List.of(new BusinessViolation("id", "User not found with id " + id, ErrorCode.USER_NOT_FOUND)),
                ErrorCode.USER_NOT_FOUND);
    }
}