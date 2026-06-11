package com.orientation.backend.sessions.infraestructure.web.dto.response;

import com.orientation.backend.sessions.domain.model.entities.Session;

public record SessionResponse (
    String title,
    String description,
    String motivation,
    String sessionType,
    String sessionOrigin,
    boolean allWelcome
){

    public static SessionResponse fromDomain(Session session) {
        return new SessionResponse(
                session.getTitle(),
                session.getDescription(),
                session.getMotivation(),
                session.getSessionType().name(),
                session.getSessionOrigin().name(),
                session.isAllWelcome()
        );
    }
}
