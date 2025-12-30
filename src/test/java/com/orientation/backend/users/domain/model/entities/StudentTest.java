package com.orientation.backend.users.domain.model.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {


    // ========== Builder Tests ==========
    @Test
    void shouldCreateStudentWithBuilder() {

    }

    @Test
    void shouldCreateStudentWithDefaults() {

    }

    @Test
    void shouldThrowExceptionForNullDni() {

    }

    @Test
    void
     shouldThrowExceptionForNullFullName() {

    }

    @Test
    void shouldThrowExceptionForNullDegree() {

    }

    // ========== Email Management ==========
    @Test
    void shouldAddEmail() {

    }

    @Test
    void shoulThrowExceptionWhenAddingEmailTwice() {

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

    // ========== Phone Management ==========
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

    // ========== Contact Info Atomic ==========
    @Test
    void shouldUpdateContactInfoAtomically() {

    }

    @Test
    void shouldAllowNullInUpdateContactInfo() {

    }

    // ========== RGPD Management ==========
    @Test
    void shouldGiveRgpdConsentInPerson() {

    }

    @Test
    void shouldGiveRgpdConsentOnline() {

    }

    @Test
    void shouldRegisterPreviousRgpdConsent() {

    }

    // ========== Alumni Management ==========
    @Test
    void shouldMarkAsAlumni() {

    }

    @Test
    void shouldMarkAsNonAlumni() {

    }

    // ========== Discovery & Contact Tracking ==========
    @Test
    void shouldRegisterDiscoveryChannel() {

    }

    @Test
    void shouldRegisterContactMethod() {

    }

    // ========== Notes Management ==========
    @Test
    void shouldUpdateCounselorNotes() {

    }

    @Test
    void shouldTrimCounselorNotes() {

    }

    @Test
    void shouldAllowNullCounselorNotes() {

    }

    // ========== Metadata ==========
    @Test
    void shouldUpdateUpdateAtWhenModifying() {

    }

    // ========== Equals & HashCode ==========
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