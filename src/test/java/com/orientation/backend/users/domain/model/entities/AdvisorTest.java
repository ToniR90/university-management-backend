package com.orientation.backend.users.domain.model.entities;

import com.orientation.backend.users.domain.model.exceptions.PersonAlreadyInactiveException;
import com.orientation.backend.users.domain.model.valueobjects.Dni;
import com.orientation.backend.users.domain.model.valueobjects.Email;
import com.orientation.backend.users.domain.model.valueobjects.FullName;
import com.orientation.backend.users.domain.model.valueobjects.Phone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdvisorTest {

    private Dni testDni;
    private FullName testFullName;
    private Email testEmail;
    private Phone testPhone;

    @BeforeEach
    void setUp(){
        testDni = Dni.of("12345678Z");
        testFullName = FullName.of("Dante", "Alighiero");
        testEmail = Email.of("test@email.com");
        testPhone = Phone.of("600000000");
    }

    // ========== Helper Methods ==========
    private Advisor createAdvisor() {
        return Advisor.builder()
                .dni(testDni)
                .fullName(testFullName)
                .build();
    }

    private Advisor createFullAdvisor(){
        return Advisor.builder()
                .dni(testDni)
                .fullName(testFullName)
                .email(testEmail)
                .phone(testPhone)
                .build();
    }


    // ========== Builder Tests ==========

    @Test
    void shouldCreateAdvisorWithBuilder(){
        Advisor advisor = createAdvisor();

        assertNotNull(advisor);
        assertEquals(testDni, advisor.getDni());
        assertEquals(testFullName, advisor.getFullName());
    }

    @Test
    void shouldCreateFullAdvisorWithBuilder(){
        Advisor advisor = createFullAdvisor();

        assertNotNull(advisor);
        assertEquals(testDni, advisor.getDni());
        assertEquals(testFullName, advisor.getFullName());
        assertNotNull(advisor.getEmail());
        assertNotNull(advisor.getPhone());
    }

    @Test
    void shouldThrowExceptionForNullDni(){
        assertThrows(NullPointerException.class, () -> {
            Advisor advisor = Advisor.builder()
                    .dni(null)
                    .fullName(testFullName)
                    .build();
        });
    }

    @Test
    void shouldThrowExceptionForNullFullName(){
        assertThrows(NullPointerException.class, () -> {
            Advisor advisor = Advisor.builder()
                    .dni(testDni)
                    .fullName(null)
                    .build();
        });
    }


    // ============ Active / Deactivate Methods =======

    @Test
    void shouldBeActiveWhenCreateAdvisor(){
        Advisor advisor = createAdvisor();

        assertTrue(advisor.isActive());
        assertTrue(advisor.getDeletedAt().isEmpty());
    }

    @Test
    void shouldNotBeActiveWhenIsDeactivated(){
        Advisor advisor = createAdvisor();

        advisor.deactivate();
        assertFalse(advisor.isActive());
        assertTrue(advisor.getDeletedAt().isPresent());
    }

    @Test
    void shouldThrowExceptionIfAlreadyDeactivated(){
        Advisor advisor = createAdvisor();

        advisor.deactivate();

        assertThrows(PersonAlreadyInactiveException.class, advisor::deactivate);
    }


    // ========== Email Management Tests ==========

    @Test
    void shouldAddEmail() {
        Advisor advisor = createAdvisor();
        Email email = Email.of("dante@mail.com");

        advisor.addEmail(email);

        assertTrue(advisor.getEmail().isPresent());
        assertEquals(email, advisor.getEmail().get());
    }

    @Test
    void shouldThrowExceptionWhenAddingEmailTwice() {
        assertThrows(IllegalStateException.class, () -> {
            Advisor advisor = createAdvisor();
            advisor.addEmail(Email.of("dante@mail.com"));
            advisor.addEmail(Email.of("dante2@mail.com"));
        });
    }

    @Test
    void shouldUpdateEmail() {
        Advisor advisor = createAdvisor();
        Email email = Email.of("dante@mail.com");
        advisor.addEmail(email);
        Email email1 = Email.of("alighiero@mail.com");
        advisor.updateEmail(email1);

        assertTrue(advisor.getEmail().isPresent());
        assertEquals(email1, advisor.getEmail().get());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentEmail() {
        assertThrows(IllegalStateException.class, () -> {
            Advisor advisor = createAdvisor();
            Email email = Email.of("alighiero@mail.com");
            advisor.updateEmail(email);
        });
    }

    @Test
    void shouldRemoveEmail() {
        Advisor advisor = createAdvisor();
        Email email = Email.of("dante@mail.com");
        advisor.addEmail(email);
        advisor.removeEmail();

        assertTrue(advisor.getEmail().isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenRemovingNonExistentEmail() {
        assertThrows(IllegalStateException.class, () -> {
            Advisor advisor = createAdvisor();

            advisor.removeEmail();
        });
    }

    // ========== Phone Management Tests ==========
    @Test
    void shouldAddPhone() {
        Advisor advisor = createAdvisor();
        Phone phone = Phone.of("600123456");

        advisor.addPhone(phone);

        assertTrue(advisor.getPhone().isPresent());
        assertEquals(phone, advisor.getPhone().get());
    }

    @Test
    void shouldThrowExceptionWhenAddingPhoneTwice() {
        assertThrows(IllegalStateException.class, () -> {
            Advisor advisor = createAdvisor();
            Phone phone = Phone.of("600123456");
            Phone phone1 = Phone.of("600654321");

            advisor.addPhone(phone);
            advisor.addPhone(phone1);
        });

    }

    @Test
    void shouldUpdatePhone() {
        Advisor advisor = createAdvisor();
        Phone phone = Phone.of("600123456");
        Phone phone1 = Phone.of("600654321");

        advisor.addPhone(phone);
        advisor.updatePhone(phone1);

        assertTrue(advisor.getPhone().isPresent());
        assertEquals(phone1, advisor.getPhone().get());
    }

    @Test
    void shouldRemovePhone() {
        Advisor advisor = createAdvisor();
        Phone phone = Phone.of("600123456");

        advisor.addPhone(phone);
        advisor.removePhone();

        assertTrue(advisor.getPhone().isEmpty());
    }
}