package com.orientation.backend.sessions.application.commands;

import java.util.UUID;

public record AddAdvisorToSessionCommand (
        UUID sessionId,
        String advisorDni
){
}
