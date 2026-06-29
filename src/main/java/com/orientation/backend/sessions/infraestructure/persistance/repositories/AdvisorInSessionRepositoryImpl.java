package com.orientation.backend.sessions.infraestructure.persistance.repositories;

import com.orientation.backend.sessions.domain.model.entities.AdvisorInSession;
import com.orientation.backend.sessions.domain.repository.AdvisorInSessionRepository;
import com.orientation.backend.sessions.infraestructure.persistance.entities.AdvisorInSessionJpaEntity;
import com.orientation.backend.sessions.infraestructure.persistance.mappers.AdvisorInSessionJpaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AdvisorInSessionRepositoryImpl implements AdvisorInSessionRepository {

    private final SpringDataAdvisorInSessionRepository jpaRepository;

    @Override
    public AdvisorInSession add(AdvisorInSession advisorInSession) {
        AdvisorInSessionJpaEntity jpaEntity = AdvisorInSessionJpaMapper.toJpaEntity(advisorInSession);
        AdvisorInSessionJpaEntity added = jpaRepository.save(jpaEntity);
        return AdvisorInSessionJpaMapper.toDomain(added);
    }

    @Override
    public void remove(UUID sessionId, UUID advisorId) {
        jpaRepository.deleteByIdSessionIdAndIdAdvisorId(sessionId, advisorId);
    }

    @Override
    public List<AdvisorInSession> findBySessionId(UUID sessionId) {
        return jpaRepository.findByIdSessionId(sessionId)
                .stream()
                .map(AdvisorInSessionJpaMapper::toDomain)
                .toList();
    }
}