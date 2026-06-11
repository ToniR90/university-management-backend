package com.orientation.backend.sessions.infraestructure.persistance.repositories;

import com.orientation.backend.sessions.domain.model.entities.Session;
import com.orientation.backend.sessions.domain.model.query.SessionSearchCriteria;
import com.orientation.backend.sessions.domain.repository.SessionRepository;
import com.orientation.backend.sessions.infraestructure.persistance.entities.SessionJpaEntity;
import com.orientation.backend.sessions.infraestructure.persistance.mappers.SessionJpaMapper;
import com.orientation.backend.sessions.infraestructure.persistance.specifications.SessionSpecifications;
import com.orientation.backend.shared.domain.model.query.PageResult;
import com.orientation.backend.shared.domain.model.query.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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
        Specification<SessionJpaEntity> spec = SessionSpecifications.isCancellable();

        if (criteria.getTitle() != null) {
            spec = spec.and(SessionSpecifications.hasTitle(criteria.getTitle()));
        }

        return jpaRepository.findAll(spec)
                .stream()
                .map(SessionJpaMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Session> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(SessionJpaMapper::toDomain);
    }

    @Override
    public PageResult<Session> search(SessionSearchCriteria criteria, Pagination pagination) {
        if (criteria == null) {
            throw new IllegalArgumentException("Search criteria cannot be null");
        }
        if (pagination == null) {
            throw new IllegalArgumentException("Pagination cannot be null");
        }

        Specification<SessionJpaEntity> specification = SessionSpecifications.fromCriteria(criteria);

        PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getSize());

        Page<SessionJpaEntity> page = jpaRepository.findAll(specification, pageRequest);

        List<Session> sessions = page.getContent().stream().map(SessionJpaMapper::toDomain).toList();

        return new PageResult<>(sessions, page.getNumber(), pagination.getSize(), page.getTotalElements(), page.getTotalPages());
    }
}
