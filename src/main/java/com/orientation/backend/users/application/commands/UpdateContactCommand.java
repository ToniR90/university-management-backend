package com.orientation.backend.users.application.commands;

public record UpdateContactCommand (
        String email,
        String phone
) {}