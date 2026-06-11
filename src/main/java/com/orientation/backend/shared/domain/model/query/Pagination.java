package com.orientation.backend.shared.domain.model.query;

import lombok.Getter;

/**
 * Pagination parameters for paginated queries.
 * Immutable and self-validated.
 *
 * This is a domain abstraction to avoid depending on Spring's PageRequest,
 * since the domain layer must not know anything about Spring.
 */
@Getter
public class Pagination {

    private final int page;
    private final int size;

    public Pagination(int page, int size) {
        if(page < 0){
            throw new IllegalArgumentException("El número de pàgina no pot ser negatiu");
        }
        this.page = page;
        if(size < 1 || size > 100){
            throw new IllegalArgumentException("Size ha de ser entre 1 i 100");
        }
        this.size = size;
    }
}
