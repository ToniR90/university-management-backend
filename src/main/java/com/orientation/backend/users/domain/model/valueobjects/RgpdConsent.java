package com.orientation.backend.users.domain.model.valueobjects;

import com.orientation.backend.users.domain.model.enums.RgpdConsentStatus;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.Objects;
import java.util.Optional;

/**
 * Value Object representing RGPD consent information.
 * Composite VO with complex consistency validation.
 *
 * Business rules:
 * - PENDING: no year, no date
 * - ALREADY_SIGNED: requires year (>= 2018, not future), no date
 * - SIGNED_IN_PERSON/SIGNED_ONLINE: requires date (auto-generated as NOW), no year
 *
 * Immutable and self-validated.
 */
public final class RgpdConsent {

    private static final int RGPD_START_YEAR = 2018;

    private final RgpdConsentStatus status;
    private final Integer signedYear; // Nullable
    private final LocalDateTime signedDate; // Nullable

    // Private constructor
    private RgpdConsent(RgpdConsentStatus status, Integer signedYear, LocalDateTime signedDate) {
        validateConsistency(status, signedYear, signedDate);
        this.status = status;
        this.signedYear = signedYear;
        this.signedDate = signedDate;
    }

    // Factory method: pending
    public static RgpdConsent pending() {
        return new RgpdConsent(RgpdConsentStatus.PENDING, null, null);
    }

    // Factory method: already signed (year provided)
    public static RgpdConsent alreadySigned(Integer year) {
        validateYear(year);
        return new RgpdConsent(RgpdConsentStatus.ALREADY_SIGNED, year, null);
    }

    // Factory method: signed in person (date = NOW)
    public static RgpdConsent signedInPerson() {
        return new RgpdConsent(RgpdConsentStatus.SIGNED_IN_PERSON, null, LocalDateTime.now());
    }

    // Factory method: reconstruct signed in person (from persistence)
    public static RgpdConsent signedInPerson(LocalDateTime signedDate) {
        Objects.requireNonNull(signedDate, "La data de signa no pot ser nul");
        return new RgpdConsent(RgpdConsentStatus.SIGNED_IN_PERSON, null, signedDate);
    }

    // Factory method: signed online (date = NOW)
    public static RgpdConsent signedOnline() {
        return new RgpdConsent(RgpdConsentStatus.SIGNED_ONLINE, null, LocalDateTime.now());
    }

    // Factory method: reconstruct signed online (from persistence)
    public static RgpdConsent signedOnline(LocalDateTime signedDate) {
        Objects.requireNonNull(signedDate, "La data de signa no pot ser nul");
        return new RgpdConsent(RgpdConsentStatus.SIGNED_ONLINE, null, signedDate);
    }

    // Validate year
    private static void validateYear(Integer year) {
        Objects.requireNonNull(year, "L'any de signa no pot ser nul");

        if (year < RGPD_START_YEAR) {
            throw new IllegalArgumentException("L'any de signa de RGPD ha de ser " + RGPD_START_YEAR + " o posterior");
        }

        int currentYear = Year.now().getValue();
        if (year > currentYear) {
            throw new IllegalArgumentException("L'any de signa no pot ser en el futur");
        }
    }

    // Consistency validation
    private static void validateConsistency(RgpdConsentStatus status, Integer signedYear, LocalDateTime signedDate) {
        Objects.requireNonNull(status, "L'estat de RGPD no pot ser nul");

        switch (status) {
            case PENDING:
                if (signedYear != null || signedDate != null) {
                    throw new IllegalArgumentException("PENDING no pot tenir any ni data de signa");
                }
                break;

            case ALREADY_SIGNED:
                if (signedYear == null) {
                    throw new IllegalArgumentException("ALREADY_SIGNED necessita un any de signa");
                }
                if (signedDate != null) {
                    throw new IllegalArgumentException("ALREADY_SIGNED NO hauria de tenir una data exacta (només any)");
                }
                break;

            case SIGNED_IN_PERSON:
            case SIGNED_ONLINE:
                if (signedDate == null) {
                    throw new IllegalArgumentException(status + " necessita una data de signa");
                }
                if (signedYear != null) {
                    throw new IllegalArgumentException(status + " NO hauria de tenir un any separat (només data exacta)");
                }
                break;
        }
    }

    // Getters
    public RgpdConsentStatus getStatus() {
        return status;
    }

    public Optional<Integer> getSignedYear() {
        return Optional.ofNullable(signedYear);
    }

    public Optional<LocalDateTime> getSignedDate() {
        return Optional.ofNullable(signedDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RgpdConsent that)) return false;
        return status == that.status &&
                Objects.equals(signedYear, that.signedYear) &&
                Objects.equals(signedDate, that.signedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, signedYear, signedDate);
    }

    @Override
    public String toString() {
        return "Estat RGPD:  " + "\n" +
                "Estat: " + status + "\n" +
                "Any de signa: " + signedYear + "\n" +
                "Data de signa: " + signedDate;
    }
}