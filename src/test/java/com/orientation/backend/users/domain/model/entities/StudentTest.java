package com.orientation.backend.users.domain.model.entities;

import com.orientation.backend.users.domain.model.enums.CurrentYear;
import com.orientation.backend.users.domain.model.valueobjects.Dni;
import com.orientation.backend.users.domain.model.valueobjects.Email;
import com.orientation.backend.users.domain.model.valueobjects.FullName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    private Dni testDni;
    private FullName testFullName;

    @BeforeEach
    void setUp() {
        testDni = Dni.of("12345678Z");
        testFullName = FullName.of("Dante", "Alighiero", "Alighieri");
    }

    // ========== Helper Method ==========
    private Student createStudent() {
        return Student.builder()
                .dni(testDni)
                .fullName(testFullName)
                .degree("Videojocs")
                .currentYear(CurrentYear.FIRST)
                .build();
    }

    // ========== Builder Tests ==========
    @Test
    void shouldCreateStudentWithBuilder() {
        Student student = createStudent();

        assertNotNull(student);
    }

    @Test
    void shouldCreateStudentWithDefaults() {
        // TODO
    }

    @Test
    void shouldThrowExceptionForNullDni() {
        assertThrows(NullPointerException.class, () -> {
            Student student = Student.builder()
                    .dni(null)
                    .fullName(testFullName)
                    .degree("Videojocs")
                    .currentYear(CurrentYear.FIRST)
                    .build();
        });
    }

    @Test
    void
     shouldThrowExceptionForNullFullName() {
        assertThrows(NullPointerException.class, () -> {
            Student student = Student.builder()
                    .dni(testDni)
                    .fullName(null)
                    .degree("Videojocs")
                    .currentYear(CurrentYear.FIRST)
                    .build();
        });
    }

    @Test
    void shouldThrowExceptionForNullDegree() {
        assertThrows(NullPointerException.class, () -> {
            Student student = Student.builder()
                    .dni(testDni)
                    .fullName(testFullName)
                    .degree(null)
                    .currentYear(CurrentYear.FIRST)
                    .build();
        });
    }

    // ========== Email Management Tests ==========
    @Test
    void shouldAddEmail() {
        Student student = createStudent();
        student.addEmail(Email.of("dante@mail.com"));

        assertNotNull(student.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenAddingEmailTwice() {
        assertThrows(IllegalStateException.class, () -> {
            Student student = createStudent();
            student.addEmail(Email.of("dante@mail.com"));
            student.addEmail(Email.of("dante2@mail.com"));
        });
    }

    @Test
    void shouldUpdateEmail() {

    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistenEmail() {

    }

    @Test
    void shouldRemoveEmail() {

    }

    @Test
    void shouldThrowExceptionWhenRemovingNonExistenEmail() {

    }

    // ========== Phone Management Tests ==========
    @Test
    void shouldAddPhone() {

    }

    @Test
    void shouldThrowExceptionWhenAddingPhoneTwice() {

    }

    @Test
    void shouldUpdatePhone() {

    }

    @Test
    void shouldRemovePhone() {

    }

    // ========== Contact Info Atomic Tests ==========
    @Test
    void shouldUpdateContactInfoAtomically() {

    }

    @Test
    void shouldAllowNullInUpdateContactInfo() {

    }

    // ========== RGPD Management Tests ==========
    @Test
    void shouldGiveRgpdConsentInPerson() {

    }

    @Test
    void shouldGiveRgpdConsentOnline() {

    }

    @Test
    void shouldRegisterPreviousRgpdConsent() {

    }

    // ========== Alumni Management Tests ==========
    @Test
    void shouldMarkAsAlumni() {

    }

    @Test
    void shouldMarkAsNonAlumni() {

    }

    // ========== Discovery & Contact Tracking Tests ==========
    @Test
    void shouldRegisterDiscoveryChannel() {

    }

    @Test
    void shouldRegisterContactMethod() {

    }

    // ========== Notes Management Tests ==========
    @Test
    void shouldUpdateCounselorNotes() {

    }

    @Test
    void shouldTrimCounselorNotes() {

    }

    @Test
    void shouldAllowNullCounselorNotes() {

    }

    // ========== Metadata Tests ==========
    @Test
    void shouldUpdateUpdateAtWhenModifying() {

    }

    // ========== Equals & HashCode Tests ==========
    @Test
    void shouldBeEqualById() {

    }

    @Test
    void shouldBeEqualByDniWhenNoId() {

    }

    @Test
    void shouldNotBeEqualWithDifferentId() {

    }
}