package com.orientation.backend.sessions.application.commands;

import java.util.UUID;

public record AddAssistantCommand (
        UUID sessionId,
        String personDni
){}
