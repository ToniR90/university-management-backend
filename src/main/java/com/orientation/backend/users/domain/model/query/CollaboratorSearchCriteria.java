package com.orientation.backend.users.domain.model.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CollaboratorSearchCriteria {
    private final String name;
    private final Boolean external;
}
