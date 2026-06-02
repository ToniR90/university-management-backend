package com.orientation.backend.sessions.application.commands;

public record CreateSessionCommand (
        String title,
        String description,
        String motivation,
        String sessionType,
        String sessionOrigin,
        boolean allWelcome
) {}
