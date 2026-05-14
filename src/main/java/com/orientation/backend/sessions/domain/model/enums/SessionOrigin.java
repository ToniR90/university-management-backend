package com.orientation.backend.sessions.domain.model.enums;

public enum SessionOrigin {
    OFFERED("Offered"),
    REQUESTED("Requested");

    private final String displayName;

    SessionOrigin(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return displayName;
    }

    public static SessionOrigin fromString(String value){
        try{
            return SessionOrigin.valueOf(value);
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Session origin not valid");
        }
    }
}
