package com.orientation.backend.sessions.infraestructure.web.dto.response;

import com.orientation.backend.sessions.domain.model.entities.Session;

import java.time.LocalDateTime;
import java.util.UUID;

public record CancellableSessionResponse (
        UUID id,
        String title,
        String description,
        String motivation,
        String sessionType,
        String sessionOrigin,
        LocalDateTime startDateTime,
        boolean allWelcome
){

    public static CancellableSessionResponse fromDomain(Session session){
        return new CancellableSessionResponse(
                session.getId(),
                session.getTitle(),
                session.getDescription(),
                session.getMotivation(),
                session.getSessionType().name(),
                session.getSessionOrigin().name(),
                session.getStartDateTime(),
                session.isAllWelcome()
        );
    }
}
