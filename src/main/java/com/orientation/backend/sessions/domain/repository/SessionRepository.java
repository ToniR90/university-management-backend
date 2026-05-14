package com.orientation.backend.sessions.domain.repository;

import com.orientation.backend.sessions.domain.model.entities.Session;

import java.util.List;

public interface SessionRepository {
    Session save(Session session);
    List<Session> findAll();
}
