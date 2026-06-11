package com.orientation.backend.sessions.application.commands;

import java.util.UUID;

public record RemoveAssistantCommand (
        UUID sessionId,
        String personDni
){
}
