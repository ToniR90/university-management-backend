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
 * - ALREADY_SIGNED: requires year (>= 2018, not future)
 * - SIGNED_IN_PERSON/SIGNED_ONLINE: requires date (auto-generated as NOW)
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
        // TODO: Validar año antes de crear
        validateYear(year);
        return new RgpdConsent(RgpdConsentStatus.ALREADY_SIGNED, year, null);
    }

    // Factory method: signed in person (date = NOW)
    public static RgpdConsent signedInPerson() {
        return new RgpdConsent(RgpdConsentStatus.SIGNED_IN_PERSON, null, LocalDateTime.now());
    }

    // Factory method: signed online (date = NOW)
    public static RgpdConsent signedOnline() {
        return new RgpdConsent(RgpdConsentStatus.SIGNED_ONLINE, null, LocalDateTime.now());
    }

    // Validate year
    private static void validateYear(Integer year) {
        // TODO: Validar que no sea null

        // TODO: Validar que sea >= 2018 (RGPD start year)
        // if (year < RGPD_START_YEAR) {
        //     throw new IllegalArgumentException("RGPD year cannot be before 2018: " + year);
        // }

        // TODO: Validar que no sea futuro
        // int currentYear = Year.now().getValue();
        // if (year > currentYear) {
        //     throw new IllegalArgumentException("Signed year cannot be in the future: " + year);
        // }
    }

    // Consistency validation
    private static void validateConsistency(RgpdConsentStatus status, Integer signedYear, LocalDateTime signedDate) {
        // TODO: Validar que status no sea null

        // TODO: Validar según status
        switch (status) {
            case PENDING:
                // TODO: year y date deben ser null
                // if (signedYear != null || signedDate != null) {
                //     throw new IllegalArgumentException("PENDING status cannot have year or date");
                // }
                break;

            case ALREADY_SIGNED:
                // TODO: year debe estar presente, date debe ser null
                // if (signedYear == null) {
                //     throw new IllegalArgumentException("ALREADY_SIGNED requires year");
                // }
                // if (signedDate != null) {
                //     throw new IllegalArgumentException("ALREADY_SIGNED should not have date");
                // }
                break;

            case SIGNED_IN_PERSON:
            case SIGNED_ONLINE:
                // TODO: date debe estar presente, year debe ser null
                // if (signedDate == null) {
                //     throw new IllegalArgumentException(status + " requires date");
                // }
                // if (signedYear != null) {
                //     throw new IllegalArgumentException(status + " should not have year");
                // }
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

    // TODO: Generate equals & hashCode (Select ALL three fields)


    // TODO: Generate toString

}