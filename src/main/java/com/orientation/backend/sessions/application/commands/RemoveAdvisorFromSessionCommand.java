package com.orientation.backend.sessions.application.commands;

import java.util.UUID;

public record RemoveAdvisorFromSessionCommand (
        UUID sessionId,
        String advisorDni
){
}