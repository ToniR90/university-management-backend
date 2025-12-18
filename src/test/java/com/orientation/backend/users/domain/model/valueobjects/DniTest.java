package com.orientation.backend.users.domain.model.valueobjects;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DniTest {

    // ========== DNI ==========

    @Test
    void shouldCreateValidDni() {
        Dni dni = Dni.of("12345678Z");

        assertNotNull(dni);
        assertEquals("12345678Z", dni.getValue());
        assertTrue(dni.isDni());
        assertFalse(dni.isNie());
    }

    @Test
    void shouldCreateValidDniWithLeadingZeros() {
        Dni dni = Dni.of("00000000T");

        assertEquals("00000000T", dni.getValue());
    }

    @Test
    void shouldCreateValidDniWithHighNumber() {
        Dni dni = Dni.of("99999999R");

        assertEquals("99999999R", dni.getValue());
    }

    // ========== NIE ==========

    @Test
    void shouldCreateValidNieWithX() {
        Dni dni = Dni.of("X1234567L");

        assertEquals("X1234567L", dni.getValue());
        assertTrue(dni.isNie());
        assertFalse(dni.isDni());
    }

    @Test
    void shouldCreateValidNieWithY() {
        Dni dni = Dni.of("Y1234567X");

        assertEquals("Y1234567X", dni.getValue());
        assertTrue(dni.isNie());
        assertFalse(dni.isDni());
    }

    @Test
    void shouldCreateValidNieWithZ() {
        Dni dni = Dni.of("Z1234567R");

        assertEquals("Z1234567R", dni.getValue());
        assertTrue(dni.isNie());
        assertFalse(dni.isDni());
    }

    // ========== Normalize ==========

    @Test
    void shouldNormalizeDniToUppercase() {
        Dni dni = Dni.of("12345678z");

        assertEquals("12345678Z", dni.getValue());
    }

    @Test
    void shouldTrimWhitespace() {
        Dni dni = Dni.of("   12345678Z   ");

        assertEquals("12345678Z", dni.getValue());
    }

    @Test
    void shouldRemoveInternalSpaces() {
        Dni dni = Dni.of("123 456 78 Z");

        assertEquals("12345678Z", dni.getValue());
    }

    @Test
    void shouldRemoveDashes() {
        Dni dni = Dni.of("123-456-78-Z");

        assertEquals("12345678Z", dni.getValue());
    }

    // ========== Exceptions ==========

    @Test
    void shouldThrowExceptionForNull() {
        assertThrows(NullPointerException.class, () -> {
            Dni.of(null);
        });
    }

    @Test
    void shouldThrowExceptionForEmpty() {
       assertThrows(IllegalArgumentException.class, () -> {
           Dni.of("");
       });
    }

    @Test
    void shouldThrowExceptionForBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            Dni.of(" ");
        });
    }

    @Test
    void shouldThrowExceptionForTooShort() {
        assertThrows(IllegalArgumentException.class, () -> {
            Dni.of("123Z");
        });
    }

    @Test
    void shouldThrowExceptionForTooLong() {
        assertThrows(IllegalArgumentException.class, () -> {
            Dni.of("123456789Z");
        });
    }

    @Test
    void shouldThrowExceptionForOnlyNumbers() {
        assertThrows(IllegalArgumentException.class, () -> {
            Dni.of("123456789");
        });
    }

    @Test
    void shouldThrowExceptionForOnlyLetters() {
        assertThrows(IllegalArgumentException.class, () -> {
            Dni.of("ABCDEFGH");
        });
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