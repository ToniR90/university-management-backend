package com.orientation.backend.users.domain.model.enums;

// TODO: Review with client in Sprint 2 - temporary values

public enum CurrentYear {
    FIRST("1r"),
    SECOND("2n"),
    THIRD("3r"),
    FOURTH("4t"),
    FIFTH("5è"),
    SIXTH("6è"),
    MASTER("Màster"),
    DOCTORATE("Doctorat");

    private final String displayName;

    CurrentYear(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isGraduateLevel() {
        return this == MASTER || this == DOCTORATE;
    }

    public boolean isUndergraduate() {
        return !isGraduateLevel();
    }
}
