package com.orientation.backend.users.domain.model.enums;


/**
 * RGPD consent status for students.
 * Represents the different states of RGPD consent.
 */

public enum RgpdConsentStatus {
    SIGNED_IN_PERSON("Signat presencialment"),
    SIGNED_ONLINE("Signat en línia"),
    PENDING("Pendent de signar"),
    ALREADY_SIGNED("Ja signat anteriorment");

    private final String displayName;

    RgpdConsentStatus(String displayName) {
        this.displayName = displayName;
    }

    public boolean isPending() {
        return this == PENDING;
    }

    public boolean isSigned() {
        return !isPending();
    }

    public boolean requiresYear() {
        return this == ALREADY_SIGNED;
    }

    public boolean requiresDate() {
        return this == SIGNED_IN_PERSON || this == SIGNED_ONLINE;
    }
}
