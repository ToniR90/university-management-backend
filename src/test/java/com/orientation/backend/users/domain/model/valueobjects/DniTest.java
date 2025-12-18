package com.orientation.backend.users.domain.model.valueobjects;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DniTest {

    // ========== DNI ==========

    @Test
    void shouldCreateValidDni() {
        Dni dni = Dni.of("12345678Z");

        assertNotNull(dni);
        assertEquals(("12345678Z"), dni.getValue());
        assertTrue(dni.isDni());
        assertFalse(dni.isNie());
    }

    @Test
    void shouldCreateValidDniWithLeadingZeros() {
        Dni dni = Dni.of("00000000T");

        assertEquals(("00000000T"), dni.getValue());
    }

    @Test
    void shouldCreateValidDniWithHighNumber() {
        Dni dni = Dni.of("99999999R");

        assertEquals(("99999999R"), dni.getValue());
    }

    // ========== NIE ==========

    @Test
    void shouldCreateValidNieWithX() {
        Dni dni = Dni.of("X1234567L");

        assertEquals(("X1234567L"), dni.getValue());
        assertTrue(dni.isNie());
        assertFalse(dni.isDni());
    }

    @Test
    void shouldCreateValidNieWithY() {
        Dni dni = Dni.of(("Y1234567X"));

        assertEquals(("Y1234567X"), dni.getValue());
        assertTrue(dni.isNie());
        assertFalse(dni.isDni());
    }

    @Test
    void shouldCreateValidNieWithZ() {
    }

    // ========== Normalize ==========

    @Test
    void shouldNormalizeDniToUppercase() {
    }

    @Test
    void shouldTrimWhitespace() {
    }

    @Test
    void shouldRemoveInternalSpaces() {
    }

    @Test
    void shouldRemoveDashes() {
    }

    // ========== Exceptions ==========

    @Test
    void shouldThrowExceptionForNull() {
    }

    @Test
    void shouldThrowExceptionForEmpty() {
    }

    @Test
    void shouldThrowExceptionForBlank() {
    }

    @Test
    void shouldThrowExceptionForTooShort() {
    }

    @Test
    void shouldThrowExceptionForTooLong() {
    }

    @Test
    void shouldThrowExceptionForOnlyNumbers() {
    }

    @Test
    void shouldThrowExceptionForOnlyLetters() {
    }

    @Test
    void shouldThrowExceptionForInvalidDniLetter() {
    }

    @Test
    void shouldThrowExceptionForAnotherInvalidDniLetter() {
    }

    @Test
    void shouldThrowExceptionForInvalidNieLetter() {
    }

    // ========== Equals & HashCode ==========

    @Test
    void shouldBeEqualByValue() {
    }

    @Test
    void shouldHaveSameHashCodeForSameValue() {
    }

    @Test
    void shouldNotBeEqualWithDifferentValue() {
    }
}