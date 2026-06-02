package com.orientation.backend.users.domain.model.entities;

import com.orientation.backend.users.domain.model.exceptions.PersonAlreadyInactiveException;
import com.orientation.backend.users.domain.model.valueobjects.Dni;
import com.orientation.backend.users.domain.model.valueobjects.Email;
import com.orientation.backend.users.domain.model.valueobjects.FullName;
import com.orientation.backend.users.domain.model.valueobjects.Phone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CollaboratorTest {

    private Dni testDni;
    private FullName testFullName;
    private Email testEmail;
    private Phone testPhone;
    private Organization testOrganization;

    @BeforeEach
    void setUp() {
        testDni = Dni.of("12345678Z");
        testFullName = FullName.of("Dante", "Alighiero");
        testEmail = Email.of("test@email.com");
        testPhone = Phone.of("600000000");
        testOrganization = Organization.builder()
                .name("Test Organization")
                .build();
    }

    // ========== Helper Methods ==========

    private Collaborator createCollaborator() {
        return Collaborator.builder()
                .dni(testDni)
                .fullName(testFullName)
                .external(false)
                .build();
    }

    private Collaborator createFullCollaborator() {
        return Collaborator.builder()
                .dni(testDni)
                .fullName(testFullName)
                .email(testEmail)
                .phone(testPhone)
                .external(true)
                .organization(testOrganization)
                .build();
    }

    // ========== Builder Tests ==========

    @Test
    void shouldCreateCollaboratorWithBuilder() {
        Collaborator collaborator = createCollaborator();

        assertNotNull(collaborator);
        assertEquals(testDni, collaborator.getDni());
        assertEquals(testFullName, collaborator.getFullName());
        assertFalse(collaborator.isExternal());
        assertNull(collaborator.getOrganization());
    }

    @Test
    void shouldCreateFullCollaboratorWithBuilder() {
        Collaborator collaborator = createFullCollaborator();

        assertNotNull(collaborator);
        assertEquals(testDni, collaborator.getDni());
        assertEquals(testFullName, collaborator.getFullName());
        assertTrue(collaborator.isExternal());
        assertNotNull(collaborator.getOrganization());
        assertEquals(testOrganization, collaborator.getOrganization());
    }

    @Test
    void shouldThrowExceptionForNullDni() {
        assertThrows(NullPointerException.class, () ->
                Collaborator.builder()
                        .dni(null)
                        .fullName(testFullName)
                        .external(false)
                        .build()
        );
    }

    @Test
    void shouldThrowExceptionForNullFullName() {
        assertThrows(NullPointerException.class, () ->
                Collaborator.builder()
                        .dni(testDni)
                        .fullName(null)
                        .external(false)
                        .build()
        );
    }

    // ========== Active / Deactivate Tests ==========

    @Test
    void shouldBeActiveWhenCreated() {
        Collaborator collaborator = createCollaborator();

        assertTrue(collaborator.isActive());
        assertTrue(collaborator.getDeletedAt().isEmpty());
    }

    @Test
    void shouldDeactivateCollaborator() {
        Collaborator collaborator = createCollaborator();

        collaborator.deactivate();

        assertFalse(collaborator.isActive());
        assertTrue(collaborator.getDeletedAt().isPresent());
    }

    @Test
    void shouldThrowExceptionWhenDeactivatingAlreadyInactive() {
        Collaborator collaborator = createCollaborator();
        collaborator.deactivate();

        assertThrows(PersonAlreadyInactiveException.class, collaborator::deactivate);
    }

    // ========== Email Management Tests ==========

    @Test
    void shouldAddEmail() {
        Collaborator collaborator = createCollaborator();
        Email email = Email.of("collaborator@mail.com");

        collaborator.addEmail(email);

        assertTrue(collaborator.getEmail().isPresent());
        assertEquals(email, collaborator.getEmail().get());
    }

    @Test
    void shouldThrowExceptionWhenAddingEmailTwice() {
        Collaborator collaborator = createCollaborator();
        collaborator.addEmail(Email.of("first@mail.com"));

        assertThrows(IllegalStateException.class, () ->
                collaborator.addEmail(Email.of("second@mail.com"))
        );
    }

    @Test
    void shouldUpdateEmail() {
        Collaborator collaborator = createCollaborator();
        collaborator.addEmail(Email.of("first@mail.com"));
        Email newEmail = Email.of("updated@mail.com");

        collaborator.updateEmail(newEmail);

        assertTrue(collaborator.getEmail().isPresent());
        assertEquals(newEmail, collaborator.getEmail().get());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentEmail() {
        Collaborator collaborator = createCollaborator();

        assertThrows(IllegalStateException.class, () ->
                collaborator.updateEmail(Email.of("updated@mail.com"))
        );
    }

    @Test
    void shouldRemoveEmail() {
        Collaborator collaborator = createCollaborator();
        collaborator.addEmail(Email.of("collaborator@mail.com"));

        collaborator.removeEmail();

        assertTrue(collaborator.getEmail().isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenRemovingNonExistentEmail() {
        Collaborator collaborator = createCollaborator();

        assertThrows(IllegalStateException.class, collaborator::removeEmail);
    }

    // ========== Phone Management Tests ==========

    @Test
    void shouldAddPhone() {
        Collaborator collaborator = createCollaborator();
        Phone phone = Phone.of("600123456");

        collaborator.addPhone(phone);

        assertTrue(collaborator.getPhone().isPresent());
        assertEquals(phone, collaborator.getPhone().get());
    }

    @Test
    void shouldThrowExceptionWhenAddingPhoneTwice() {
        Collaborator collaborator = createCollaborator();
        collaborator.addPhone(Phone.of("600123456"));

        assertThrows(IllegalStateException.class, () ->
                collaborator.addPhone(Phone.of("600654321"))
        );
    }

    @Test
    void shouldUpdatePhone() {
        Collaborator collaborator = createCollaborator();
        collaborator.addPhone(Phone.of("600123456"));
        Phone newPhone = Phone.of("600654321");

        collaborator.updatePhone(newPhone);

        assertTrue(collaborator.getPhone().isPresent());
        assertEquals(newPhone, collaborator.getPhone().get());
    }

    @Test
    void shouldRemovePhone() {
        Collaborator collaborator = createCollaborator();
        collaborator.addPhone(Phone.of("600123456"));

        collaborator.removePhone();

        assertTrue(collaborator.getPhone().isEmpty());
    }
}