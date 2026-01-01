package com.orientation.backend.users.domain.model.entities;

import com.orientation.backend.users.domain.model.enums.CurrentYear;
import com.orientation.backend.users.domain.model.enums.RgpdConsentStatus;
import com.orientation.backend.users.domain.model.valueobjects.Dni;
import com.orientation.backend.users.domain.model.valueobjects.Email;
import com.orientation.backend.users.domain.model.valueobjects.FullName;
import com.orientation.backend.users.domain.model.valueobjects.Phone;
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
        assertEquals(testDni, student.getDni());
        assertEquals(testFullName, student.getFullName());
        assertEquals("Videojocs", student.getDegree());
        assertEquals(CurrentYear.FIRST, student.getCurrentYear());
    }

    @Test
    void shouldCreateStudentWithDefaults() {
        Student student = createStudent();

        assertFalse(student.getAlumniInfo().isAlumni());
        assertEquals(RgpdConsentStatus.PENDING, student.getRgpdConsent().getStatus());
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
        Email email = Email.of("dante@mail.com");

        student.addEmail(email);

        assertTrue(student.getEmail().isPresent());
        assertEquals(email, student.getEmail().get());
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
        Student student = createStudent();
        Email email = Email.of("dante@mail.com");
        student.addEmail(email);
        Email email1 = Email.of("alighiero@mail.com");
        student.updateEmail(email1);

        assertTrue(student.getEmail().isPresent());
        assertEquals(email1, student.getEmail().get());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentEmail() {
        assertThrows(IllegalStateException.class, () -> {
            Student student = createStudent();
            Email email = Email.of("alighiero@mail.com");
            student.updateEmail(email);
        });
    }

    @Test
    void shouldRemoveEmail() {
        Student student = createStudent();
        Email email = Email.of("dante@mail.com");
        student.addEmail(email);
        student.removeEmail();

        assertTrue(student.getEmail().isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenRemovingNonExistentEmail() {
        assertThrows(IllegalStateException.class, () -> {
            Student student = createStudent();

            student.removeEmail();
        });
    }

    // ========== Phone Management Tests ==========
    @Test
    void shouldAddPhone() {
        Student student = createStudent();
        Phone phone = Phone.of("600123456");

        student.addPhone(phone);

        assertTrue(student.getPhone().isPresent());
        assertEquals(phone, student.getPhone().get());
    }

    @Test
    void shouldThrowExceptionWhenAddingPhoneTwice() {
        assertThrows(IllegalStateException.class, () -> {
            Student student = createStudent();
            Phone phone = Phone.of("600123456");
            Phone phone1 = Phone.of("600654321");

            student.addPhone(phone);
            student.addPhone(phone1);
        });

    }

    @Test
    void shouldUpdatePhone() {
        Student student = createStudent();
        Phone phone = Phone.of("600123456");
        Phone phone1 = Phone.of("600654321");

        student.addPhone(phone);
        student.updatePhone(phone1);

        assertTrue(student.getPhone().isPresent());
        assertEquals(phone1, student.getPhone().get());
    }

    @Test
    void shouldRemovePhone() {
        Student student = createStudent();
        Phone phone = Phone.of("600123456");

        student.addPhone(phone);
        student.removePhone();

        assertTrue(student.getPhone().isEmpty());
    }

    // ========== Contact Info Atomic Tests ==========
    @Test
    void shouldUpdateContactInfoAtomically() {
        Student student = createStudent();
        Email email = Email.of("dante@mail.com");
        Email email1 = Email.of("alighiero@mail.com");
        Phone phone = Phone.of("600123456");
        Phone phone1 = Phone.of("600654321");

        student.addEmail(email);
        student.addPhone(phone);
        student.updateContactInfo(email1, phone1);

        assertEquals(email1, student.getEmail().get());
        assertEquals(phone1, student.getPhone().get());
    }

    @Test
    void shouldAllowNullInUpdateContactInfo() {
        Student student = createStudent();
        Email email = Email.of("dante@mail.com");
        Phone phone = Phone.of("600123456");

        student.addEmail(email);
        student.addPhone(phone);

        student.updateContactInfo(null, null);

        assertTrue(student.getEmail().isEmpty());
        assertTrue(student.getPhone().isEmpty());
    }

    // ========== RGPD Management Tests ==========
    @Test
    void shouldGiveRgpdConsentInPerson() {
        Student student = createStudent();

        student.giveRgpdConsentInPerson();

        assertTrue(student.getRgpdConsent().getSignedDate().isPresent());
    }

    @Test
    void shouldGiveRgpdConsentOnline() {
        Student student = createStudent();

        student.giveRgpdConsentOnline();

        assertTrue(student.getRgpdConsent().getSignedDate().isPresent());
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