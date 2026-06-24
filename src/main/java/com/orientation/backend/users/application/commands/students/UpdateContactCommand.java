package com.orientation.backend.users.application.commands.students;

public record UpdateContactCommand (
        String email,
        String phone
) {}