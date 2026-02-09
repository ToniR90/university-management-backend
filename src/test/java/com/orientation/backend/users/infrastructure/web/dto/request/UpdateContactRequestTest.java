package com.orientation.backend.users.infrastructure.web.dto.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateContactRequestTest {

    @Test
    void shouldCreateWithBothFields() {
        UpdateContactRequest request = new UpdateContactRequest("joan@mail.com", "600123456");

        assertEquals("joan@mail.com", request.email());
        assertEquals("600123456", request.phone());
    }

    @Test
    void shouldNormalizeEmptyToNull() {
        UpdateContactRequest request = new UpdateContactRequest("  ", "");

        assertNull(request.email());
        assertNull(request.phone());
    }

    @Test
    void shouldTrimFields() {
        UpdateContactRequest request = new UpdateContactRequest("  joan@mail.com  ", "  600123456  ");

        assertEquals("joan@mail.com", request.email());
        assertEquals("600123456", request.phone());
    }

    @Test
    void shouldAllowNullFields() {
        UpdateContactRequest request = new UpdateContactRequest(null, null);

        assertNull(request.email());
        assertNull(request.phone());
    }

    @Test
    void shouldAllowOnlyEmail() {
        UpdateContactRequest request = new UpdateContactRequest("joan@mail.com", null);

        assertEquals("joan@mail.com", request.email());
        assertNull(request.phone());
    }

    @Test
    void shouldAllowOnlyPhone() {
        UpdateContactRequest request = new UpdateContactRequest(null, "600123456");

        assertNull(request.email());
        assertEquals("600123456", request.phone());
    }
}