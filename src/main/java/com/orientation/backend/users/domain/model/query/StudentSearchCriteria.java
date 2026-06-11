package com.orientation.backend.users.domain.model.query;

import com.orientation.backend.users.domain.model.enums.CurrentYear;
import com.orientation.backend.users.domain.model.enums.Degree;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This class encapsulate all the parameters the client can send to make the filters
 * All the parameters can be null, as the filters are optional
 */
@Getter
@RequiredArgsConstructor
public class StudentSearchCriteria {
    private final String name;
    private final String dni;
    private final CurrentYear currentYear;
    private final Boolean isAlumni;
    private final Degree degree;
}
