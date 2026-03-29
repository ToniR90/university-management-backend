package com.orientation.backend.users.domain.model.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Generic class, can be used for all the Objects as Students, Sessions, Collaborators, ...
 * This is a domain abstraction to avoid depending on Spring's Page<T>
 */
@Getter
@RequiredArgsConstructor
public class PageResult<T> {

    private final List<T> content;
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;
}
