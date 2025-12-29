package com.orientation.backend.users.domain.model.valueobjects;

import com.orientation.backend.users.domain.model.enums.AlumniType;
import org.junit.jupiter.api.Test;

import java.time.Year;

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
        AlumniInfo alumniInfo = AlumniInfo.createAlumni(AlumniType.DOCTORATE, 1901);

        assertTrue(alumniInfo.isAlumni());
    }

    // ========== Exceptions ==========
    @Test
    void shouldThrowExceptionWhenAlumniMissingType() {
        assertThrows(IllegalArgumentException.class, () -> {
            AlumniInfo.createAlumni(null, 1901);
        });
    }

    @Test
    void shouldThrowExceptionWhenAlumniMissingYear() {
        assertThrows(IllegalArgumentException.class, () -> {
            AlumniInfo.createAlumni(AlumniType.ERASMUS, null);
        });
    }

    @Test
    void shouldThrowExceptionForYearBefore1900() {
        assertThrows(IllegalArgumentException.class, () -> {
            AlumniInfo.createAlumni(AlumniType.MASTER, 1899);
        });
    }

    @Test
    void shouldThrowExceptionForFutureYear() {
        assertThrows(IllegalArgumentException.class, () -> {
            AlumniInfo.createAlumni(AlumniType.MASTER, 3000);
        });
    }

    @Test
    void shouldAcceptYear1900() {
        AlumniInfo alumniInfo = AlumniInfo.createAlumni(AlumniType.MASTER, 1900);

        assertTrue(alumniInfo.getGraduationYear().isPresent());
        assertEquals(1900, alumniInfo.getGraduationYear().get());
    }

    @Test
    void shouldAcceptCurrentYear() {
        AlumniInfo alumniInfo = AlumniInfo.createAlumni(AlumniType.ERASMUS, Year.now().getValue());

        assertTrue(alumniInfo.getGraduationYear().isPresent());
        assertEquals(Year.now().getValue(), alumniInfo.getGraduationYear().get());
    }

    @Test
    void shouldAcceptYear2000() {
        AlumniInfo alumniInfo = AlumniInfo.createAlumni(AlumniType.ERASMUS, 2000);

        assertTrue(alumniInfo.getGraduationYear().isPresent());
        assertEquals(2000, alumniInfo.getGraduationYear().get());
    }

    // ========== Equals & HashCode ==========

    @Test
    void shouldBeEqualByValue() {

    }

    @Test
    void shouldHaveSameHashCodeForSameValue() {

    }


}