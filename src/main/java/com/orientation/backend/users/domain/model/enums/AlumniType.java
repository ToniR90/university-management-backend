package com.orientation.backend.users.domain.model.enums;

// TODO: Review with client in Sprint 2 - temporary values

public enum AlumniType {
    BACHELOR("Grau"),
    MASTER("Màster"),
    DOCTORATE("Doctorat"),
    DOUBLE_DEGREE("Doble Grau"),
    ERASMUS("Erasmus"),
    EXCHANGE("Intercanvi"),
    OTHER("Altre");

    private final String displayName;

    AlumniType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
