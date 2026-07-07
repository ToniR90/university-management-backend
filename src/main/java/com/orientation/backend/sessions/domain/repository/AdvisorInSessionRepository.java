package com.orientation.backend.sessions.domain.repository;

import com.orientation.backend.sessions.domain.model.entities.AdvisorInSession;

import java.util.List;
import java.util.UUID;

public interface AdvisorInSessionRepository {
    AdvisorInSession add(AdvisorInSession advisorInSession);
    void remove(UUID sessionId, UUID advisorId);
    List<AdvisorInSession> findBySessionId(UUID sessionId);
}