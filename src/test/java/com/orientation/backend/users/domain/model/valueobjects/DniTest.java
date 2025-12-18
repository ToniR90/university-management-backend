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
    }

    @Test
    void shouldCreateValidDniWithHighNumber() {
    }

    // ========== NIE ==========

    @Test
    void shouldCreateValidNieWithX() {
    }

    @Test
    void shouldCreateValidNieWithY() {
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