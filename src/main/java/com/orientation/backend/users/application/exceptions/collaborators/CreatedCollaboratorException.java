package com.orientation.backend.users.application.exceptions.collaborators;

import com.orientation.backend.shared.application.exceptions.core.BusinessValidationException;
import com.orientation.backend.shared.application.exceptions.core.BusinessViolation;
import com.orientation.backend.shared.application.exceptions.core.ErrorCode;

import java.util.List;

public class CreatedCollaboratorException extends BusinessValidationException {
    public CreatedCollaboratorException(List<BusinessViolation> violations) {

        super(violations, ErrorCode.USER_ALREADY_EXISTS);
    }
}
