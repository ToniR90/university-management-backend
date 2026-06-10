package com.orientation.backend.sessions.domain.repository;

import com.orientation.backend.sessions.domain.model.entities.Assistant;

import java.util.List;
import java.util.UUID;

public interface AssistantRepository {
    Assistant add(Assistant assistant);
    void remove(UUID sessionId, UUID personId);
    List<Assistant> findBySessionId(UUID sessionId);
}