package com.orientation.backend.sessions.domain.model.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This class encapsulate all the parameters the client can send to make the filters
 * All the parameters can be null, as the filters are optional
 */
@Getter
@RequiredArgsConstructor
public class SessionSearchCriteria {
    private final String title;
}
