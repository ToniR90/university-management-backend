package com.orientation.backend.users.infrastructure.web.dto.response;

import com.orientation.backend.users.domain.model.entities.Student;
import com.orientation.backend.users.domain.model.enums.*;
import com.orientation.backend.users.domain.model.valueobjects.*;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class StudentResponseTest {

    // ========== Helper ==========
    private Student createFullStudent() {
        return Student.builder()
                .id(1L)
                .dni(Dni.of("12345678Z"))
                .fullName(FullName.of("Joan", "García", "López"))
                .email(Email.of("joan@mail.com"))
                .phone(Phone.of("600123456"))
                .degree(Degree.DIGITAL_MARKETING)
                .currentYear(CurrentYear.FIRST)
                .alumniInfo(AlumniInfo.createAlumni(AlumniType.BACHELOR, 2023))
                .rgpdConsent(RgpdConsent.alreadySigned(2020))
                .howDidYouKnowUs(DiscoveryChannel.WEBSITE)
                .howDidYouContactUs(ContactMethod.EMAIL)
                .counselorNotes("Bon estudiant")
                .build();
    }

    private Student createMinimalStudent() {
        return Student.builder()
                .dni(Dni.of("00000000T"))
                .fullName(FullName.of("Maria", "Martínez", null))
                .degree(Degree.COMPUTER_ENGINEERING)
                .currentYear(CurrentYear.SECOND)
                .build();
    }

    // ========== fromDomain Tests ==========
    @Test
    void shouldMapAllFieldsFromDomain() {
        Student student = createFullStudent();

        StudentResponse response = StudentResponse.fromDomain(student);

        assertEquals(1L, response.id());
        assertEquals("12345678Z", response.dni());
        assertEquals("Joan", response.name());
        assertEquals("García", response.firstSurname());
        assertEquals("López", response.secondSurname());
        assertEquals("joan@mail.com", response.email());
        assertEquals("+34600123456", response.phone());
        assertEquals("DIGITAL_MARKETING", response.degree());
        assertEquals("FIRST", response.currentYear());
        assertTrue(response.isAlumni());
        assertEquals("BACHELOR", response.alumniType());
        assertEquals(2023, response.alumniGraduationYear());
        assertEquals("ALREADY_SIGNED", response.rgpdConsentStatus());
        assertEquals(2020, response.rgpdSignedYear());
        assertEquals("WEBSITE", response.howDidYouKnowUs());
        assertEquals("EMAIL", response.howDidYouContactUs());
        assertEquals("Bon estudiant", response.counselorNotes());
    }

    @Test
    void shouldMapNullFieldsFromDomain() {
        Student student = createMinimalStudent();

        StudentResponse response = StudentResponse.fromDomain(student);

        assertEquals("00000000T", response.dni());
        assertEquals("Maria", response.name());
        assertEquals("Martínez", response.firstSurname());
        assertNull(response.secondSurname());
        assertNull(response.email());
        assertNull(response.phone());
        assertEquals("SECOND", response.currentYear());
        assertFalse(response.isAlumni());
        assertNull(response.alumniType());
        assertNull(response.alumniGraduationYear());
        assertEquals("PENDING", response.rgpdConsentStatus());
        assertNull(response.rgpdSignedYear());
        assertNull(response.rgpdSignedDate());
        assertNull(response.howDidYouKnowUs());
        assertNull(response.howDidYouContactUs());
        assertNull(response.counselorNotes());
    }
}