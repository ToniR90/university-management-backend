package com.orientation.backend.users.domain.model.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RgpdConsentTest {

    // ========== Validation ==========
    @Test
    void shouldCreatePending() {
        RgpdConsent rgpdConsent = RgpdConsent.pending();

        assertNotNull(rgpdConsent);
        assertTrue(rgpdConsent.getStatus().isPending());
        assertTrue(rgpdConsent.getSignedDate().isEmpty());
        assertTrue(rgpdConsent.getSignedYear().isEmpty());
    }

    @Test
    void shouldCreateAlreadySigned() {
        RgpdConsent rgpdConsent = RgpdConsent.alreadySigned(2020);

        assertNotNull(rgpdConsent);
        assertTrue(rgpdConsent.getStatus().isSigned());
    }

    @Test
    void shouldCreateSignedInPerson() {

    }

    @Test
    void shouldCreateSignedOnline() {

    }

    // ===== Exception ==========
    @Test
    void shouldThrowExceptionForYearBefore2018() {

    }

    @Test
    void shouldThrowExceptionForFutureYear() {

    }

    @Test
    void shouldAcceptYear2018() {

    }

    @Test
    void shouldAcceptCurrentYear() {

    }

    // ========== Equals & HashCode ==========
    @Test
    void shouldBeEqualByValue() {

    }

    @Test
    void shouldHaveSameHashCodeForSameValue() {

    }
}