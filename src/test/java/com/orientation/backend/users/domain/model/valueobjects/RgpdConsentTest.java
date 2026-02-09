package com.orientation.backend.users.domain.model.valueobjects;

import com.orientation.backend.users.domain.model.enums.RgpdConsentStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class RgpdConsentTest {

    // ========== Validation ==========
    @Test
    void shouldCreatePending() {
        RgpdConsent rgpdConsent = RgpdConsent.pending();

        assertNotNull(rgpdConsent);
        assertEquals(RgpdConsentStatus.PENDING, rgpdConsent.getStatus());
        assertTrue(rgpdConsent.getSignedDate().isEmpty());
        assertTrue(rgpdConsent.getSignedYear().isEmpty());
    }

    @Test
    void shouldCreateAlreadySigned() {
        RgpdConsent rgpdConsent = RgpdConsent.alreadySigned(2020);

        assertNotNull(rgpdConsent);
        assertEquals(RgpdConsentStatus.ALREADY_SIGNED, rgpdConsent.getStatus());
        assertTrue(rgpdConsent.getSignedYear().isPresent());
        assertEquals(2020, rgpdConsent.getSignedYear().get());
        assertTrue(rgpdConsent.getSignedDate().isEmpty());
    }

    @Test
    void shouldCreateSignedInPerson() {
        RgpdConsent rgpdConsent = RgpdConsent.signedInPerson();

        assertNotNull(rgpdConsent);
        assertEquals(RgpdConsentStatus.SIGNED_IN_PERSON, rgpdConsent.getStatus());
        assertTrue(rgpdConsent.getSignedDate().isPresent());
        assertTrue(rgpdConsent.getSignedYear().isEmpty());
    }

    @Test
    void shouldCreateSignedOnline() {
        RgpdConsent rgpdConsent = RgpdConsent.signedOnline();

        assertNotNull(rgpdConsent);
        assertEquals(RgpdConsentStatus.SIGNED_ONLINE, rgpdConsent.getStatus());
        assertTrue(rgpdConsent.getSignedDate().isPresent());
        assertTrue(rgpdConsent.getSignedYear().isEmpty());
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
        RgpdConsent rgpdConsent1 = RgpdConsent.alreadySigned(2020);
        RgpdConsent rgpdConsent2 = RgpdConsent.alreadySigned(2020);

        assertEquals(rgpdConsent1.hashCode(), rgpdConsent2.hashCode());
    }

    @Test
    void shouldCreateSignedInPersonWithDate() {
        LocalDateTime date = LocalDateTime.of(2023, 6, 15, 10, 30);
        RgpdConsent rgpdConsent = RgpdConsent.signedInPerson(date);

        assertEquals(RgpdConsentStatus.SIGNED_IN_PERSON, rgpdConsent.getStatus());
        assertTrue(rgpdConsent.getSignedDate().isPresent());
        assertEquals(date, rgpdConsent.getSignedDate().get());
    }

    @Test
    void shouldCreateSignedOnlineWithDate() {
        LocalDateTime date = LocalDateTime.of(2024, 1, 20, 14, 0);
        RgpdConsent rgpdConsent = RgpdConsent.signedOnline(date);

        assertEquals(RgpdConsentStatus.SIGNED_ONLINE, rgpdConsent.getStatus());
        assertTrue(rgpdConsent.getSignedDate().isPresent());
        assertEquals(date, rgpdConsent.getSignedDate().get());
    }

    @Test
    void shouldThrowExceptionForNullDateInSignedInPerson() {
        assertThrows(NullPointerException.class, () -> {
            RgpdConsent.signedInPerson(null);
        });
    }
}