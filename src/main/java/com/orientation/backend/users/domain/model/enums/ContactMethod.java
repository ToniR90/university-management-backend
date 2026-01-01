package com.orientation.backend.users.domain.model.enums;

// TODO: Review with client in Sprint 2 - temporary values

public enum ContactMethod {
    EMAIL("Correu electrònic"),
    PHONE("Telèfon"),
    IN_PERSON("Presencial"),
    ONLINE_FORM("Formulari web"),
    REFERRAL("Recomanació"),
    OTHER("Altre");

    private final String displayName;

    ContactMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
