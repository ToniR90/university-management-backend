package com.orientation.backend.sessions.domain.repository;

import com.orientation.backend.sessions.domain.model.entities.Session;
import com.orientation.backend.sessions.domain.model.query.SessionSearchCriteria;
import com.orientation.backend.shared.domain.model.query.PageResult;
import com.orientation.backend.shared.domain.model.query.Pagination;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionRepository {
    Session save(Session session);
    List<Session> findAll();
    List<Session> findCancellable(SessionSearchCriteria criteria);
    Optional<Session> findById(UUID id);
    PageResult<Session> search(SessionSearchCriteria criteria, Pagination pagination);
}
