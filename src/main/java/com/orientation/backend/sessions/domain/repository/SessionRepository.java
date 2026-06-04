package com.orientation.backend.sessions.domain.repository;

import com.orientation.backend.sessions.domain.model.entities.Session;
import com.orientation.backend.sessions.domain.model.query.SessionSearchCriteria;

import java.util.List;

public interface SessionRepository {
    Session save(Session session);
    List<Session> findAll();
    List<Session> findCancellable(SessionSearchCriteria criteria);
}
