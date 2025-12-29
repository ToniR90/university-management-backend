package com.orientation.backend.users.domain.model.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlumniInfoTest {

    // ========== Validation ==========
    @Test
    void shouldCreateNotAlumni() {
        AlumniInfo alumniInfo = AlumniInfo.notAlumni();

        assertFalse(alumniInfo.isAlumni());
    }

    @Test
    void shouldCreateAlumni() {

    }

    // ========== Exceptions ==========
    @Test
    void shouldThrowExceptionWhenAlumniMissingType() {

    }

    @Test
    void shouldThrowExceptionWhenAlumniMissingYear() {

    }

    @Test
    void shouldThrowExceptionForYearBefore1900() {

    }

    @Test
    void shouldThrowExceptionForFutureYear() {

    }

    @Test
    void shouldAcceptYear1900() {

    }

    @Test
    void shouldAcceptCurrentYear() {

    }

    @Test
    void shouldAcceptYear2000() {

    }

    // ========== Equals & HashCode ==========

    @Test
    void shouldBeEqualByValue() {

    }

    @Test
    void shouldHaveSameHashCodeForSameValue() {

    }


}