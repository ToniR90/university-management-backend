package com.orientation.backend.users.infrastructure.web.dto.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateStudentRequestTest {

    // ========== Normalization Tests ==========
    @Test
    void shouldCreateRequestWithAllFields() {
        CreateStudentRequest request = new CreateStudentRequest(
                "12345678Z", "Joan", "García", "López",
                "joan@mail.com", "600123456", "Informàtica", "FIRST"
        );

        assertNotNull(request);
        assertEquals("12345678Z", request.dni());
        assertEquals("Joan", request.name());
        assertEquals("García", request.firstSurname());
        assertEquals("López", request.secondSurname());
        assertEquals("joan@mail.com", request.email());
        assertEquals("600123456", request.phone());
        assertEquals("Informàtica", request.degree());
        assertEquals("FIRST", request.currentYear());
    }

    @Test
    void shouldNormalizeOptionalFieldsToNull() {
        CreateStudentRequest request = new CreateStudentRequest(
                "12345678Z", "Joan", "García", "  ",
                "", "   ", "Informàtica", "FIRST"
        );

        assertNull(request.secondSurname());
        assertNull(request.email());
        assertNull(request.phone());
    }

    @Test
    void shouldTrimOptionalFields() {
        CreateStudentRequest request = new CreateStudentRequest(
                "12345678Z", "Joan", "García", "  López  ",
                "  joan@mail.com  ", "  600123456  ", "Informàtica", "FIRST"
        );

        assertEquals("López", request.secondSurname());
        assertEquals("joan@mail.com", request.email());
        assertEquals("600123456", request.phone());
    }

    @Test
    void shouldAllowNullOptionalFields() {
        CreateStudentRequest request = new CreateStudentRequest(
                "12345678Z", "Joan", "García", null,
                null, null, "Informàtica", "FIRST"
        );

        assertNull(request.secondSurname());
        assertNull(request.email());
        assertNull(request.phone());
    }
}