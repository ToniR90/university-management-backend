package com.orientation.backend.sessions.domain.model.enums;

public enum SessionType {
    ONLINE("Online"),
    IN_PERSON("In person");

    private final String displayName;

    SessionType(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static  SessionType fromString(String value){
        try {
            return SessionType.valueOf(value);
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Session type not valid: " + value);
        }
    }
}
