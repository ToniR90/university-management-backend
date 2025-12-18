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
    void shouldRemoveDashes() {
        Phone phone = Phone.of("600-65-43-21");

        assertEquals("+34600654321", phone.getValue());
    }

    @Test
    void shouldNormalize0034Prefix() {
        Phone phone = Phone.of("0034600654321");

        assertEquals("+34600654321", phone.getValue());
    }

    @Test
    void shouldAddSpanishPrefixByDefault() {
        Phone phone = Phone.of("600654321");

        assertTrue(phone.getValue().startsWith("+34"));
    }

    @Test
    void shouldKeepNonSpanishInternationalPrefix() {
        Phone phone = Phone.of("+33612345678");

        assertEquals("+33612345678", phone.getValue());
    }

    @Test
    void shouldKeepExistingInternationalPrefix() {
        Phone phone = Phone.of("+34600654321");

        assertEquals("+34600654321", phone.getValue());
    }

    @Test
    void shouldBeEqualByValue() {
        Phone phone1 = Phone.of("600654321");
        Phone phone2 = Phone.of("600654321");

        assertEquals(phone1, phone2);
    }

    @Test
    void shouldHaveSameHashCodeForSameValue() {
        Phone phone1 = Phone.of("600654321");
        Phone phone2 = Phone.of("600654321");

        assertEquals(phone1.hashCode(), phone2.hashCode());
    }
}