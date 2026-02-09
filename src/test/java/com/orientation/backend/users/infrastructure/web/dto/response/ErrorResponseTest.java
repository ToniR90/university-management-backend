package com.orientation.backend.users.infrastructure.web.dto.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void shouldCreateErrorResponse() {
        ErrorResponse response = ErrorResponse.of(404, "Not Found", "No es troba l'estudiant");

        assertEquals(404, response.status());
        assertEquals("Not Found", response.error());
        assertEquals("No es troba l'estudiant", response.message());
        assertNotNull(response.timestamp());
    }

    @Test
    void shouldGenerateTimestampAutomatically() {
        ErrorResponse response1 = ErrorResponse.of(400, "Bad Request", "Error");
        ErrorResponse response2 = ErrorResponse.of(400, "Bad Request", "Error");

        assertNotNull(response1.timestamp());
        assertNotNull(response2.timestamp());
    }
}