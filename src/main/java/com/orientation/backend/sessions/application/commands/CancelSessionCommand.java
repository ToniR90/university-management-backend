package com.orientation.backend.sessions.application.commands;

import java.util.UUID;

public record CancelSessionCommand (
        UUID id,
        String cancelReason
){}
