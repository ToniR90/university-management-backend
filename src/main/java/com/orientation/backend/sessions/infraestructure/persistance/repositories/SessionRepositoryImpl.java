package com.orientation.backend.sessions.infraestructure.persistance.repositories;

import com.orientation.backend.sessions.domain.model.entities.Session;
import com.orientation.backend.sessions.domain.model.query.SessionSearchCriteria;
import com.orientation.backend.sessions.domain.repository.SessionRepository;
import com.orientation.backend.sessions.infraestructure.persistance.entities.SessionJpaEntity;
import com.orientation.backend.sessions.infraestructure.persistance.mappers.SessionJpaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SessionRepositoryImpl implements SessionRepository {

    private final SpringDataSessionRepository jpaRepository;

    @Override
    public Session save(Session session) {
        SessionJpaEntity jpaEntity = SessionJpaMapper.toJpaEntity(session);

        SessionJpaEntity saved = jpaRepository.save(jpaEntity);

        return SessionJpaMapper.toDomain(saved);
    }

    @Override
    public List<Session> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(SessionJpaMapper::toDomain)
                .toList();
    }

    @Override
    public List<Session> findCancellable(SessionSearchCriteria criteria) {
        return jpaRepository.findCancellable(criteria.getTitle())
                .stream()
                .map(SessionJpaMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Session> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(SessionJpaMapper::toDomain);
    }
}
