package com.orientation.backend.users.domain.model.query;

import com.orientation.backend.users.domain.model.enums.CurrentYear;
import com.orientation.backend.users.domain.model.enums.Degree;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AAA testing
 */

class StudentSearchCriteriaTest {

    @Test
    void shouldCreateValidStudentSearchCriteriaWithAllFields() {
        StudentSearchCriteria criteria = new StudentSearchCriteria("Dani", "12345678Z", CurrentYear.FIRST, true, Degree.VIDEOGAME_DESIGN);

        assertNotNull(criteria);
        assertEquals("Dani", criteria.getName());
        assertEquals("12345678Z", criteria.getDni());
        assertEquals(CurrentYear.FIRST, criteria.getCurrentYear());
        assertEquals(Degree.VIDEOGAME_DESIGN, criteria.getDegree());
        assertTrue(criteria.getIsAlumni());
    }

    @Test
    void shouldCreateValidStudentSearchCriteriaWithEmptyFields() {
        StudentSearchCriteria criteria = new StudentSearchCriteria(null, null, null, null, null);

        assertNotNull(criteria);
        assertNull(criteria.getName());
        assertNull(criteria.getDni());
        assertNull(criteria.getCurrentYear());
        assertNull(criteria.getDegree());
        assertNull(criteria.getIsAlumni());
    }
}