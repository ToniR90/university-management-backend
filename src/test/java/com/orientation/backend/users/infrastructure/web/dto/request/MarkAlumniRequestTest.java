package com.orientation.backend.users.infrastructure.web.dto.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MarkAlumniRequestTest {

    @Test
    void shouldCreateRequest() {
        MarkAlumniRequest request = new MarkAlumniRequest("BACHELOR", 2023);

        assertEquals("BACHELOR", request.alumniType());
        assertEquals(2023, request.graduationYear());
    }

    @Test
    void shouldAcceptAllAlumniTypes() {
        MarkAlumniRequest bachelor = new MarkAlumniRequest("BACHELOR", 2020);
        MarkAlumniRequest master = new MarkAlumniRequest("MASTER", 2021);
        MarkAlumniRequest doctorate = new MarkAlumniRequest("DOCTORATE", 2022);

        assertEquals("BACHELOR", bachelor.alumniType());
        assertEquals("MASTER", master.alumniType());
        assertEquals("DOCTORATE", doctorate.alumniType());
    }
}