package com.orientation.backend.users.domain.model.entities;

import com.orientation.backend.users.domain.model.enums.*;
import com.orientation.backend.users.domain.model.valueobjects.Dni;
import com.orientation.backend.users.domain.model.valueobjects.Email;
import com.orientation.backend.users.domain.model.valueobjects.FullName;
import com.orientation.backend.users.domain.model.valueobjects.Phone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

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
        Phone phone = Phone.of("600123456");

        student.updateContactInfo(email, phone);

        assertTrue(student.getEmail().isPresent());
        assertEquals(email, student.getEmail().get());
        assertTrue(student.getPhone().isPresent());
        assertEquals(phone, student.getPhone().get());
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

        assertEquals(RgpdConsentStatus.SIGNED_IN_PERSON, student.getRgpdConsent().getStatus());
        assertTrue(student.getRgpdConsent().getSignedDate().isPresent());
    }

    @Test
    void shouldGiveRgpdConsentOnline() {
        Student student = createStudent();

        student.giveRgpdConsentOnline();

        assertEquals(RgpdConsentStatus.SIGNED_ONLINE, student.getRgpdConsent().getStatus());
        assertTrue(student.getRgpdConsent().getSignedDate().isPresent());
    }

    @Test
    void shouldRegisterPreviousRgpdConsent() {
        Student student = createStudent();

        student.registerPreviousRgpdConsent(2020);

        assertEquals(RgpdConsentStatus.ALREADY_SIGNED, student.getRgpdConsent().getStatus());
        assertTrue(student.getRgpdConsent().getSignedYear().isPresent());
        assertEquals(2020, student.getRgpdConsent().getSignedYear().get());
    }

    // ========== Alumni Management Tests ==========
    @Test
    void shouldMarkAsAlumni() {
        Student student = createStudent();

        student.markAsAlumni(AlumniType.MASTER, 2020);

        assertTrue(student.getAlumniInfo().isAlumni());
        assertTrue(student.getAlumniInfo().getType().isPresent());
        assertEquals(AlumniType.MASTER, student.getAlumniInfo().getType().get());
        assertTrue(student.getAlumniInfo().getGraduationYear().isPresent());
        assertEquals(2020, student.getAlumniInfo().getGraduationYear().get());
    }

    @Test
    void shouldMarkAsNonAlumni() {
        Student student = createStudent();

        student.markAsAlumni(AlumniType.MASTER, 2020);

        student.markAsNonAlumni();

        assertFalse(student.getAlumniInfo().isAlumni());
        assertTrue(student.getAlumniInfo().getType().isEmpty());
        assertTrue(student.getAlumniInfo().getGraduationYear().isEmpty());
    }

    // ========== Discovery & Contact Tracking Tests ==========
    @Test
    void shouldRegisterDiscoveryChannel() {
        Student student = createStudent();

        student.registerDiscoveryChannel(DiscoveryChannel.WEBSITE);

        assertTrue(student.getHowDidYouKnowUs().isPresent());
        assertEquals(DiscoveryChannel.WEBSITE, student.getHowDidYouKnowUs().get());
    }

    @Test
    void shouldRegisterContactMethod() {
        Student student = createStudent();

        student.registerContactMethod(ContactMethod.EMAIL);

        assertTrue(student.getHowDidYouContactUs().isPresent());
        assertEquals(ContactMethod.EMAIL, student.getHowDidYouContactUs().get());
    }

    // ========== Notes Management Tests ==========
    @Test
    void shouldUpdateCounselorNotes() {
        Student student = createStudent();
        String notes = "This is a test";

        student.updateCounselorNotes(notes);

        assertTrue(student.getCounselorNotes().isPresent());
        assertEquals("This is a test", student.getCounselorNotes().get());
    }

    @Test
    void shouldTrimCounselorNotes() {
        Student student = createStudent();
        String notes = "    Test    ";

        student.updateCounselorNotes(notes);

        assertTrue(student.getCounselorNotes().isPresent());
        assertEquals("Test", student.getCounselorNotes().get());
    }

    @Test
    void shouldAllowNullCounselorNotes() {
        Student student = createStudent();

        student.updateCounselorNotes("Test notes");

        student.updateCounselorNotes(null);

        assertTrue(student.getCounselorNotes().isEmpty());
    }

    // ========== Metadata Tests ==========
    @Test
    void shouldUpdateUpdateAtWhenModifying() throws InterruptedException {
        Student student = createStudent();
        LocalDateTime initialUpdateAt = student.getUpdatedAt();

        Thread.sleep(100);

        student.addEmail(Email.of("dante@mail.com"));

        assertTrue(student.getUpdatedAt().isAfter(initialUpdateAt));
    }

    // ========== Equals & HashCode Tests ==========
    @Test
    void shouldBeEqualById() {
        Student student1 = Student.builder()
                .id(1L)
                .dni(testDni)
                .fullName(testFullName)
                .degree("Videojocs")
                .currentYear(CurrentYear.FIRST)
                .build();

        Student student2 = Student.builder()
                .id(1L)
                .dni(Dni.of("00000000T"))
                .fullName(FullName.of("Jason", "Vorgees", null))
                .degree("Matemàtiques")
                .currentYear(CurrentYear.SECOND)
                .build();

        assertEquals(student1, student2);
    }

    @Test
    void shouldBeEqualByDniWhenNoId() {
        Student student1 = Student.builder()
                .dni(testDni)
                .fullName(testFullName)
                .degree("Videojocs")
                .currentYear(CurrentYear.FIRST)
                .build();

        Student student2 = Student.builder()
                .dni(testDni)
                .fullName(FullName.of("Jason", "Vorgees", null))
                .degree("Matemàtiques")
                .currentYear(CurrentYear.SECOND)
                .build();

        assertEquals(student1, student2);
    }

    @Test
    void shouldNotBeEqualWithDifferentId() {
        Student student1 = Student.builder()
                .id(1L)
                .dni(testDni)
                .fullName(testFullName)
                .degree("Videojocs")
                .currentYear(CurrentYear.FIRST)
                .build();

        Student student2 = Student.builder()
                .id(2L)
                .dni(testDni)
                .fullName(testFullName)
                .degree("Videojocs")
                .currentYear(CurrentYear.FIRST)
                .build();

        assertNotEquals(student1, student2);
    }

    @Test
    void shouldBeActiveWhenCreateStudent() {
    }

    @Test
    void shouldNotBeActiveWhenIsDeactivated() {

    }

    @Test
    void shouldThrowExceptionIfAlreadyDeactivated() {

    }
}