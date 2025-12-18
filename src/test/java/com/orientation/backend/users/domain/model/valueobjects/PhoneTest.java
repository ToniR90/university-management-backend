package com.orientation.backend.users.domain.model.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhoneTest {

    @Test
    void shouldCreateValidPhone() {
        Phone phone = Phone.of("600654321");

        assertNotNull(phone);
        assertEquals("+34600654321", phone.getValue());
    }

    @Test
    void shouldThrowExceptionForNull() {
        assertThrows(NullPointerException.class, () -> {
            Phone.of(null);
        });
    }

    @Test
    void shouldThrowExceptionForShort() {
        assertThrows(IllegalArgumentException.class, () -> {
            Phone.of("600");
        });
    }

    @Test
    void shouldThrowExceptionForLetter() {
        assertThrows(IllegalArgumentException.class, () -> {
            Phone.of("600abc321");
        });
    }

    @Test
    void shouldTrimWhitespace() {
        Phone phone = Phone.of("600 65 43 21");

        assertEquals("+34600654321", phone.getValue());
    }

    @Test
    void shouldDeleteEmptySpace() {
        Phone phone = Phone.of("600-65-43-21");

        assertEquals("+34600654321", phone.getValue());
    }

    @Test
    void shouldDelete00() {
        Phone phone = Phone.of("00600654321");

        assertEquals("+600654321", phone.getValue());
    }

    @Test
    void shouldReturnDefaultCountry() {
        Phone phone = Phone.of("600654321");

        assertEquals("+34600654321", phone.getValue());
    }

    @Test
    void shouldReturnPlus() {

    }

    @Test
    void getCountryCode() {
    }

    @Test
    void testEquals() {
    }

    @Test
    void testHashCode() {
    }

    @Test
    void testToString() {
    }
}

/*
private static String normalize(String phone) {
        // 1. Trim
        String cleaned = phone.trim();

        // 2. Delete empty spaces, "-"
        cleaned = cleaned.replaceAll("[\\s()\\-]", "");

        // 3. Convert 00 a +
        if (cleaned.startsWith("00")) {
            cleaned = "+" + cleaned.substring(2);
        }

        // 4. If there's no prefixes, default is Spain
        if (!cleaned.startsWith("+") && cleaned.matches("^[6789]\\d{8}$")) {
            cleaned = DEFAULT_COUNTRY_CODE + cleaned;
        }

        // 5. If there's no "+", but there's numbers
        if (!cleaned.startsWith("+") && cleaned.matches("^\\d+$")) {
            cleaned = DEFAULT_COUNTRY_CODE + cleaned;
        }

        return cleaned;
    }
 */