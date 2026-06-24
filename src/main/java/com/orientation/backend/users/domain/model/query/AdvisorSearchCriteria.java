package com.orientation.backend.users.domain.model.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This class encapsulate all the parameters the client can send to make the filters
 * All the parameters can be null, as the filters are optional
 */
@Getter
@RequiredArgsConstructor
public class AdvisorSearchCriteria {
    private final String name;
    private final String dni;
}
