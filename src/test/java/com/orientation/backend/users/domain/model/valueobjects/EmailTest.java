package com.orientation.backend.users.domain.model.valueobjects;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// AAA (Arrange-Act-Assert) Pattern

class EmailTest {

    @Test
    void shouldCreateValidEmail() {
        Email email = Email.of("test@test.com");

        assertNotNull(email);
        assertEquals("test@test.com", email.getValue());
    }

    @Test
    void shouldNormalizeEmailToLowercase() {
        Email email = Email.of("TEST@TEST.COM");

        assertEquals("test@test.com", email.getValue());
    }

    @Test
    void shouldTrimWhitespace() {
        Email email = Email.of("  test@test.com  ");

        assertEquals("test@test.com", email.getValue());
    }

    @Test
    void shouldThrowExceptionForNull() {
        assertThrows(NullPointerException.class, () -> {
            Email.of(null);
        });
    }

    @Test
    void shouldThrowExceptionForInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            Email.of("invalid-email");
        });
    }

    @Test
    void shouldThrowExceptionForEmailWithoutAt() {
        assertThrows(IllegalArgumentException.class, () -> {
            Email.of("testtest.com");
        });
    }

    @Test
    void shouldThrowExceptionForEmailWithoutDomain() {
        assertThrows(IllegalArgumentException.class, () -> {
            Email.of("test@");
        });
    }

    @Test
    void shouldBeEqualByValue() {
        Email email1 = Email.of("test@test.com");
        Email email2 = Email.of("test@test.com");

        assertEquals(email1, email2);
    }

    @Test
    void shouldHaveSameHashCodeForSameValue() {
        Email email1 = Email.of("test@test.com");
        Email email2 = Email.of("test@test.com");

        assertEquals(email1.hashCode(), email2.hashCode());
    }
}