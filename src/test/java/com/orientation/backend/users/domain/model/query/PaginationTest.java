package com.orientation.backend.users.domain.model.query;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *6 test: 2 happy path & 4 Error path
 * AAA testing (Arrange - Act - Assert)
 */

class PaginationTest {

    // ========== Happy Path ==========

    @Test
    void shouldCreateValidPagination() {
        Pagination pagination = new Pagination(1, 10);

        assertNotNull(pagination);
        assertEquals(1, pagination.getPage());
        assertEquals(10, pagination.getSize());
    }

    @Test
    void shouldCreateValidPaginationWithLimitValues() {
        Pagination pagination = new Pagination(0, 100);

        assertNotNull(pagination);
        assertEquals(0, pagination.getPage());
        assertEquals(100, pagination.getSize());
    }


    // ========== Error Path ==========

    @Test
    void shouldThrowExceptionWhenPageIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            Pagination pagination = new Pagination(-10, 10);
        });
    }

    @Test
    void shouldThrowExceptionWhenSizeIsZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            Pagination pagination = new Pagination(1, 0);
        });
    }

    @Test
    void shouldThrowExceptionWhenSizeIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            Pagination pagination = new Pagination(1, -10);
        });
    }

    @Test
    void shouldThrowExceptionWhenSizeIsBiggerThan100() {
        assertThrows(IllegalArgumentException.class, () -> {
            Pagination pagination = new Pagination(1, 105);
        });
    }

}