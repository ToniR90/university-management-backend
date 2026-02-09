package com.orientation.backend.users.infrastructure.web.dto.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateRgpdRequestTest {

    @Test
    void shouldCreateWithStatusOnly() {
        UpdateRgpdRequest request = new UpdateRgpdRequest("SIGNED_IN_PERSON", null);

        assertEquals("SIGNED_IN_PERSON", request.rgpdConsentStatus());
        assertNull(request.signedYear());
    }

    @Test
    void shouldCreateWithStatusAndYear() {
        UpdateRgpdRequest request = new UpdateRgpdRequest("ALREADY_SIGNED", 2020);

        assertEquals("ALREADY_SIGNED", request.rgpdConsentStatus());
        assertEquals(2020, request.signedYear());
    }

    @Test
    void shouldCreatePending() {
        UpdateRgpdRequest request = new UpdateRgpdRequest("PENDING", null);

        assertEquals("PENDING", request.rgpdConsentStatus());
        assertNull(request.signedYear());
    }
}