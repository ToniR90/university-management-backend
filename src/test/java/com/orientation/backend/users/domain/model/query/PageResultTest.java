package com.orientation.backend.users.domain.model.query;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AAA Testing
 */

class PageResultTest {

    @Test
    void shouldCreateValidPageResult() {
        List<String> content = List.of("Student1", "Student2");
        PageResult<String> pageResult = new PageResult<>(content, 1, 1, 1, 1);

        assertNotNull(pageResult);
        assertFalse(pageResult.getContent().isEmpty());
        assertEquals(2, pageResult.getContent().size());
        assertEquals(1, pageResult.getPage());
        assertEquals(1, pageResult.getSize());
        assertEquals(1, pageResult.getTotalElements());
        assertEquals(1, pageResult.getTotalPages());
    }

}