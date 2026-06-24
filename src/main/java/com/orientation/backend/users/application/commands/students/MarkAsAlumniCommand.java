package com.orientation.backend.users.application.commands.students;

public record MarkAsAlumniCommand (
        String alumniType,
        int graduationYear
) {}
