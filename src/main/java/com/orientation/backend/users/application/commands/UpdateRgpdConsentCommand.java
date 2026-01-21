package com.orientation.backend.users.application.commands;

public record UpdateRgpdConsentCommand (
        String rgpdConsentStatus,
        Integer signedYear
){}
