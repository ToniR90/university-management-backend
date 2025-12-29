package com.orientation.backend.users.domain.model.valueobjects;

import org.junit.jupiter.api.Test;

import java.time.Year;

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
        RgpdConsent rgpdConsent = RgpdConsent.signedInPerson();

        assertNotNull(rgpdConsent);
        assertTrue(rgpdConsent.getStatus().isSigned());
        assertNotNull(rgpdConsent.getSignedDate());
    }

    @Test
    void shouldCreateSignedOnline() {
        RgpdConsent rgpdConsent = RgpdConsent.signedOnline();

        assertNotNull(rgpdConsent);
        assertTrue(rgpdConsent.getStatus().isSigned());
        assertNotNull(rgpdConsent.getSignedDate());
    }

    // ===== Exception ==========
    @Test
    void shouldThrowExceptionForYearBefore2018() {
        assertThrows(IllegalArgumentException.class, () -> {
            RgpdConsent.alreadySigned(2000);
        });
    }

    @Test
    void shouldThrowExceptionForFutureYear() {
        assertThrows(IllegalArgumentException.class, () -> {
            RgpdConsent.alreadySigned(3000);
        });
    }

    @Test
    void shouldAcceptYear2018() {
        RgpdConsent rgpdConsent = RgpdConsent.alreadySigned(2018);

        assertNotNull(rgpdConsent);
        assertTrue(rgpdConsent.getSignedYear().isPresent());
        assertEquals(2018, rgpdConsent.getSignedYear().get());
    }

    @Test
    void shouldAcceptCurrentYear() {
        RgpdConsent rgpdConsent = RgpdConsent.alreadySigned(Year.now().getValue());

        assertNotNull(rgpdConsent);
        assertTrue(rgpdConsent.getSignedYear().isPresent());
        assertEquals(Year.now().getValue(), rgpdConsent.getSignedYear().get());
    }

    // ========== Equals & HashCode ==========
    @Test
    void shouldBeEqualByValue() {
        RgpdConsent rgpdConsent1 = RgpdConsent.alreadySigned(2020);
        RgpdConsent rgpdConsent2 = RgpdConsent.alreadySigned(2020);

        assertEquals(rgpdConsent1, rgpdConsent2);
    }

    @Test
    void shouldHaveSameHashCodeForSameValue() {

    }
}