package com.orientation.backend.users.application.commands;

public record MarkAsAlumniCommand (
        String alumniType,
        int graduationYear
) {}
