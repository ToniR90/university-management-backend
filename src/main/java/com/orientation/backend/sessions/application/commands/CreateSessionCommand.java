package com.orientation.backend.sessions.application.commands;

import java.time.LocalDateTime;

public record CreateSessionCommand (
        String title,
        String description,
        String motivation,
        String sessionType,
        String sessionOrigin,
        boolean allWelcome,
        LocalDateTime startDateTime
){}
