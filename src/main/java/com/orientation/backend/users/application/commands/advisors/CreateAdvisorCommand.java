package com.orientation.backend.users.application.commands.advisors;

public record CreateAdvisorCommand (
        String dni,
        String name,
        String surname,
        String email,
        String phone
){}
