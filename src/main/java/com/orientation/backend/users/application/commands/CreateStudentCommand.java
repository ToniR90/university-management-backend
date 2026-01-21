package com.orientation.backend.users.application.commands;

public record CreateStudentCommand (
    String dni,
    String name,
    String firstSurname,
    String secondSurname,
    String email,
    String phone,
    String degree,
    String currentYear
) {}
