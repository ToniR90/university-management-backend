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
    void ofNullable() {
    }

    @Test
    void getValue() {
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