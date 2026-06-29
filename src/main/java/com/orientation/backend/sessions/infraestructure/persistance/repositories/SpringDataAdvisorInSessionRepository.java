package com.orientation.backend.sessions.infraestructure.persistance.repositories;

import com.orientation.backend.sessions.infraestructure.persistance.entities.AdvisorInSessionId;
import com.orientation.backend.sessions.infraestructure.persistance.entities.AdvisorInSessionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface SpringDataAdvisorInSessionRepository extends JpaRepository<AdvisorInSessionJpaEntity, AdvisorInSessionId>, JpaSpecificationExecutor<AdvisorInSessionJpaEntity> {
    List<AdvisorInSessionJpaEntity> findByIdSessionId(UUID sessionId);
    void deleteByIdSessionIdAndIdAdvisorId(UUID sessionId, UUID advisorId);
}