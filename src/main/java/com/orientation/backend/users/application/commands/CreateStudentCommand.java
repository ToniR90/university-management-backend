package com.orientation.backend.users.application.commands;

public record CreateStudentCommand (
    String dni,
    String name,
    String surname,
    String email,
    String phone,
    String degree,
    String currentYear
) {}
